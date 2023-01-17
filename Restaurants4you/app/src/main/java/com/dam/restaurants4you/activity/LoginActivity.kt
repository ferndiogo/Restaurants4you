package com.dam.restaurants4you.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.dam.restaurants4you.R
import com.dam.restaurants4you.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val btnRegisto = findViewById<Button>(R.id.btnRegistar)
        btnRegisto.setOnClickListener(View.OnClickListener {
            val it = Intent(this@LoginActivity, RegistoActivity::class.java)
            startActivity(it)
        })

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener(View.OnClickListener {
            login()
        })

        //to remover top bar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

    }

    fun login() {
        val username: String = findViewById<EditText>(R.id.userRegisto).text.toString()
        val pass: String = findViewById<EditText>(R.id.passwordRegisto).text.toString()

        val call = RetrofitInitializer().userService().login(username, pass)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.code() == 400) {
                    response.errorBody().let {
                        val aux = it?.string()
                        Toast.makeText(
                            this@LoginActivity,
                            aux,
                            Toast.LENGTH_LONG
                        ).show()
                        findViewById<EditText>(R.id.userRegisto).setText("")
                        findViewById<EditText>(R.id.passwordRegisto).setText("")
                    }
                } else {
                    response.body().let {
                        var token: String = it as String
                        token = "bearer $token"
                        saveToken(token)

                        val act = Intent(this@LoginActivity, MapaActivity::class.java)
                        //act.putExtra("token", token)
                        startActivity(act)
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    R.string.ErrorServer,
                    Toast.LENGTH_LONG
                )
            }

        })
    }

    fun saveToken(token: String) {
        val sharedPref: SharedPreferences = this@LoginActivity.getSharedPreferences(
            R.string.Name_File_Token.toString(),
            MODE_PRIVATE
        )
        val edit: SharedPreferences.Editor = sharedPref.edit()
        edit.putString("token", token)
        edit.apply()
        edit.commit()
    }
}