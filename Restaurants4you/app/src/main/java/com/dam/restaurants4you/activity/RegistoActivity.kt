package com.dam.restaurants4you.activity

import android.content.Intent
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
        btnRegistar.setOnClickListener(View.OnClickListener {
            register()
        })

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener(View.OnClickListener {
            val it = Intent(this@RegistoActivity, LoginActivity::class.java)
            startActivity(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.info -> {
                val it = Intent(this@RegistoActivity, SobreActivity::class.java)
                startActivity(it)
                return true
            }
            else -> super.onOptionsItemSelected(item)
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
                        R.string.ErrorServer,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if (response.code() == 400) {
                        response.errorBody().let {
                            val aux = it?.string()
                            Toast.makeText(
                                this@RegistoActivity,
                                aux,
                                Toast.LENGTH_LONG
                            ).show()
                            findViewById<EditText>(R.id.userRegisto).setText("")
                            findViewById<EditText>(R.id.passwordRegisto).setText("")
                            findViewById<EditText>(R.id.confPassword).setText("")
                        }
                    } else {
                        // returns the TOKEN
                        response.body().let {
                            //val username: String? = (it as User).username
                            Toast.makeText(
                                this@RegistoActivity,
                                "Utilizador Adicionado : $txtUser",
                                Toast.LENGTH_LONG
                            ).show()
                            val it = Intent(this@RegistoActivity, LoginActivity::class.java)
                            startActivity(it)

                        }
                    }

                }
            })

        }
    }

}


