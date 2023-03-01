package com.rastete.todoapp.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.rastete.todoapp.data.entity.TodoEntity

class TodoDiffUtil(
    private val oldList: List<TodoEntity>, private val newList: List<TodoEntity>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size


    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id &&
                oldList[oldItemPosition].title == newList[newItemPosition].title &&
                oldList[oldItemPosition].description == newList[newItemPosition].description &&
                oldList[oldItemPosition].priority == newList[newItemPosition].priority

}