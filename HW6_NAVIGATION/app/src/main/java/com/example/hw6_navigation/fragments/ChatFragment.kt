package com.example.hw6_navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hw6_navigation.R
import androidx.navigation.fragment.findNavController
import com.example.hw6_navigation.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    override fun onCreateView(
        inflater:LayoutInflater,
        container:ViewGroup?,
        savedInstanceState:Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentChatBinding.bind(view)
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_chat_to_new)
        }
    }
}