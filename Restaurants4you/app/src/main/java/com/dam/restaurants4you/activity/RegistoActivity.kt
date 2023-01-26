package com.dam.restaurants4you.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        // referência para o botão de registo
        val btnRegistar = findViewById<Button>(R.id.btnRegistar)
        // atribui uma função ao botão
        btnRegistar.setOnClickListener {
            // chama a função para realizar o registo
            register()
        }

        // referência para o botão de registo
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        // atribui uma função ao botão
        btnLogin.setOnClickListener {
            // reecaminha para outra activity, neste caso para o Login activity
            val it2 = Intent(this@RegistoActivity, LoginActivity::class.java)
            startActivity(it2)
        }

    }

    /**
     * função para mostrar a action bar personalizada
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu1, menu)
        return true
    }

    /**
     * função para atribuir funções ao clicar nos diferentes item da action bar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.info -> {
                // reecaminha para outra activity, neste caso para o Sobre activity
                val it = Intent(this@RegistoActivity, SobreActivity::class.java)
                startActivity(it)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * função para realizar o resgito de um utilizador
     */
    private fun register() {

        // variáveis para obter o valor do username e password escritos pelo usuário
        val txtUser: String = findViewById<EditText>(R.id.userRegisto).text.toString()
        val txtPass = findViewById<EditText>(R.id.passwordRegisto).text.toString()
        val txtConfPass = findViewById<EditText>(R.id.confPassword).text.toString()

        // confirma se as passwords são iguais
        if (txtPass == txtConfPass) {

            val user: RequestBody = RequestBody.create(MediaType.parse("text/plain"), txtUser)

            val password: RequestBody = RequestBody.create(MediaType.parse("text/plain"), txtPass)

            // chamada à API (POST) para registar o utilizador
            val call = RetrofitInitializer().userService().register(user, password)

            call.enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    // escreve na consola o erro
                    t.printStackTrace()
                    Toast.makeText(
                        this@RegistoActivity, R.string.ErrorServer, Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    // caso ocorra o erro 400 é criado um toast com a informação desse mesmo erro
                    if (response.code() == 400) {
                        response.errorBody().let {
                            val aux = it?.string()
                            Toast.makeText(
                                this@RegistoActivity, aux, Toast.LENGTH_LONG
                            ).show()
                            // coloca as EditText vazias
                            findViewById<EditText>(R.id.userRegisto).setText("")
                            findViewById<EditText>(R.id.passwordRegisto).setText("")
                            findViewById<EditText>(R.id.confPassword).setText("")
                        }
                    } else {
                        response.body().let {
                            Toast.makeText(
                                this@RegistoActivity,
                                "Utilizador Registado : $txtUser",
                                Toast.LENGTH_LONG
                            ).show()
                            // reecaminha para outra activity, neste caso para o Login activity
                            val it3 = Intent(this@RegistoActivity, LoginActivity::class.java)
                            startActivity(it3)

                        }
                    }

                }
            })

        }
    }

}


