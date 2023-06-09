package com.akaiz.noteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akaiz.noteapp.feature_note.domain.model.Note
import com.akaiz.noteapp.feature_note.domain.use_case.NoteUseCase
import com.akaiz.noteapp.feature_note.domain.util.NoteOrder
import com.akaiz.noteapp.feature_note.domain.util.NotesEvent
import com.akaiz.noteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val useCase: NoteUseCase
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeleteNote: Note? = null

    private var getNoteJob: Job? = null

    init {
        getNotes(noteOrder = NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    useCase.deleteNote(note = event.note)
                    recentlyDeleteNote = event.note
                }
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    useCase.addNote(note = recentlyDeleteNote ?: return@launch)
                    recentlyDeleteNote = null
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNoteJob?.cancel()
        getNoteJob = useCase.getNotes(noteOrder = noteOrder)
            .onEach { note ->
                _state.value = state.value.copy(
                    notes = note,
                    noteOrder = noteOrder
                )
            }.launchIn(viewModelScope)
    }
}