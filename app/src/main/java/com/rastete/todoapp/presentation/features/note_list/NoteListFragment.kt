package com.rastete.todoapp.presentation.features.note_list

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
import com.rastete.todoapp.databinding.FragmentNoteListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private lateinit var actionSortHighPriorityItem: MenuItem
    private lateinit var actionSortLowPriorityItem: MenuItem

    private val viewModel: NoteListViewModel by viewModels()

    private val adapter: NoteListAdapter by lazy {
        NoteListAdapter { todo ->
            findNavController().navigate(
                NoteListFragmentDirections
                    .actionNoteListFragmentToAddUpdateNoteFragment(todo)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        binding.rvTodoListNoteListF.adapter = adapter
        binding.rvTodoListNoteListF.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        setupMenu(menuHost)

        binding.fabAddNoteNoteListF.setOnClickListener {
            findNavController().navigate(
                NoteListFragmentDirections
                    .actionNoteListFragmentToAddUpdateNoteFragment()
            )
        }

        viewModel.todoList.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isNotEmpty()) {
                    binding.llEmptyDataNoteListF.visibility = GONE
                } else {
                    binding.llEmptyDataNoteListF.visibility = VISIBLE
                }

                adapter.setList(it)
            }
        }
        viewModel.getTodoList()
    }

    private fun setupMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_note_list, menu)
                actionSortHighPriorityItem = menu.findItem(R.id.action_sort_notes_by_high_priority)
                actionSortLowPriorityItem = menu.findItem(R.id.action_sort_notes_by_low_priority)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete_all_notes -> {
                        true
                    }
                    R.id.action_sort_notes_by_high_priority -> {
                        menuItem.isChecked = true
                        actionSortLowPriorityItem.isChecked = false
                        true
                    }
                    R.id.action_sort_notes_by_low_priority -> {
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