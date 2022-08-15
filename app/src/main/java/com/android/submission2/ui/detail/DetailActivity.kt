package com.android.submission2.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.submission2.CheckConnection
import com.android.submission2.R
import com.android.submission2.databinding.ActivityDetailBinding
import com.android.submission2.ui.adapter.SectionPagerAdapter
import com.android.submission2.ui.favorite.FavoriteUserActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_TYPE = "extra_type"
        private val TITLE_TABS = intArrayOf(R.string.string_followers, R.string.string_following)
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var checkConnection: CheckConnection

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.share -> {
                val intent = Intent(Intent.ACTION_SEND)

                val shareText =
                    resources.getString(
                        R.string.string_share_body,
                        supportActionBar?.title,
                        supportActionBar?.title
                    )
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, binding.textName.text)
                intent.putExtra(Intent.EXTRA_TEXT, shareText)
                startActivity(Intent.createChooser(intent, getString(R.string.string_share)))
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //check the internet connection first before displaying data from the API
        checkConnectionUI()

    }

    private fun checkConnectionUI() {
        checkConnection = CheckConnection(application)

        checkConnection.observe(this, { isConnected ->
            if (isConnected) {
                binding.apply {
                    detailUser.visibility = View.VISIBLE
                }
                barLoading(true)
                setDetailUserUI()
                showSwipeRefreshLayout(false)
            } else {
                binding.apply {
                    detailUser.visibility = View.GONE
                }
                feedbackFavorite(resources.getString(R.string.redirect_message), false)
                showSwipeRefreshLayout(true)
            }
        })
    }

    private fun setDetailUserUI() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)
        val type = intent.getStringExtra(EXTRA_TYPE)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(
            this,
        ).get(DetailViewModel::class.java)
        viewModel.setDetailUser(username.toString())
        viewModel.getDetailUser().observe(this, {
            if (it != null) {
                barLoading(false)
                binding.apply {
                    if (it.name == null) {
                        textName.text = getString(R.string.not_found)
                    } else {
                        textName.text = it.name

                    }
                    supportActionBar?.title = it.login
                    if (it.company == null) {
                        textCompany.text = "-"
                    } else {
                        textCompany.text = it.company
                    }

                    if (it.location == null) {
                        textLocation.text = "-"
                    } else {
                        textLocation.text = it.location
                    }

                    textRepository.text = it.public_repos.toString()
                    textFollowers.text = it.followers.toString()
                    textFollowing.text = it.following.toString()
                    Glide.with(this@DetailActivity)
                        .load(it.avatar_url)
                        .apply(
                            RequestOptions.circleCropTransform()
                                .placeholder(R.drawable.ic_baseline_account_circle_24_black)
                        )
                        .into(imgUser)


                    refreshDetail.setOnRefreshListener {
                        showSwipeRefreshLayout(false)
                        viewModel.setDetailUser(username.toString())

                    }

                }
            }
        })


        val sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        binding.apply {
            pager.adapter = sectionPagerAdapter
            TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
                tab.text = resources.getString(TITLE_TABS[position])
            }.attach()
        }

        // setupFavorite
        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.btnToggleFavorite.isChecked = true
                        isChecked = true
                    } else {
                        binding.btnToggleFavorite.isChecked = false
                        isChecked = false
                    }

                }

            }
        }

        binding.btnToggleFavorite.setOnClickListener {
            isChecked = !isChecked

            if (isChecked) {
                viewModel.insertToFavorite(
                    id,
                    username.toString(),
                    avatar.toString(),
                    type.toString()
                )
                feedbackFavorite(
                    resources.getString(R.string.user_added_to_favorite_label),
                    true
                )
            } else {
                viewModel.deleteUserToFavorite(id)
                feedbackFavorite(
                    resources.getString(R.string.user_remove_from_favorite_label),
                    true
                )
            }
        }
    }

    private fun showSwipeRefreshLayout(state: Boolean) {
        binding.refreshDetail.isRefreshing = state
    }

    private fun barLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun feedbackFavorite(message: String, useSnack: Boolean) {
        if (useSnack) {
            Snackbar.make(binding.detailUser, message, Snackbar.LENGTH_LONG).apply {
                animationMode = Snackbar.ANIMATION_MODE_SLIDE
                setAction(resources.getString(R.string.favorite_action_label)) {
                    startActivity(
                        Intent(this@DetailActivity, FavoriteUserActivity::class.java)
                    )
                }
                show()
            }
        } else {
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }


}