package com.dam.restaurants4you.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dam.restaurants4you.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnLogin = findViewById<View>(R.id.btnLogin) as Button
        val btnRegisto = findViewById<View>(R.id.btnRegisto) as Button
        val btnmapa = findViewById<View>(R.id.mapaa) as Button

        btnLogin.setOnClickListener(View.OnClickListener {
            val it = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(it)
        })

        btnRegisto.setOnClickListener(View.OnClickListener {
            val it = Intent(this@MainActivity, RegistoActivity::class.java)
            startActivity(it)
        })

        btnmapa.setOnClickListener(View.OnClickListener {
            val it = Intent(this@MainActivity, MapaActivity::class.java)
            startActivity(it)
        })

    }
}