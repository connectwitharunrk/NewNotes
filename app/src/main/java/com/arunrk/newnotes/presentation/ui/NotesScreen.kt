package com.arunrk.newnotes.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arunrk.newnotes.presentation.ui.components.NoteDialog
import com.arunrk.newnotes.presentation.ui.components.NotesList
import com.arunrk.newnotes.presentation.viewmodel.NoteIntent
import com.arunrk.newnotes.presentation.viewmodel.NoteViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: NoteViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Notes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.processIntent(NoteIntent.SelectNote(null))
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.error != null) {
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                NotesList(
                    notes = state.notes,
                    onNoteClick = { note ->
                        viewModel.processIntent(NoteIntent.SelectNote(note))
                        showDialog = true
                    },
                    onDeleteClick = { note ->
                        viewModel.processIntent(NoteIntent.DeleteNote(note))
                    }
                )
            }
        }

        if (showDialog) {
            NoteDialog(
                note = state.selectedNote,
                onDismiss = { showDialog = false },
                onSave = { title, description ->
                    if (state.selectedNote == null) {
                        viewModel.processIntent(NoteIntent.AddNote(title, description))
                    } else {
                        viewModel.processIntent(
                            NoteIntent.UpdateNote(
                                state.selectedNote!!.copy(
                                    title = title,
                                    description = description
                                )
                            )
                        )
                    }
                    showDialog = false
                }
            )
        }
    }
}