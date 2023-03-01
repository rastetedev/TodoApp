package com.rastete.todoapp.presentation.utils

import com.rastete.todoapp.data.Priority

fun getPriority(priority: String): Priority =
        when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.HIGH
        }