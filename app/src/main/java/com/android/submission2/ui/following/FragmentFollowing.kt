package com.android.submission2.ui.following

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.submission2.R
import com.android.submission2.databinding.FragmentFollowingBinding
import com.android.submission2.model.Users
import com.android.submission2.ui.adapter.UsersAdapter
import com.android.submission2.ui.detail.DetailActivity

class FragmentFollowing : Fragment(R.layout.fragment_following) {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: UsersAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowingBinding.bind(view)

        val args = arguments
        username = args?.getString(DetailActivity.EXTRA_USERNAME).toString()

        adapter = UsersAdapter()

        adapter.setOnItemClickCallback((object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(user: Users) {
                Intent(requireActivity(), DetailActivity::class.java).run {
                    putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                    putExtra(DetailActivity.EXTRA_ID, user.id)
                    putExtra(DetailActivity.EXTRA_AVATAR, user.avatar_url)
                    putExtra(DetailActivity.EXTRA_TYPE, user.type)
                    requireActivity().startActivity(this)
                }
            }
        }))

        binding.apply {
            listUser.setHasFixedSize(true)
            listUser.layoutManager = LinearLayoutManager(activity)
            listUser.adapter = adapter

        }
        barLoading(true)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)

        viewModel.setListFollowers(username)
        viewModel.getListFollowers().observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setList(it)
                barLoading(false)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun barLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}