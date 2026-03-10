package com.basitbhatti.notesapp.supabase.ui.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.basitbhatti.notesapp.supabase.data.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(modifier: Modifier = Modifier) {

    val viewModel: NotesViewModel = viewModel()

    val notes by viewModel.notes.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    var title by remember {
        mutableStateOf("")
    }

    var content by remember {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text("Supabase Notes App")
        })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                showDialog = true
            }) {
            Icon(
                Icons.Default.Add, "Add"
            )
        }
    }

    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues
        ) {
            items(notes) { note ->
                NoteCard(note) {
                }
            }
        }
    }

    if (showDialog){
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = { Text("New Note")},
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        placeholder = {
                            Text("Title")
                        }
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = content,
                        onValueChange = {
                            content = it
                        },
                        placeholder = {
                            Text("Content")
                        }
                    )

                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addNote(title, content)
                    showDialog = false
                    title = ""
                    content = ""
                }) {
                    Text("Save")
                }
            }
        )
    }

}

@Composable
fun NoteCard(note: Note, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(note.title, style = MaterialTheme.typography.titleMedium)
                Text(note.content, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}