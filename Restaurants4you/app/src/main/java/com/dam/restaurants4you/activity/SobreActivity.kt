package com.dam.restaurants4you.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dam.restaurants4you.R

class SobreActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sobre_info)

        // coloca a action bar visível
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * função para permitir que seja possível voltar atrás na activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}