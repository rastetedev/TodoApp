package com.rastete.todoapp.data

enum class Priority(val position: Int, val order: Int) {
    HIGH(0, 1),
    MEDIUM(1, 1),
    LOW(2, 3)
}