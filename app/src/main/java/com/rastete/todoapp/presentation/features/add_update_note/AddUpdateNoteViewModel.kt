package com.rastete.todoapp.presentation.features.add_update_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastete.todoapp.data.Priority
import com.rastete.todoapp.data.entity.TodoEntity
import com.rastete.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUpdateNoteViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {


    fun addNote(title: String, priority: String, description: String) {
        viewModelScope.launch {
            todoRepository.insertTodo(
                TodoEntity(
                    priority = getPriority(priority),
                    title = title,
                    description = description
                )
            )
        }
    }

    private fun getPriority(priority: String): Priority =
        when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.HIGH
        }
}