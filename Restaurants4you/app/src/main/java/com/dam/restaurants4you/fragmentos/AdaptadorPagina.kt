package com.dam.restaurants4you.fragmentos

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdaptadorPagina(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return FragmentoMapa()
            1 -> return FragmentoRestaurantes()
            else -> return FragmentoMapa()
        }
    }
}