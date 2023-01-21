package com.dam.restaurants4you.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dam.restaurants4you.R
import com.dam.restaurants4you.fragmentos.Fragmentos
import com.dam.restaurants4you.model.Restaurant
import com.dam.restaurants4you.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logOut -> {
                Toast.makeText(this@RoleRActivity, "Logout com sucesso!", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.info -> {
                val it = Intent(this@RoleRActivity, SobreActivity::class.java)
                startActivity(it)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}