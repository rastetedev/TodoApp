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

    @Query(
        """
        SELECT * FROM TodoEntity
        ORDER BY 
        CASE
            WHEN priority LIKE 'H%' THEN 1
            WHEN priority LIKE 'M%' THEN 2
            WHEN priority LIKE 'L%' THEN 3
        END
        """
    )
    suspend fun sortByHighPriority() : List<TodoEntity>

    @Query(
        """
        SELECT * FROM TodoEntity
        ORDER BY 
        CASE
            WHEN priority LIKE 'H%' THEN 3
            WHEN priority LIKE 'M%' THEN 2
            WHEN priority LIKE 'L%' THEN 1
        END
        """
    )
    suspend fun sortByLowPriority() : List<TodoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todoEntity: TodoEntity)

    @Update
    suspend fun updateTodo(todoEntity: TodoEntity)

    @Query("DELETE FROM TodoEntity WHERE id = :todoId")
    suspend fun deleteTodo(todoId: Int)

    @Query("DELETE FROM TodoEntity")
    suspend fun deleteAllTodos()
}