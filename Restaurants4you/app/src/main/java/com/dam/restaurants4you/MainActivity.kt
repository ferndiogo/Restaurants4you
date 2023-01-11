package com.dam.restaurants4you

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnLogin = findViewById<View>(R.id.btnLogin) as Button
        val btnRegisto = findViewById<View>(R.id.btnRegisto) as Button

        btnLogin.setOnClickListener(View.OnClickListener {
            val it = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(it)
        })

        btnRegisto.setOnClickListener(View.OnClickListener {
            val it = Intent(this@MainActivity, RegistoActivity::class.java)
            startActivity(it)
        })

    }
}