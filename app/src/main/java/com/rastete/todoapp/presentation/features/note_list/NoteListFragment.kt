package com.rastete.todoapp.presentation.features.note_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.rastete.todoapp.R
import com.rastete.todoapp.databinding.FragmentNoteListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    private lateinit var actionSortHighPriorityItem: MenuItem
    private lateinit var actionSortLowPriorityItem: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        setupMenu(menuHost)

        binding.fabAddNoteNoteListF.setOnClickListener {
            findNavController().navigate(R.id.action_NoteListFragment_to_AddUpdateNoteFragment)
        }
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