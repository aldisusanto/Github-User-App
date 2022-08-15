package com.android.submission2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.submission2.R
import com.android.submission2.databinding.ItemUsersBinding
import com.android.submission2.model.Users
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private val listUsers = ArrayList<Users>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    fun setList(newUsersListData: ArrayList<Users>) {
        listUsers.clear()
        listUsers.addAll(newUsersListData)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder((view))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int = listUsers.size

    inner class UserViewHolder(private val binding: ItemUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Users) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .apply(
                        RequestOptions.circleCropTransform()
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                    )
                    .into(imgUser)

                textUsername.text = user.login
                textType.text = user.type

            }
        }
    }


    interface OnItemClickCallback {
        fun onItemClicked(user: Users)
    }

}