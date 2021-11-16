package com.example.hw6_navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hw6_navigation.R
import com.example.hw6_navigation.databinding.FragmentDictionaryBinding

class DictionaryFragment : Fragment() {
    override fun onCreateView(
        inflater:LayoutInflater,
        container:ViewGroup?,
        savedInstanceState:Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_dictionary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDictionaryBinding.bind(view)

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_dictionary_to_new)
        }
    }
}