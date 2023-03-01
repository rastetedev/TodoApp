package com.rastete.todoapp.presentation.features.todo_list

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rastete.todoapp.R
import com.rastete.todoapp.databinding.FragmentTodoListBinding
import com.rastete.todoapp.presentation.utils.createDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var actionSortHighPriorityItem: MenuItem
    private lateinit var actionSortLowPriorityItem: MenuItem

    private val viewModel: TodoListViewModel by viewModels()

    private val todoAdapter: TodoListAdapter by lazy {
        TodoListAdapter { todo ->
            findNavController().navigate(
                TodoListFragmentDirections
                    .actionTodoListFragmentToAddUpdateTodoFragment(todo)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTodoListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        setupMenu(menuHost)

        binding.rvTodoListTodoListF.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todoAdapter
        }

        binding.fabAddTodoTodoListF.setOnClickListener {
            findNavController().navigate(
                TodoListFragmentDirections
                    .actionTodoListFragmentToAddUpdateTodoFragment()
            )
        }

        viewModel.getTodoList().observe(viewLifecycleOwner) {
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

    private fun setupMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_todo_list, menu)
                actionSortHighPriorityItem = menu.findItem(R.id.action_sort_todos_by_high_priority)
                actionSortLowPriorityItem = menu.findItem(R.id.action_sort_todos_by_low_priority)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete_all_todos -> {
                        createDialog(
                            context,
                            title = getString(R.string.delete_all_todos),
                            description = getString(R.string.delete_all_todo_message),
                            onPositiveButtonClicked = {
                                viewModel.deleteAllTodos()
                            },
                            onNegativeButtonClicked = {}
                        )
                        true
                    }
                    R.id.action_sort_todos_by_high_priority -> {
                        menuItem.isChecked = true
                        actionSortLowPriorityItem.isChecked = false
                        true
                    }
                    R.id.action_sort_todos_by_low_priority -> {
                        menuItem.isChecked = true
                        actionSortHighPriorityItem.isChecked = false

                        true
                    }

                    else -> true
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}