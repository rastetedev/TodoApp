package com.rastete.todoapp.data.repository

import com.rastete.todoapp.data.dao.TodoDao
import com.rastete.todoapp.data.entity.TodoEntity
import javax.inject.Inject

class TodoRepository @Inject constructor(private val todoDao: TodoDao) {

    /*fun getAllTodos(query: String, priority: Priority) =
        //todoDao.getAllTodos(query = query, sortBy = priority)
        todoDao.getAllTodos()*/
    fun getAllTodos() = todoDao.getAllTodos()

    suspend fun insertTodo(todoEntity: TodoEntity) = todoDao.insertTodo(todoEntity)

    suspend fun updateTodo(todoEntity: TodoEntity) = todoDao.updateTodo(todoEntity)

    suspend fun deleteTodo(todoId: Int) = todoDao.deleteTodo(todoId)

    suspend fun deleteAllTodos() = todoDao.deleteAllTodos()
}