package com.rastete.todoapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rastete.todoapp.data.Priority

@Entity()
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val priority: Priority,
    val name: String,
    val description: String
)