package com.rastete.todoapp.presentation.features

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastete.todoapp.data.entity.TodoEntity
import com.rastete.todoapp.data.repository.TodoRepository
import com.rastete.todoapp.presentation.utils.getPriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage
        get() = _errorMessage

    fun deleteTodo(todoId: Int) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todoId)
        }
    }

    fun addTodo(todoEntity: TodoEntity) {
        viewModelScope.launch { todoRepository.insertTodo(todoEntity) }
    }

    fun addTodo(title: String, priority: String, description: String) {
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
}