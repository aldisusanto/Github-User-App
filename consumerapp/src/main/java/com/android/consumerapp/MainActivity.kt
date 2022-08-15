package com.android.consumerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.consumerapp.adapter.UsersAdapter
import com.android.consumerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Consumer App"

        setupUI()
    }

    private fun setupUI() {
        adapter = UsersAdapter()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.apply {
            listUser.setHasFixedSize(true)
            listUser.layoutManager = LinearLayoutManager(this@MainActivity)
            listUser.adapter = adapter

        }

        viewModel.setFavoriteUsers(this)
        viewModel.getFavoriteUsers().observe(this, {
            if (it != null) {
                adapter.setList(it)
                binding.apply {
                    listUser.visibility = View.VISIBLE
                    bgIllustrationImageFavorite.visibility = View.GONE
                    bgIllustrationTextFavorite.visibility = View.GONE

                }
            }
            if (it.isEmpty()) {
                binding.apply {
                    listUser.visibility = View.GONE
                    bgIllustrationImageFavorite.visibility = View.VISIBLE
                    bgIllustrationTextFavorite.visibility = View.VISIBLE
                }

            }

        })
    }

}