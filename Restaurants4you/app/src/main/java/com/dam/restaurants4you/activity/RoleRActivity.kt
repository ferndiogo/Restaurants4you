package com.dam.restaurants4you.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        btnMapa.setOnClickListener {
            // reecaminha para outra activity, neste caso para o Mapa activity
            val it1 = Intent(this, MapaActivity::class.java)
            startActivity(it1)
        }

        // referência para o botão ddo mapa
        val btnFrags = findViewById<Button>(R.id.btnGere)
        // atribui uma função ao botão
        btnFrags.setOnClickListener {
            // reecaminha para outra activity, neste caso para o Fragmentos activity que terá outras informações
            val it2 = Intent(this, Fragmentos::class.java)
            startActivity(it2)
        }
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
        return when (item.itemId) {
            R.id.logOut -> {
                logout()
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

    /**
     * função que irá fazer logout do utilizador descartando o token
     */
    private fun logout() {
        val sharedPref: SharedPreferences = this@RoleRActivity.getSharedPreferences(
            R.string.Name_File_Token.toString(), MODE_PRIVATE
        )
        val edit: SharedPreferences.Editor = sharedPref.edit()
        edit.putString("token", "")
        edit.apply()
        edit.commit()
        val inte = Intent(this@RoleRActivity, LoginActivity::class.java)
        startActivity(inte)
        Toast.makeText(this@RoleRActivity, "Logout com sucesso!", Toast.LENGTH_LONG).show()
    }
}