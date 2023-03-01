package com.rastete.todoapp.presentation.features.note_list

import androidx.lifecycle.ViewModel
import com.rastete.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {
}