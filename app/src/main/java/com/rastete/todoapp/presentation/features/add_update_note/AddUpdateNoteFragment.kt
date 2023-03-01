package com.rastete.todoapp.presentation.features.add_update_note

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rastete.todoapp.R
import com.rastete.todoapp.databinding.FragmentAddUpdateNoteBinding
import com.rastete.todoapp.presentation.utils.createDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUpdateNoteFragment : Fragment() {

    private var _binding: FragmentAddUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val args: AddUpdateNoteFragmentArgs by navArgs()
    private val todo by lazy {
        args.todo
    }

    private val viewModel: AddUpdateNoteViewModel by viewModels()

    private val spinnerListener: AdapterView.OnItemSelectedListener by lazy {
        object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val color = when (position) {
                    0 -> R.color.red
                    1 -> R.color.yellow
                    2 -> R.color.green
                    else -> R.color.red
                }
                (parent?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(requireContext(), color)
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddUpdateNoteBinding.inflate(inflater, container, false)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(getMenu(todo != null), viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding.spnNotePriorityAddUpdateNoteF.onItemSelectedListener = spinnerListener
        return binding.root
    }

    private fun getMenu(isNotNewNote: Boolean): MenuProvider {
        return if (isNotNewNote) getUpdateMenu() else getAddMenu()
    }

    private fun getAddMenu(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_add_note, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_note -> {
                        viewModel.addNote(
                            binding.etNoteTitleAddUpdateNoteF.text.toString(),
                            binding.spnNotePriorityAddUpdateNoteF.selectedItem.toString(),
                            binding.etNoteDescriptionAddUpdateNoteF.text.toString()
                        )
                        Toast.makeText(
                            context,
                            getString(R.string.create_note_successful),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                        true
                    }
                    else -> true
                }
            }
        }
    }

    private fun getUpdateMenu(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_update_note, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_update_note -> {
                        viewModel.updateNote(
                            todoId = todo?.id ?: 0,
                            title = binding.etNoteTitleAddUpdateNoteF.text.toString(),
                            description = binding.etNoteDescriptionAddUpdateNoteF.text.toString(),
                            priority = binding.spnNotePriorityAddUpdateNoteF.selectedItem.toString()
                        )
                        Toast.makeText(
                            context,
                            getString(R.string.update_note_successful),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                        true
                    }
                    R.id.action_delete_note -> {
                        createDialog(
                            context,
                            title = getString(R.string.delete_note),
                            description = getString(R.string.delete_note_message),
                            onPositiveButtonClicked = {
                                viewModel.deleteNote(todo?.id ?: 0)
                                Toast.makeText(
                                    context,
                                    getString(R.string.delete_note_successful),
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().popBackStack()
                            },
                            onNegativeButtonClicked = {}
                        )
                        true
                    }
                    else -> true
                }
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todo?.let {
            activity?.title = getString(R.string.update_note)
            binding.etNoteDescriptionAddUpdateNoteF.setText(it.description)
            binding.etNoteTitleAddUpdateNoteF.setText(it.title)
            binding.spnNotePriorityAddUpdateNoteF.setSelection(it.priority.position)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}