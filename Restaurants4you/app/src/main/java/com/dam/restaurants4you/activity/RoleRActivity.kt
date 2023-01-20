package com.dam.restaurants4you.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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

    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.role_restaurant)

        token = loadToken()

        if (token.isNullOrBlank()) {
            val it = Intent(this@RoleRActivity, LoginActivity::class.java)
            startActivity(it)
        } else {
            val call = RetrofitInitializer().userService().getRoles(token)
            println("Iam Here")
            call.enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>, response: Response<String>
                ) {
                    response.body().let {
                        var role: String = it as String
                        if(role == "User"){
                            val it = Intent(this@RoleRActivity, MapaActivity::class.java)
                            startActivity(it)
                        }
                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(
                        this@RoleRActivity,
                        "Token inválido ou erro no servidor",
                        Toast.LENGTH_LONG
                    )
                    val it = Intent(this@RoleRActivity, LoginActivity::class.java)
                    startActivity(it)
                }

            })

        }

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

    private fun loadToken(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            R.string.Name_File_Token.toString(),
            MODE_PRIVATE
        )
        return sharedPreferences.getString("token", "").toString()
    }
}