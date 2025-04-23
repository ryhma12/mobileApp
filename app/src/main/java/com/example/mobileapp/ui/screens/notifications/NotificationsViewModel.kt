import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.mobileapp.model.Contract

class NotificationsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _contracts = MutableStateFlow<List<Contract>>(emptyList())
    val contracts: StateFlow<List<Contract>> get() = _contracts

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    init {
        getContractsByCurrentUser()
    }

    fun getContractsByCurrentUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserUid = currentUser?.uid

        if (currentUserUid != null) {
            viewModelScope.launch {
                try {
                    val recipientDocs = db.collection("contracts")
                        .whereEqualTo("recipientUid", currentUserUid)
                        .get()
                        .await()

                    val companyDocs = db.collection("contracts")
                        .whereEqualTo("companyUid", currentUserUid)
                        .get()
                        .await()

                    val allDocs = (recipientDocs.documents + companyDocs.documents)
                        .distinctBy { it.id }

                    val contractsList = allDocs.map { doc ->
                        val data = doc.data ?: emptyMap()
                        Contract(
                            company = data["company"] as? String ?: "Unknown Company",
                            recipient = data["recipient"] as? String ?: "Unknown Recipient",
                            price = data["price"] as? String ?: "0.00",
                            status = data["status"] as? String ?: "pending",
                            contractId = doc.id
                        )
                    }

                    _contracts.value = contractsList
                    Log.d("notifications", "Contracts fetched: $contractsList")
                } catch (e: Exception) {
                    _error.value = "Error retrieving contracts: ${e.message}"
                    Log.e("notifications", "Error fetching contracts", e)
                }
            }
        } else {
            _error.value = "User is not logged in"
        }
    }

    fun declineContract(contractId: String) {
        viewModelScope.launch {
            try {
                db.collection("contracts").document(contractId)
                    .update("status", "archive")
                    .await()
                getContractsByCurrentUser()
            } catch (e: Exception) {
                _error.value = "Error updating contract: ${e.message}"
            }
        }
    }

    fun acceptContract(contractId: String) {
        viewModelScope.launch {
            try {
                db.collection("contracts").document(contractId)
                    .update("status", "ongoing")
                    .await()
                getContractsByCurrentUser()
            } catch (e: Exception) {
                _error.value = "Error updating contract: ${e.message}"
            }
        }
    }

    fun closeContract(contractId: String) {
        viewModelScope.launch {
            try {
                db.collection("contracts").document(contractId)
                    .update("status", "archive")
                    .await()
                getContractsByCurrentUser()
            } catch (e: Exception) {
                _error.value = "Error updating contract: ${e.message}"
            }
        }
    }

    suspend fun generatePDF(
        price: String,
        recipient: String,
        company: String,
        context: Context
    ) {
        if (recipient.isEmpty() || company.isEmpty() || price.isEmpty()) return

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 20f
        paint.isFakeBoldText = true
        canvas.drawText("Contract", (canvas.width / 2).toFloat(), 140f, paint)

        paint.textSize = 14f
        paint.isFakeBoldText = false
        canvas.drawText("Sample contract description", (canvas.width / 2).toFloat(), 180f, paint)

        paint.color = android.graphics.Color.BLACK
        canvas.drawLine(20f, 210f, canvas.width.toFloat() - 20, 210f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 14f
        paint.color = android.graphics.Color.BLACK
        canvas.drawText("Company: $company", 20f, 450f, paint)
        canvas.drawText("Recipient: $recipient", 20f, 470f, paint)
        canvas.drawText("Price: $price", 20f, 490f, paint)

        pdfDocument.finishPage(page)

        val pdfFileName = "Contract_${company}_${recipient}.pdf"
        val resolver = context.contentResolver
        val contentValue = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }
        val pdfUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValue)

        if (pdfUri != null) {
            resolver.openOutputStream(pdfUri).use {
                pdfDocument.writeTo(it)
                Toast.makeText(context, "PDF saved as $pdfFileName", Toast.LENGTH_LONG).show()
            }

            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(pdfUri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(openIntent, "Open PDF with")
            try {
                context.startActivity(chooser)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "No app found to open PDF", Toast.LENGTH_SHORT).show()
            }
        }

        pdfDocument.close()
    }
}
