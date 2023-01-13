package com.dam.restaurants4you.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.dam.restaurants4you.R
import com.dam.restaurants4you.model.Restaurant
import com.dam.restaurants4you.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantesActivity() : AppCompatActivity() {

    private var restaurant: Restaurant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.janela_detalhes)

        val id = intent.getIntExtra("id", -1)
        val token = intent.getStringExtra("token")

        println(id)
        println(token)

        val call = RetrofitInitializer().restaurantService().listRestaurants(token!!, id)
        call.enqueue(object : Callback<Restaurant> {
            override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                response.body().let {
                    restaurant = it as Restaurant
                }
                //restaurant?.let { println(it.id) }
               // restaurant?.let { println(it.name) }

                val txtNome = findViewById<TextView>(R.id.txtNome)
                val txtDesc = findViewById<TextView>(R.id.txtDesc)
                val txtLocal = findViewById<TextView>(R.id.txtLocal)
                val txtContacto = findViewById<TextView>(R.id.txtContacto)
                val txtEmail = findViewById<TextView>(R.id.txtEmail)
                val txtHorario = findViewById<TextView>(R.id.txtHorario)
                val imageview = findViewById<ImageView>(R.id.imageView)


                //txtNome.text = restaurant?.images?.get(0)?.path

                // define um valor à TextView
                txtNome.text = restaurant?.name
                txtDesc.text = restaurant?.description
                txtLocal.text = restaurant?.localization
                txtContacto.text = restaurant?.contact
                txtEmail.text = restaurant?.email
                txtHorario.text = restaurant?.Time

                Glide.with(this@RestaurantesActivity)
                    .load("https://restaurants4you-api.azurewebsites.net/Fotos/0_6fc0c8d6-9066-40ad-a231-ba2c96305b97.png")
                    .fitCenter()
                    .into(imageview)

            }

            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                Toast.makeText(this@RestaurantesActivity,"Token inválido ou erro no servidor", Toast.LENGTH_LONG)
                val it = Intent(this@RestaurantesActivity, LoginActivity::class.java)
                startActivity(it)
            }


        })
    }
}