package com.ascendcorp.androidtechpoc.screen.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ascendcorp.androidtechpoc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ActivityMainBinding.inflate(layoutInflater)
                .apply { binding = this }
                .root
        )
        setupView()
    }

    private fun setupView() {
        binding.rvTopic.apply {
            adapter = TopicAdapter().apply { items = getTopics() }
            layoutManager = LinearLayoutManager(context)
        }
    }
}
