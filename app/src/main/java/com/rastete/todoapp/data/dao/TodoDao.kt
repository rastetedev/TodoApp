package com.rastete.todoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rastete.todoapp.data.entity.TodoEntity

@Dao
interface TodoDao {

    @Query("SELECT * FROM TodoEntity")
    fun getAllTodos(): LiveData<List<TodoEntity>>

    @Query(
        """
        SELECT * FROM TodoEntity 
        WHERE title LIKE '%' || :query || '%' OR
        description LIKE '%' || :query || '%' 
        """
    )
    suspend fun searchTodos(query: String): List<TodoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todoEntity: TodoEntity)

    @Update
    suspend fun updateTodo(todoEntity: TodoEntity)

    @Query("DELETE FROM TodoEntity WHERE id = :todoId")
    suspend fun deleteTodo(todoId: Int)

    @Query("DELETE FROM TodoEntity")
    suspend fun deleteAllTodos()
}