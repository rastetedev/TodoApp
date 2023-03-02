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

    fun getTodoList(lifecycleOwner: LifecycleOwner, query: String) {
        jobSearch?.cancel()
        jobSearch = viewModelScope.launch {
            todoRepository.getAllTodos(query).observe(lifecycleOwner) { list ->
                noteList = list
                _todos.postValue(noteList.filter {
                    it.title.contains(query).or(it.description.contains(query))
                })
            }
        }

    }

    private fun sortByHighPriority() {
        viewModelScope.launch {
            _todos.postValue(todoRepository.sortByHighPriority())
        }
    }

    private fun sortByLowPriority() {
        viewModelScope.launch {
            _todos.postValue(todoRepository.sortByLowPriority())
        }
    }

    fun deleteAllTodos() {
        viewModelScope.launch {
            todoRepository.deleteAllTodos()
        }
    }

    fun setSortByCriteria(priority: Priority) {
        if (priority == Priority.HIGH) sortByHighPriority()
        else if (priority == Priority.LOW) sortByLowPriority()
    }
}