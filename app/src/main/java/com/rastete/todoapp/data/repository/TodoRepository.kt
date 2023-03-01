package com.rastete.todoapp.data.repository

import com.rastete.todoapp.data.dao.TodoDao
import com.rastete.todoapp.data.entity.TodoEntity
import javax.inject.Inject

class TodoRepository @Inject constructor(private val todoDao: TodoDao) {

    fun getAllTodos() = todoDao.getAllTodos()

    suspend fun searchTodos(query: String) = todoDao.searchTodos(query)

    suspend fun insertTodo(todoEntity: TodoEntity) = todoDao.insertTodo(todoEntity)

    suspend fun updateTodo(todoEntity: TodoEntity) = todoDao.updateTodo(todoEntity)

    suspend fun deleteTodo(todoId: Int) = todoDao.deleteTodo(todoId)

    suspend fun deleteAllTodos() = todoDao.deleteAllTodos()

    suspend fun sortByHighPriority() = todoDao.sortByHighPriority()

    suspend fun sortByLowPriority() = todoDao.sortByLowPriority()
}