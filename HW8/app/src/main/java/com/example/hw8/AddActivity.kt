package com.example.hw8

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hw8.databinding.AddActivityBinding



class AddActivity : AppCompatActivity() {
    private lateinit var binding: AddActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun addClickEvent(view: View) {
        if (view is Button) {
            if (binding.newId.text.toString() == "" || binding.newTitle.text.toString() == ""){
                Toast.makeText(this@AddActivity, "need more information", Toast.LENGTH_SHORT).show()
                return
            }

            MyApp.instance.post = Post(
                binding.newId.text.toString().toInt(),
                binding.newTitle.text.toString(),
                binding.newBody.text.toString(),
                0
            )

            val intent = Intent(this@AddActivity, MainActivity::class.java)
            intent.putExtra("added", true)
            startActivity(intent)
        }
    }

    fun backClickEvent(view: View) {
        val intent = Intent(this@AddActivity, MainActivity::class.java)
        startActivity(intent)
    }
}