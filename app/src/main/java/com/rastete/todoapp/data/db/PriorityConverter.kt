package com.rastete.todoapp.data.db

import androidx.room.TypeConverter
import com.rastete.todoapp.data.Priority


class PriorityConverter {

    @TypeConverter
    fun toString(priority: Priority): String {
        return priority.toString()
    }

    @TypeConverter
    fun fromString(priorityName: String): Priority {
        return Priority.valueOf(priorityName)
    }

}