package com.rastete.todoapp.presentation.features.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastete.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    fun getTodoList() = todoRepository.getAllTodos()

    fun deleteAllTodos() {
        viewModelScope.launch {
            todoRepository.deleteAllTodos()
        }
    }
}