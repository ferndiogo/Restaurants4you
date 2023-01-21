package com.dam.restaurants4you.fragmentos

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.dam.restaurants4you.R
import com.google.android.material.tabs.TabLayout


class Fragmentos : AppCompatActivity(){
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var myViewPagerAdapter: AdaptadorPagina

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragmentos)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.view_pager2)
        myViewPagerAdapter = AdaptadorPagina(this)
        viewPager2.adapter = myViewPagerAdapter
        viewPager2.setUserInputEnabled(false);

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
                //viewPager2.currentItem
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}