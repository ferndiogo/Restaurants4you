package com.dam.restaurants4you.activity

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dam.restaurants4you.R
import com.dam.restaurants4you.retrofit.RetrofitInitializer
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var token: String
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // coleta as permissões do utilizador
        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )

        // lê o token guardado no dispositivo
        token = loadToken()


        // caso exista um token guardado em memória é deita uma chamada à API (GET) para obter a
        // role do utilizador, caso seja "Restaurant" é encaminhado para uma activity, se for "User"
        // para outra, ou gera uma mensagem de erro
        if (!(token.isBlank())) {

            val call = RetrofitInitializer().userService().getRoles(token)
            call.enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>, response: Response<String>
                ) {
                    response.body().let {
                        val role: String = it as String
                        if (role == "Restaurant") {
                            // reecaminha para outra activity, neste caso para a RoleR activity
                            val it1 = Intent(this@LoginActivity, RoleRActivity::class.java)
                            startActivity(it1)
                        } else if (role == "User") {
                            // reecaminha para outra activity, neste caso para o Mapa activity
                            val it2 = Intent(this@LoginActivity, MapaActivity::class.java)
                            startActivity(it2)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Ocorreu um erro a verificar o tipo de utilizador",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Token inválido ou erro no servidor",
                        Toast.LENGTH_LONG
                    ).show()

                    // coloca os valores vazios nos campos de username e password
                    val username = findViewById<EditText>(R.id.userRegisto)
                    val pass = findViewById<EditText>(R.id.passwordRegisto)
                    username.setText("")
                    pass.setText("")
                }

            })
        }

        // referência para o botão de registo
        val btnRegisto = findViewById<Button>(R.id.btnRegistar)

        // atribui uma função ao botão
        btnRegisto.setOnClickListener {
            // reecaminha para outra activity, neste caso para o Registo activity
            val it3 = Intent(this@LoginActivity, RegistoActivity::class.java)
            startActivity(it3)
        }

        // referência para o botão de login
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // atribui uma função ao botão
        btnLogin.setOnClickListener {
            // chama a função para realizar o login
            login()
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
                val it = Intent(this@LoginActivity, SobreActivity::class.java)
                startActivity(it)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * função para realizar login
     */
    fun login() {

        // variáveis para obter o valor do username e password escritos pelo usuário
        val username: String = findViewById<EditText>(R.id.userRegisto).text.toString()
        val pass: String = findViewById<EditText>(R.id.passwordRegisto).text.toString()

        val user: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), username)

        val password: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), pass)

        // chamada à API (POST) para obter realizar o login obtendo o token
        val call = RetrofitInitializer().userService().login(user, password)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                // caso ocorra o erro 400 é criado um toast com a informação desse mesmo erro
                if (response.code() == 400) {
                    response.errorBody().let {
                        val aux = it?.string()
                        Toast.makeText(
                            this@LoginActivity,
                            aux,
                            Toast.LENGTH_LONG
                        ).show()
                        // coloca as EditText vazias
                        findViewById<EditText>(R.id.userRegisto).setText("")
                        findViewById<EditText>(R.id.passwordRegisto).setText("")

                    }
                } else {
                    response.body().let {
                        var token: String = it as String
                        // adiciona antes do token "bearer" para posteriormente ser guardado
                        token = "bearer $token"
                        // guarda o token no dispositivo para utilizações futuras
                        saveToken(token)

                        // chamada à API (GET) para obter o role do utilizador
                        val call2 = RetrofitInitializer().userService().getRoles(token)

                        call2.enqueue(object : Callback<String> {
                            override fun onResponse(
                                call: Call<String>, response: Response<String>
                            ) {
                                response.body().let {
                                    if (response.code() == 401) {
                                        descartToken()
                                        login()
                                    } else {
                                        // guarda a role
                                        val role: String = it as String

                                        // caso seja "Restaurant" é encaminhado para uma activity, se for
                                        // "User" para outra, ou gera uma mensagem de erro
                                        if (role == "Restaurant") {
                                            val it4 =
                                                Intent(
                                                    this@LoginActivity,
                                                    RoleRActivity::class.java
                                                )
                                            startActivity(it4)
                                        } else if (role == "User") {
                                            val it5 =
                                                Intent(this@LoginActivity, MapaActivity::class.java)
                                            startActivity(it5)
                                        } else {
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Ocorreu um erro a verificar o tipo de utilizador",
                                                Toast.LENGTH_LONG
                                            )
                                        }
                                    }
                                }

                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Token inválido ou erro no servidor",
                                    Toast.LENGTH_LONG
                                ).show()
                                // reecaminha para outra activity, neste caso para o Login activity
                                val it6 = Intent(this@LoginActivity, LoginActivity::class.java)
                                startActivity(it6)
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
                ).show()
            }

        })
    }

    /**
     * função para coletar as permissões do utilizador
     */
    private fun requestPermissionsIfNecessary(permissions: Array<out String>) {
        val permissionsToRequest = ArrayList<String>()
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // a permissão não é concedida
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this@LoginActivity,
                permissionsToRequest.toArray(arrayOf<String>()),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    /**
     * função que irá ler o token guardado em memória
     */
    private fun loadToken(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            R.string.Name_File_Token.toString(),
            MODE_PRIVATE
        )
        return sharedPreferences.getString("token", "").toString()
    }

    /**
     * função que irá guardar o token no dispositivo
     */
    fun saveToken(token: String) {
        val sharedPref: SharedPreferences = this@LoginActivity.getSharedPreferences(
            R.string.Name_File_Token.toString(),
            MODE_PRIVATE
        )
        val edit: SharedPreferences.Editor = sharedPref.edit()
        edit.putString("token", token)
        edit.apply()
    }

    /**
     * Descarta o token guarda no armazenamento
     */
    private fun descartToken() {
        val sharedPref: SharedPreferences = this.getSharedPreferences(
            R.string.Name_File_Token.toString(),
            MODE_PRIVATE
        )
        val edit: SharedPreferences.Editor = sharedPref.edit()
        edit.putString("token", "")
        edit.apply()
    }
}