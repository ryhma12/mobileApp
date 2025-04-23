import android.util.Log
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
                            price = data["price"] as? String ?: "0.00"
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
}
