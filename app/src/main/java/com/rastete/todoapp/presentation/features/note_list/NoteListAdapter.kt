package com.rastete.todoapp.presentation.features.note_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rastete.todoapp.R
import com.rastete.todoapp.data.Priority
import com.rastete.todoapp.data.entity.TodoEntity
import com.rastete.todoapp.databinding.ItemTodoBinding

class NoteListAdapter : RecyclerView.Adapter<NoteListHolder>() {

    private var noteList = emptyList<TodoEntity>()

    fun setList(list: List<TodoEntity>) {
        noteList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListHolder {
        val itemBinding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteListHolder(itemBinding)
    }

    override fun getItemCount(): Int = noteList.size

    override fun onBindViewHolder(holder: NoteListHolder, position: Int) {
        holder.bind(noteList[position])
    }
}

class NoteListHolder(private val binding: ItemTodoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(todo: TodoEntity) {
        with(binding) {
            tvTitleTodoI.text = todo.title
            tvDescriptionTodoI.text = todo.description
            cvPriorityIndicatorTodoI.setCardBackgroundColor(
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
