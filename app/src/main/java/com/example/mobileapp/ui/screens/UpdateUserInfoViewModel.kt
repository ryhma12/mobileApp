package com.example.mobileapp.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl("http://10.0.2.2:3001/")
    .build()

private interface ApiService {
    @POST("bio/validate")
    suspend fun validateBio(
        @Body body: Map<String, String>,
        @Header("authorization") idToken: String
    ): String
}

class UpdateUserInfoViewModel() : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = firebaseAuth.currentUser
    private val firestore = FirebaseFirestore.getInstance()
    private val userRef = user?.let {
        firestore.collection("users").document(it.uid)
    }
    private val updates = mutableMapOf<String, Any>()

    fun updateUserInfo(newUsername: String?, newEmail: String?, description: String?) {
        updates.clear()

        newUsername?.let { updates["username"] = it }
        newEmail?.let { updates["email"] = it }
        //description?.let { updates["description"] = it }

        if(!description.isNullOrBlank()){
            val mUser = FirebaseAuth.getInstance().currentUser
            mUser?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String? = task.result.token
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val service = retrofit.create<ApiService>()
                            val response = service.validateBio(
                                mapOf("bio" to description),
                                idToken!!
                            )
                            Log.d("UpdateUserInfoViewModel", response)
                        } catch(e: Exception) {
                          Log.e("UpdateUserInfoViewModel", "Failed to update bio", e)
                        }
                    }
                } else {
                    Log.e("UpdateUserInfoViewModel", task.exception.toString())
                }
            }
        }

        if(updates.isNotEmpty()){
            userRef?.update(updates)
                ?.addOnSuccessListener {
                    //update ui
                }
                ?.addOnFailureListener{
                    //update ui
                }
        }
    }
}