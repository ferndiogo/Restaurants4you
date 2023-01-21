package com.dam.restaurants4you.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dam.restaurants4you.R

class SobreActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sobre_info)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}