package com.rastete.todoapp.presentation.features.note_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rastete.todoapp.data.entity.TodoEntity
import com.rastete.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _todoList = MutableLiveData<List<TodoEntity>>()

    val todoList: LiveData<List<TodoEntity>>
        get() = _todoList


     fun getTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            _todoList.postValue(todoRepository.getAllTodos())
        }
    }
}