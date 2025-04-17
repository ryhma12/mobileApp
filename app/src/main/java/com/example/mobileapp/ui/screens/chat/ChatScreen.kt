package com.example.mobileapp.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapp.helpers.UserHelper
import com.example.mobileapp.model.Message

@Composable
fun ChatScreen(
    viewModel: ChatViewModel
) {
    var inputMessage by remember { mutableStateOf(TextFieldValue("")) }
    val chat by viewModel.chat.collectAsState()

    val currentUid = UserHelper().getCurrentUid()

    val listState: LazyListState = rememberLazyListState()
    LaunchedEffect(chat.messages.lastIndex) {
        if(chat.messages.isNotEmpty()){
            listState.scrollToItem(chat.messages.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA))
            .padding(8.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(chat.messages) { message ->
                val isUser = message.senderId == currentUid
                ChatBubble(message = message, isUser = isUser)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = inputMessage,
                onValueChange = { inputMessage = it },
                modifier = Modifier.weight(1f).background(Color.LightGray).padding(8.dp)
            )
            Button(onClick = {
                if (inputMessage.text.isNotBlank()) {
                    viewModel.sendMessage(text = inputMessage.text, senderId = currentUid)
                    inputMessage = TextFieldValue("")
                }
            }) {
                Text("Send")
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message, isUser: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
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
                text = message.content,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(ChatViewModel(chatId = "123"))
}