package com.rastete.todoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rastete.todoapp.data.Priority
import com.rastete.todoapp.data.entity.TodoEntity

@Dao
interface TodoDao {

    @Query(
        """
        SELECT * FROM TodoEntity 
        WHERE title LIKE '%' || :query || '%' OR
        description LIKE '%' || :query || '%' 
        ORDER BY id ASC 
        """
    )
    //fun getAllTodos(query: String, sortBy: Priority = Priority.HIGH): LiveData<List<TodoEntity>>
    fun getAllTodos(query: String): LiveData<List<TodoEntity>>

    @Query("SELECT * FROM TodoEntity WHERE id = :todoId")
    fun getTodo(todoId: Int): TodoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todoEntity: TodoEntity)

    @Update
    suspend fun updateTodo(todoEntity: TodoEntity)

    @Delete
    suspend fun deleteTodo(todoEntity: TodoEntity)

    @Query("DELETE FROM TodoEntity")
    fun deleteAllTodos()
}