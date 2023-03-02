package com.rastete.todoapp.presentation.features.add_update_todo

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
import com.rastete.todoapp.databinding.FragmentAddUpdateTodoBinding
import com.rastete.todoapp.presentation.features.CommonTodoViewModel
import com.rastete.todoapp.presentation.utils.createDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUpdateTodoFragment : Fragment() {

    private var _binding: FragmentAddUpdateTodoBinding? = null
    private val binding get() = _binding!!

    private val args: AddUpdateTodoFragmentArgs by navArgs()
    private val todo by lazy {
        args.todo
    }

    private val viewModel: AddUpdateTodoViewModel by viewModels()
    private val commonViewModel: CommonTodoViewModel by viewModels()

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

        _binding = FragmentAddUpdateTodoBinding.inflate(inflater, container, false)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(getMenu(todo != null), viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding.spnTodoPriorityAddUpdateTodoF.onItemSelectedListener = spinnerListener
        return binding.root
    }

    private fun getMenu(isNotNewTodo: Boolean): MenuProvider {
        return if (isNotNewTodo) getUpdateMenu() else getAddMenu()
    }

    private fun getAddMenu(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_add_todo, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_todo -> {
                        with(binding) {
                            if (commonViewModel.addTodo(
                                    etTodoTitleAddUpdateTodoF.text.toString(),
                                    spnTodoPriorityAddUpdateTodoF.selectedItem.toString(),
                                    etTodoDescriptionAddUpdateTodoF.text.toString()
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.create_todo_successful),
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().popBackStack()
                            }
                        }

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
                menuInflater.inflate(R.menu.menu_update_todo, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_update_todo -> {
                        with(binding) {
                            if (viewModel.updateTodo(
                                    todoId = todo?.id ?: 0,
                                    title = etTodoTitleAddUpdateTodoF.text.toString(),
                                    description = etTodoDescriptionAddUpdateTodoF.text.toString(),
                                    priority = spnTodoPriorityAddUpdateTodoF.selectedItem.toString()
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.update_todo_successful),
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().popBackStack()
                            }
                        }
                        true
                    }
                    R.id.action_delete_todo -> {
                        createDialog(
                            context,
                            title = getString(R.string.delete_todo),
                            description = getString(R.string.delete_todo_message),
                            onPositiveButtonClicked = {
                                commonViewModel.deleteTodo(todo?.id ?: 0)
                                Toast.makeText(
                                    context,
                                    getString(R.string.delete_todo_successful),
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
            activity?.title = getString(R.string.update_todo)
            binding.etTodoTitleAddUpdateTodoF.setText(it.title)
            binding.spnTodoPriorityAddUpdateTodoF.setSelection(it.priority.position)
            binding.etTodoDescriptionAddUpdateTodoF.setText(it.description)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            showMessage(it)
        }

        commonViewModel.errorMessage.observe(viewLifecycleOwner) {
            showMessage(it)
        }
    }

    private fun showMessage(message: String?) {
        if (!message.isNullOrEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}