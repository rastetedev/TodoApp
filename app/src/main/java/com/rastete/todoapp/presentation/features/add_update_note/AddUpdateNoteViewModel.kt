package com.rastete.todoapp.presentation.features.add_update_note

import androidx.lifecycle.MutableLiveData
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

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage
        get() = _errorMessage

    fun addNote(title: String, priority: String, description: String) {
        viewModelScope.launch {

            if (title.isEmpty() or description.isEmpty()) {
                _errorMessage.value = "Fill out title and description"
                return@launch
            }

            todoRepository.insertTodo(
                TodoEntity(
                    priority = getPriority(priority),
                    title = title,
                    description = description
                )
            )

        }
    }

    fun updateNote(todoId: Int, title: String, priority: String, description: String) {
        viewModelScope.launch {

            if (title.isEmpty() or description.isEmpty()) {
                _errorMessage.value = "Fill out title and description"
                return@launch
            }

            todoRepository.updateTodo(
                TodoEntity(
                    id = todoId,
                    priority = getPriority(priority),
                    title = title,
                    description = description
                )
            )
        }
    }

    fun deleteNote(todoId: Int) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todoId)
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