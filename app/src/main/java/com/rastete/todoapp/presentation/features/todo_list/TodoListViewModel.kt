package com.rastete.todoapp.presentation.features.todo_list

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastete.todoapp.data.entity.TodoEntity
import com.rastete.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _todos = MutableLiveData<List<TodoEntity>>()
    val todos: LiveData<List<TodoEntity>>
        get() = _todos

    var jobSearch: Job? = null

    fun getTodoList(lifecycleOwner: LifecycleOwner) {
        todoRepository.getAllTodos().observe(lifecycleOwner) {
            _todos.value = it
        }
    }

    fun searchTodos(query: String) {
        jobSearch?.cancel()
        jobSearch = viewModelScope.launch {
            _todos.postValue(todoRepository.searchTodos(query))
        }
    }

    fun deleteAllTodos() {
        viewModelScope.launch {
            todoRepository.deleteAllTodos()
        }
    }
}