package com.dam.restaurants4you.activity

import android.os.Bundle
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
        btnRegistar.setOnClickListener {
            register()
        }
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
                        "ocorreu um erro ao conectar ao servidor",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    // returns the TOKEN
                    println("Deu Certo")
                }
            })

        }
    }

}


