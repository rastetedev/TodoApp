package com.rastete.todoapp.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rastete.todoapp.data.Priority
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val priority: Priority,
    val title: String,
    val description: String
) : Parcelable