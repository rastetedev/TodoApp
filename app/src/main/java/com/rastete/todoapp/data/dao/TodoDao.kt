package com.rastete.todoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rastete.todoapp.data.entity.TodoEntity

@Dao
interface TodoDao {

/*    @Query(
        """
        SELECT * FROM TodoEntity 
        WHERE title LIKE '%' || :query || '%' OR
        description LIKE '%' || :query || '%' 
        ORDER BY id ASC 
        """
    )
    //fun getAllTodos(query: String, sortBy: Priority = Priority.HIGH): LiveData<List<TodoEntity>>
    fun getAllTodos(query: String): LiveData<List<TodoEntity>>*/

    @Query("SELECT * FROM TodoEntity")
    fun getAllTodos(): LiveData<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todoEntity: TodoEntity)

    @Update
    suspend fun updateTodo(todoEntity: TodoEntity)

    @Query("DELETE FROM TodoEntity WHERE id = :todoId")
    suspend fun deleteTodo(todoId: Int)

    @Query("DELETE FROM TodoEntity")
    suspend fun deleteAllTodos()
}