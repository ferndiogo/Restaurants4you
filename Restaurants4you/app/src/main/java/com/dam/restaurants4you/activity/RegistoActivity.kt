package com.dam.restaurants4you.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dam.restaurants4you.R
import com.dam.restaurants4you.model.User
import com.dam.restaurants4you.retrofit.RetrofitInitializer
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registo)

        val btnRegistar = findViewById<Button>(R.id.btnRegistar)
        btnRegistar.setOnClickListener (View.OnClickListener {
            register()
        })

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener(View.OnClickListener {
            val it = Intent(this@RegistoActivity, LoginActivity::class.java)
            startActivity(it)
        })
    }

    private fun register() {

        val txtUser: String = findViewById<EditText>(R.id.userRegisto).text.toString()
        val txtPass = findViewById<EditText>(R.id.passwordRegisto).text.toString()
        val txtConfPass = findViewById<EditText>(R.id.confPassword).text.toString()

        if (txtPass == txtConfPass) {

            val call = RetrofitInitializer()
                .userService()
                .register(txtUser, txtPass)

            call.enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    // writes on console the error
                    t.printStackTrace()
                    Toast.makeText(
                        this@RegistoActivity,
                        "Ocorreu um erro ao conectar ao servidor",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    // returns the TOKEN
                    response?.body().let {
                        val username: String? = (it as User).username
                        Toast.makeText(
                            this@RegistoActivity,
                            "Utilizador Adicionado : $username",
                            Toast.LENGTH_LONG
                        ).show()
                        val it = Intent(this@RegistoActivity, LoginActivity::class.java)
                        startActivity(it)

                    }

                }
            })

        }
    }

}


