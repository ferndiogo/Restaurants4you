package com.dam.restaurants4you.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        token = loadToken()

        if (!(token.isNullOrBlank())) {

            val call = RetrofitInitializer().userService().getRoles(token)
            println("Iam Here")
            call.enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>, response: Response<String>
                ) {
                    response.body().let {
                        var role: String = it as String
                        if (role == "Restaurant") {
                            val it = Intent(this@LoginActivity, RoleRActivity::class.java)
                            startActivity(it)
                        } else if (role == "User") {
                            val it = Intent(this@LoginActivity, MapaActivity::class.java)
                            startActivity(it)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Ocorreu um erro a verificar o tipo de utilizador",
                                Toast.LENGTH_LONG
                            )
                        }
                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Token inválido ou erro no servidor",
                        Toast.LENGTH_LONG
                    )
                    val it = Intent(this@LoginActivity, LoginActivity::class.java)
                    startActivity(it)
                }

            })
        }


        val btnRegisto = findViewById<Button>(R.id.btnRegistar)
        btnRegisto.setOnClickListener(View.OnClickListener {
            val it = Intent(this@LoginActivity, RegistoActivity::class.java)
            startActivity(it)
        })

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener(View.OnClickListener {
            login()
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.info -> {
                val it = Intent(this@LoginActivity, SobreActivity::class.java)
                startActivity(it)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

                        val call = RetrofitInitializer().userService().getRoles(token)
                        println("Iam Here")
                        call.enqueue(object : Callback<String> {
                            override fun onResponse(
                                call: Call<String>, response: Response<String>
                            ) {
                                response.body().let {
                                    var role: String = it as String
                                    if (role == "Restaurant") {
                                        val it = Intent(this@LoginActivity, RoleRActivity::class.java)
                                        startActivity(it)
                                    } else if (role == "User") {
                                        val it = Intent(this@LoginActivity, MapaActivity::class.java)
                                        startActivity(it)
                                    } else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Ocorreu um erro a verificar o tipo de utilizador",
                                            Toast.LENGTH_LONG
                                        )
                                    }
                                }

                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Token inválido ou erro no servidor",
                                    Toast.LENGTH_LONG
                                )
                                val it = Intent(this@LoginActivity, LoginActivity::class.java)
                                startActivity(it)
                            }

                        })
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

    private fun loadToken(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            R.string.Name_File_Token.toString(),
            MODE_PRIVATE
        )
        return sharedPreferences.getString("token", "").toString()
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