package com.rastete.todoapp.presentation.features.add_update_todo

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
class AddUpdateTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage
        get() = _errorMessage

    fun updateTodo(todoId: Int, title: String, priority: String, description: String): Boolean {
        if (title.isEmpty() || description.isEmpty()) {
            _errorMessage.value = "Fill out title and description"
            return false
        }
        viewModelScope.launch {
            todoRepository.updateTodo(
                TodoEntity(
                    id = todoId,
                    priority = getPriority(priority),
                    title = title,
                    description = description
                )
            )
        }
        return true
    }

}