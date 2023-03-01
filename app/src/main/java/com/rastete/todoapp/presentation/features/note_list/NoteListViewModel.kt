package com.rastete.todoapp.presentation.features.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastete.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    fun getTodoList() = todoRepository.getAllTodos()

    fun deleteAllNotes() {
        viewModelScope.launch {
            todoRepository.deleteAllTodos()
        }
    }
}