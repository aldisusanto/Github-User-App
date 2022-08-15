package com.android.submission2.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.submission2.ui.followers.FragmentFollowers
import com.android.submission2.ui.following.FragmentFollowing

class SectionPagerAdapter(
    activity: AppCompatActivity,
    data: Bundle
) :
    FragmentStateAdapter(activity) {
    private var fragmentBundle: Bundle = data

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FragmentFollowers()
            1 -> fragment = FragmentFollowing()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }
}