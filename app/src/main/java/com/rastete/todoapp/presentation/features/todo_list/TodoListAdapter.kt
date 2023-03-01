package com.rastete.todoapp.presentation.features.todo_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rastete.todoapp.R
import com.rastete.todoapp.data.Priority
import com.rastete.todoapp.data.entity.TodoEntity
import com.rastete.todoapp.databinding.ItemTodoBinding
import com.rastete.todoapp.presentation.utils.TodoDiffUtil

class TodoListAdapter(private val clickListener: (TodoEntity) -> Unit) :
    RecyclerView.Adapter<TodoListHolder>() {

    var todoList = emptyList<TodoEntity>()
        private set

    fun setList(list: List<TodoEntity>) {
        val todoDiffUtil = TodoDiffUtil(todoList, list)
        val todoDiffResult = DiffUtil.calculateDiff(todoDiffUtil)
        this.todoList = list
        todoDiffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListHolder {
        val itemBinding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoListHolder(itemBinding)
    }

    override fun getItemCount(): Int = todoList.size

    override fun onBindViewHolder(holder: TodoListHolder, position: Int) {
        holder.bind(todoList[position], clickListener)
    }
}

class TodoListHolder(private val binding: ItemTodoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(todo: TodoEntity, clickListener: (TodoEntity) -> Unit) {
        with(binding) {
            root.setOnClickListener {
                clickListener(todo)
            }
            tvTodoTitleTodoI.text = todo.title
            tvTodoDescriptionTodoI.text = todo.description
            cvPriorityTodoTodoI.setCardBackgroundColor(
                ContextCompat.getColor(root.context, mapColor(todo))
            )
        }
    }
}

fun mapColor(todoEntity: TodoEntity) =
    when (todoEntity.priority) {
        Priority.HIGH -> R.color.red
        Priority.MEDIUM -> R.color.yellow
        Priority.LOW -> R.color.green
    }
