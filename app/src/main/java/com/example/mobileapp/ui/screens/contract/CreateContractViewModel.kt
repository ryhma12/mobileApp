package com.example.mobileapp.ui.screens.contract

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.util.Log

import android.graphics.Paint
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mobileapp.model.Account
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await


class CreateContractViewModel(private val selectedContactUid: String) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val savedStateHandle = extras.createSavedStateHandle()

                val selectedContactUid = savedStateHandle.get<String>("selectedContactUid")!!
                return CreateContractViewModel(selectedContactUid) as T
            }
        }
    }

    private val firestore = FirebaseFirestore.getInstance()
    var recipient: Account? = null
    var currentUser: Account? = null

    private val currentUserUid = Firebase.auth.currentUser?.uid

    init {
        if (currentUserUid != null) {
            fetchUserByUid(selectedContactUid, currentUserUid)
        }
    }

    private fun fetchUserByUid(recipientUid: String, companyUid: String) {
        viewModelScope.launch {
            try {
                val recipientDocument = firestore.collection("users").document(recipientUid).get().await()
                if (recipientDocument.exists()) {
                    recipient = recipientDocument.toObject<Account>()
                } else {
                    Log.d("404", "user not found")
                }

                val currentUserDocument = firestore.collection("users").document(companyUid).get().await()
                if (currentUserDocument.exists()) {
                    currentUser = currentUserDocument.toObject<Account>()
                } else {
                    Log.d("404", "user not found")
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
        saveContractInfoToFirebase(price, recipient, company, context)
    }

    private suspend fun saveContractInfoToFirebase(
        price: String,
        recipient: String,
        company: String,
        context: Context
    ) {
        val firestore = FirebaseFirestore.getInstance().collection("contracts")

        val contractData = hashMapOf(
            "recipient" to recipient,
            "recipientUid" to selectedContactUid,
            "company" to company,
            "companyUid" to currentUser?.uid,
            "price" to price,
            "status" to "pending"
        )

        firestore.add(contractData)
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to save contract to firestore: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

}

