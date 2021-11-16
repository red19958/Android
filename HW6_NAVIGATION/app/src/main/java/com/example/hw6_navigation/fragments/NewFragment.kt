package com.example.hw6_navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hw6_navigation.R
import com.example.hw6_navigation.databinding.FragmentNewBinding

class NewFragment : Fragment() {
    private var count : Int = 0
    private lateinit var binding: FragmentNewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_new, container, false)
        binding = FragmentNewBinding.inflate(layoutInflater)
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_new_to_new)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNewBinding.bind(view)
        count = NewFragmentArgs.fromBundle(requireArguments()).count + 1
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_new_to_new)
        }

        binding.newText.text = count.toString()
    }

}

