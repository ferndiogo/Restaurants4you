package com.dam.restaurants4you.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dam.restaurants4you.R
import com.dam.restaurants4you.fragmentos.Fragmentos

class RoleRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.role_restaurant)

        // referência para o botão ddo mapa
        val btnMapa = findViewById<Button>(R.id.btnMapa)
        // atribui uma função ao botão
        btnMapa.setOnClickListener(View.OnClickListener {
            // reecaminha para outra activity, neste caso para o Mapa activity
            val it = Intent(this, MapaActivity::class.java)
            startActivity(it)
        })

        // referência para o botão ddo mapa
        val btnFrags = findViewById<Button>(R.id.btnGere)
        // atribui uma função ao botão
        btnFrags.setOnClickListener(View.OnClickListener {
            // reecaminha para outra activity, neste caso para o Fragmentos activity que terá outras informações
            val it = Intent(this, Fragmentos::class.java)
            startActivity(it)
        })
    }

    /**
     * função para mostrar a action bar personalizada
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return true
    }

    /**
     * função para atribuir funções ao clicar nos diferentes item da action bar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logOut -> {
                Toast.makeText(this@RoleRActivity, "Logout com sucesso!", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.info -> {
                // reecaminha para outra activity, neste caso para o Sobre activity
                val it = Intent(this@RoleRActivity, SobreActivity::class.java)
                startActivity(it)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}