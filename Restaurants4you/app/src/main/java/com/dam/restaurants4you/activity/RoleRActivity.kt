package com.dam.restaurants4you.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dam.restaurants4you.R
import com.dam.restaurants4you.fragmentos.Fragmentos

class RoleRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.role_restaurant)

        val btnMapa = findViewById<Button>(R.id.btnMapa)

        btnMapa.setOnClickListener(View.OnClickListener {
            val it = Intent(this, MapaActivity::class.java)
            startActivity(it)
        })

        val btnFrags = findViewById<Button>(R.id.btnGere)

        btnFrags.setOnClickListener(View.OnClickListener {
            val it = Intent(this, Fragmentos::class.java)
            startActivity(it)
        })
    }
}