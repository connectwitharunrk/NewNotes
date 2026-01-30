package com.arunrk.newnotes

import app.cash.turbine.test
import com.arunrk.newnotes.domain.model.Note
import com.arunrk.newnotes.domain.usecase.AddNoteUseCase
import com.arunrk.newnotes.domain.usecase.DeleteNoteUseCase
import com.arunrk.newnotes.domain.usecase.GetNotesUseCase
import com.arunrk.newnotes.domain.usecase.UpdateNoteUseCase
import com.arunrk.newnotes.presentation.viewmodel.NoteIntent
import com.arunrk.newnotes.presentation.viewmodel.NoteViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {

    private lateinit var viewModel: NoteViewModel
    private lateinit var getNotesUseCase: GetNotesUseCase
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var updateNoteUseCase: UpdateNoteUseCase
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup()  {
        Dispatchers.setMain(testDispatcher)

        getNotesUseCase = mock()
        addNoteUseCase = mock()
        updateNoteUseCase = mock()
        deleteNoteUseCase = mock()

       // whenever(getNotesUseCase()).thenReturn(flowOf(emptyList()))

        viewModel = NoteViewModel(
            getNotesUseCase,
            addNoteUseCase,
            updateNoteUseCase,
            deleteNoteUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadNotes should update state with notes`() = runTest {
        val notes = listOf(
            Note(1, "Test", "Description", System.currentTimeMillis())
        )
        whenever(getNotesUseCase()).thenReturn(flowOf(notes))

        viewModel = NoteViewModel(
            getNotesUseCase,
            addNoteUseCase,
            updateNoteUseCase,
            deleteNoteUseCase
        )

        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(notes, state.notes)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `addNote with valid data should succeed`() = runTest {
        whenever(addNoteUseCase(any())).thenReturn(Result.success(Unit))

        viewModel.processIntent(NoteIntent.AddNote("Title", "Description"))
        advanceUntilIdle()

        verify(addNoteUseCase).invoke(any())
    }

    @Test
    fun `deleteNote should call deleteNoteUseCase`() = runTest {
        val note = Note(1, "Test", "Description", System.currentTimeMillis())
        whenever(deleteNoteUseCase(note)).thenReturn(Result.success(Unit))

        viewModel.processIntent(NoteIntent.DeleteNote(note))
        advanceUntilIdle()

        verify(deleteNoteUseCase).invoke(note)
    }
}