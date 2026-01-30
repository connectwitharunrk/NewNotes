package com.arunrk.newnotes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.arunrk.newnotes.domain.model.Note
import com.arunrk.newnotes.presentation.ui.components.NoteItem
import com.arunrk.newnotes.presentation.ui.components.NotesList
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyNotesList_displaysEmptyMessage() {
        composeTestRule.setContent {
            NotesList(
                notes = emptyList(),
                onNoteClick = {},
                onDeleteClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("No notes yet. Tap + to add one!")
            .assertIsDisplayed()
    }

    @Test
    fun notesList_displaysNotes() {
        val notes = listOf(
            Note(1, "Test Note", "Test Description", System.currentTimeMillis())
        )

        composeTestRule.setContent {
            NotesList(
                notes = notes,
                onNoteClick = {},
                onDeleteClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Test Note")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Test Description")
            .assertIsDisplayed()
    }

    @Test
    fun noteItem_clickTriggersCallback() {
        var clicked = false
        val note = Note(1, "Test", "Description", System.currentTimeMillis())

        composeTestRule.setContent {
            NoteItem(
                note = note,
                onClick = { clicked = true },
                onDeleteClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Test")
            .performClick()

        assert(clicked)
    }

}