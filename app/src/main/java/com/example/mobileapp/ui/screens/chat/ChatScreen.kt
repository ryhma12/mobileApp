package com.example.mobileapp.ui.screens.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// Firebase importit:
// import com.google.firebase.database.DatabaseReference
// import com.google.firebase.database.FirebaseDatabase
// import com.google.firebase.database.DataSnapshot
// import com.google.firebase.database.DatabaseError
// import com.google.firebase.database.ChildEventListener

@Composable
fun ChatScreen(
    contactId: Int
) {
    Log.d("ChatScreen", "ContactId: $contactId")
    var message by remember { mutableStateOf(TextFieldValue("")) }
    val messages = remember { mutableStateListOf<String>() }

    // Firebaseen liittyvä:
    // val databaseReference = FirebaseDatabase.getInstance().getReference("chat_messages")

    // Real-time updates:
    // LaunchedEffect(Unit) {
    //     databaseReference.addChildEventListener(object : ChildEventListener {
    //         override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
    //             val newMessage = snapshot.getValue(String::class.java)
    //             newMessage?.let { messages.add(it) }
    //
    //         override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
    //         override fun onChildRemoved(snapshot: DataSnapshot) {}
    //         override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
    //         override fun onCancelled(error: DatabaseError) {}
    //     })
    // }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Viestien näyttäminen:
            messages.forEach { msg ->
                ChatBubble(message = msg, isUser = messages.indexOf(msg) % 2 == 0)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f).background(Color.LightGray).padding(8.dp)
            )
            Button(onClick = {
                if (message.text.isNotBlank()) {
                    // Viestien lähettäminen
                    messages.add(message.text) // Viestit UI:ssa
                    message = TextFieldValue("") // Tekstikentän tyhjentäminen

                    // Viestien tallentaminen Firebasessa:
                    // val newMessageRef = databaseReference.push()
                    // newMessageRef.setValue(message.text)
                }
            }) {
                Text("Send")
            }
        }
    }
}

@Composable
fun ChatBubble(message: String, isUser: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isUser) Color(0xFF4CAF50) else Color(0xFF9E9E9E),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(contactId = 1)
}