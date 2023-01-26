package com.dam.restaurants4you.fragmentos

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdaptadorPagina(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FragAddRest()
            1 -> return FragEditRest()
            2 -> return FragDelRest()
            else -> return FragAddRest()
        }
    }


}