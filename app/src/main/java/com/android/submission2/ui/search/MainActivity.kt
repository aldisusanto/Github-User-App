package com.android.submission2.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.submission2.CheckConnection
import com.android.submission2.R
import com.android.submission2.ui.adapter.UsersAdapter
import com.android.submission2.databinding.ActivityMainBinding
import com.android.submission2.model.Users
import com.android.submission2.ui.detail.DetailActivity
import com.android.submission2.ui.favorite.FavoriteUserActivity
import com.android.submission2.ui.setting.SettingActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UsersAdapter
    private lateinit var checkConnection: CheckConnection

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.setting) {
            val mIntent = Intent(this, SettingActivity::class.java)
            startActivity(mIntent)
        }

        if (item.itemId == R.id.favorite) {
            val intent = Intent(this, FavoriteUserActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "GitHub User"

        //check the internet connection first before displaying data from the API

        checkConnectionUI()

        adapter = UsersAdapter()

        adapter.setOnItemClickCallback((object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(user: Users) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    it.putExtra(DetailActivity.EXTRA_ID, user.id)
                    it.putExtra(DetailActivity.EXTRA_AVATAR, user.avatar_url)
                    it.putExtra(DetailActivity.EXTRA_TYPE, user.type)
                    startActivity(it)
                }
            }
        }))

    }

    private fun checkConnectionUI() {
        checkConnection = CheckConnection(application)

        checkConnection.observe(this, { isConnected ->
            if (isConnected) {
                binding.apply {
                    bgExploreImage.visibility = View.VISIBLE
                    bgExploreText.visibility = View.VISIBLE
                    bgInternetImage.visibility = View.GONE
                    bgInternetText.visibility = View.GONE
                    refreshActivity.setOnRefreshListener {
                        searchUser()
                    }
                }
                setupSearchUI()
                setListUsers()
                showSwipeRefreshLayout(false)

            } else {
                binding.apply {
                    bgExploreImage.visibility = View.GONE
                    bgExploreText.visibility = View.GONE
                    bgInternetImage.visibility = View.VISIBLE
                    bgInternetText.visibility = View.VISIBLE
                    listUser.visibility = View.GONE
                    labelResult.visibility = View.GONE
                    labelNotFound.visibility = View.GONE
                    barLoading(false)
                    showSwipeRefreshLayout(true)
                }
            }
        })

    }

    private fun setupSearchUI() {
        binding.apply {
            textSearch.queryHint = resources.getString(R.string.search_hint)
            textSearch.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchUser()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
            btnCardSearch.setOnClickListener {
                searchUser()
                binding.apply {
                    bgExploreImage.visibility = View.GONE
                    bgExploreText.visibility = View.GONE
                    listUser.visibility = View.VISIBLE
                }

            }

        }
    }

    private fun setListUsers() {
        binding.apply {
            listUser.layoutManager = LinearLayoutManager(this@MainActivity)
            listUser.setHasFixedSize(true)
            listUser.adapter = adapter
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        viewModel.getSearchUser().observe(this, {
            if (it != null) {
                adapter.setList(it)
                barLoading(false)
                binding.apply {
                    bgExploreImage.visibility = View.GONE
                    bgExploreText.visibility = View.GONE
                    labelNotFound.visibility = View.GONE
                    labelResult.visibility = View.VISIBLE
                    listUser.visibility = View.VISIBLE
                }
                showSwipeRefreshLayout(false)
            }

            if (it.isEmpty()) {
                binding.apply {
                    bgExploreImage.visibility = View.GONE
                    bgExploreText.visibility = View.GONE
                    listUser.visibility = View.GONE
                    labelResult.visibility = View.GONE
                    labelNotFound.visibility = View.VISIBLE
                }
            }
        })


    }

    private fun searchUser() {
        binding.apply {
            val query = textSearch.query.toString()
            if (query.isEmpty()) return
            barLoading(true)
            viewModel.setSearchUsers(query)

        }
    }

    private fun showSwipeRefreshLayout(state: Boolean) {
        binding.refreshActivity.isRefreshing = state
    }

    private fun barLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}

