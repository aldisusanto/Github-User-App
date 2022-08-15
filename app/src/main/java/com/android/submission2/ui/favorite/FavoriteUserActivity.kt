package com.android.submission2.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.submission2.R
import com.android.submission2.data.room.Favorite
import com.android.submission2.databinding.ActivityFavoriteUserBinding
import com.android.submission2.model.Users
import com.android.submission2.ui.adapter.UsersAdapter
import com.android.submission2.ui.detail.DetailActivity

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.apply {
            title = getString(R.string.action_bar_title_favorite)
            setDisplayHomeAsUpEnabled(true)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        adapter = UsersAdapter()
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback((object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(user: Users) {
                Intent(this@FavoriteUserActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_ID, user.id)
                    it.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    it.putExtra(DetailActivity.EXTRA_AVATAR, user.avatar_url)
                    it.putExtra(DetailActivity.EXTRA_TYPE, user.type)
                    startActivity(it)
                }
            }
        }))

        binding.apply {
            listUser.setHasFixedSize(true)
            listUser.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            listUser.adapter = adapter
        }

        viewModel.readFavoriteUsers()?.observe(this, {
            if (it != null) {
                val toArrayList = mapList(it)
                adapter.setList(toArrayList)
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

    private fun mapList(it: List<Favorite>): ArrayList<Users> {
        val listUser = ArrayList<Users>()
        for (user in it) {
            val newMapped = Users(
                user.id,
                user.username,
                user.avatar_url,
                user.type
            )
            listUser.add(newMapped)

        }
        return listUser
    }

}