package com.rastete.todoapp.presentation.features.add_update_note

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.rastete.todoapp.R
import com.rastete.todoapp.databinding.FragmentAddUpdateNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class AddUpdateNoteFragment : Fragment() {

    private var _binding: FragmentAddUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddUpdateNoteViewModel by viewModels()

    val spinnerListener: AdapterView.OnItemSelectedListener by lazy {
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
        menuHost.addMenuProvider(getMenu(true), viewLifecycleOwner, Lifecycle.State.RESUMED)
        binding.spnNotePriorityAddUpdateNoteF.onItemSelectedListener = spinnerListener
        return binding.root
    }

    private fun getMenu(isNewNote: Boolean): MenuProvider {
        return if (Random.nextBoolean()) getAddMenu() else getUpdateMenu()
    }

    private fun getAddMenu(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_add_note, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_note -> {
                        viewModel.addNote("Hola", "High Priority", "Mundo")
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
                        true
                    }
                    R.id.action_delete_note -> {
                        true
                    }
                    else -> true
                }
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}