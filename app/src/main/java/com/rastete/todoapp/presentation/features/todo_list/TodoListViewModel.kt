package com.rastete.todoapp.presentation.features.todo_list

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastete.todoapp.data.Priority
import com.rastete.todoapp.data.entity.TodoEntity
import com.rastete.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
) : ViewModel() {

    private val _todos = MutableLiveData<List<TodoEntity>>()
    val todos: LiveData<List<TodoEntity>>
        get() = _todos

    private var noteList: List<TodoEntity> = emptyList()

    var jobSearch: Job? = null

    fun getTodoList(lifecycleOwner: LifecycleOwner) {
        todoRepository.getAllTodos().observe(lifecycleOwner) { list ->
            noteList = list
            _todos.value = list
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

    fun setSortByCriteria(priority: Priority?) {
        viewModelScope.launch {
            noteList = when (priority) {
                Priority.HIGH -> {
                    noteList.sortedBy { it.priority.order }
                }
                Priority.LOW -> {
                    noteList.sortedByDescending { it.priority.order }
                }
                else -> {
                    noteList.sortedBy { it.id }
                }
            }
            _todos.postValue(noteList)
        }
    }
}