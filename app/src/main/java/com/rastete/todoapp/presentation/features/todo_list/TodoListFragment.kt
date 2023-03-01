package com.rastete.todoapp.presentation.features.todo_list

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rastete.todoapp.R
import com.rastete.todoapp.data.Priority
import com.rastete.todoapp.data.entity.TodoEntity
import com.rastete.todoapp.databinding.FragmentTodoListBinding
import com.rastete.todoapp.presentation.features.CommonTodoViewModel
import com.rastete.todoapp.presentation.utils.createDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var actionSortHighPriorityItem: MenuItem
    private lateinit var actionSortLowPriorityItem: MenuItem

    private val viewModel: TodoListViewModel by viewModels()
    private val commonTodoViewModel: CommonTodoViewModel by viewModels()

    private val todoAdapter: TodoListAdapter by lazy {
        TodoListAdapter { todo ->
            findNavController().navigate(
                TodoListFragmentDirections.actionTodoListFragmentToAddUpdateTodoFragment(todo)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        setupMenu(menuHost)
        setupRecyclerView()
        setupEvents()
        setupListeners()
        viewModel.getTodoList(lifecycleOwner = this)
    }

    private fun setupMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_todo_list, menu)
                actionSortHighPriorityItem = menu.findItem(R.id.action_sort_todos_by_high_priority)
                actionSortLowPriorityItem = menu.findItem(R.id.action_sort_todos_by_low_priority)
                val actionSearch = menu.findItem(R.id.action_search_todos)
                val searchView = actionSearch.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@TodoListFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete_all_todos -> {
                        createDialog(context,
                            title = getString(R.string.delete_all_todos),
                            description = getString(R.string.delete_all_todo_message),
                            onPositiveButtonClicked = {
                                viewModel.deleteAllTodos()
                            },
                            onNegativeButtonClicked = {})
                        true
                    }
                    R.id.action_sort_todos_by_high_priority -> {
                        setSortPriority(menuItem, Priority.HIGH)
                        true
                    }
                    R.id.action_sort_todos_by_low_priority -> {
                        setSortPriority(menuItem, Priority.LOW)
                        true
                    }

                    else -> true
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setSortPriority(menuItem: MenuItem, priority: Priority) {
        if (menuItem.isChecked) {
            menuItem.isChecked = false
            viewModel.setSortByCriteria(null)
        } else {
            menuItem.isChecked = true
            viewModel.setSortByCriteria(priority)
        }
        if(priority == Priority.HIGH) actionSortLowPriorityItem.isChecked = false
        if(priority == Priority.LOW) actionSortHighPriorityItem.isChecked = false
    }


    private fun setupListeners() {
        viewModel.todos.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isNotEmpty()) {
                    binding.llEmptyDataTodoListF.visibility = GONE
                } else {
                    binding.llEmptyDataTodoListF.visibility = VISIBLE
                }
                todoAdapter.setList(it)
            }
        }
    }

    private fun setupEvents() {
        binding.fabAddTodoTodoListF.setOnClickListener {
            findNavController().navigate(
                TodoListFragmentDirections.actionTodoListFragmentToAddUpdateTodoFragment()
            )
        }
    }

    private fun setupRecyclerView() {
        binding.rvTodoListTodoListF.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todoAdapter
        }
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemDeleted = todoAdapter.todoList[viewHolder.adapterPosition]
                commonTodoViewModel.deleteTodo(itemDeleted.id)
                todoAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                showSnackBar(viewHolder.itemView, itemDeleted, viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.rvTodoListTodoListF)
    }

    private fun showSnackBar(view: View, todoDeleted: TodoEntity, position: Int) {
        Snackbar.make(view, getString(R.string.delete_todo_successful), Snackbar.LENGTH_LONG)
            .apply {
                setAction(getString(R.string.undo)) {
                    commonTodoViewModel.addTodo(todoDeleted)
                    todoAdapter.notifyItemChanged(position)
                }.show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.searchTodos(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            if (it.isEmpty()) {
                viewModel.getTodoList(lifecycleOwner = this)
            }
            viewModel.searchTodos(it)
        }
        return true
    }

}