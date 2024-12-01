package com.example.vociapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vociapp.data.types.Request
import com.example.vociapp.ui.viewmodels.AuthViewModel

@Composable
fun AddRequestDialog(
    onDismiss: () -> Unit,
    onAdd: (Request) -> Unit,
    authViewModel: AuthViewModel,
) {
    var requestTitle by remember { mutableStateOf("") }
    var requestDescription by remember { mutableStateOf("") }
    var homelessID by remember { mutableStateOf("") }
    var isAddingRequest by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Aggiungi Richiesta") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = requestTitle,
                    onValueChange = { requestTitle = it },
                    label = { Text("Titolo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = requestDescription,
                    onValueChange = { requestDescription = it },
                    label = { Text("Descrizione") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = homelessID,
                    onValueChange = { homelessID = it },
                    label = { Text("Senzatetto") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                SearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    onSearch = { /* TODO() Handle search query */ },
                    placeholderText = "Cerca un senzatetto...",
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
                )

            }},
        confirmButton = {
            Button(
                onClick = {
                    isAddingRequest = true
                    val newRequest = Request(
                        title = requestTitle,
                        description = requestDescription,
                        homelessID = requestDescription,
                        creatorId = authViewModel.getCurrentUserProfile()?.displayName ?: "User",
                    )
                    onAdd(newRequest)
                },
                enabled =
                !isAddingRequest and
                        requestTitle.isNotEmpty() and
                        requestDescription.isNotEmpty(),
            ) {
                Text("Aggiungi")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {onDismiss()},
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                Text("Annulla")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        textContentColor = MaterialTheme.colorScheme.onBackground,
    )
}