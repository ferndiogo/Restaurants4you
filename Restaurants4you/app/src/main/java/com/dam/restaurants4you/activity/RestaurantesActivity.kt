package com.dam.restaurants4you.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dam.restaurants4you.R
import com.dam.restaurants4you.model.Restaurant
import com.dam.restaurants4you.retrofit.RetrofitInitializer
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RestaurantesActivity() : AppCompatActivity() {

    private var restaurant: Restaurant? = null
    private val options = arrayOf<CharSequence>("Câmara ", "Galeria", "Cancelar")


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
                processRestaurant()
            }


            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                Toast.makeText(
                    this@RestaurantesActivity,
                    R.string.ErrorServer,
                    Toast.LENGTH_LONG
                ).show()
                val it = Intent(this@RestaurantesActivity, LoginActivity::class.java)
                startActivity(it)
            }


        })

        val btnUpload = findViewById<Button>(R.id.btnUpload)
        btnUpload.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(this@RestaurantesActivity)
            builder.setTitle("Selecionar Imagem")
            builder.setItems(options, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    println(which)
                    if (which == 0) {
                        val takePic = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePic, 0)
                    } else if (which == 1) {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, 1)
                    } else {
                        dialog.dismiss()
                    }
                }
            })

            builder.show()
        })


        val btnSubmeter = findViewById<Button>(R.id.btnSub)
        btnSubmeter.setOnClickListener(View.OnClickListener {

            //val file = File(getRealPathFromURI(imageUri))
            //val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            //val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode !== RESULT_CANCELED) {

            when (requestCode) {

                0 -> if (resultCode === RESULT_OK && data != null) {
                    val image: Bitmap? = data.extras!!["data"] as Bitmap?
                    val imageSelect = findViewById<ImageView>(R.id.imageSelect)
                    Glide.with(this@RestaurantesActivity).load(image).into(imageSelect)

                    val txtImgSelec = findViewById<TextView>(R.id.txtImgSelec)
                    txtImgSelec.text = "Imagem Selecionada"
                }

                1 -> if (resultCode === RESULT_OK && data != null) {
                    val imageUri = data?.data
                    val imageSelect = findViewById<ImageView>(R.id.imageSelect)
                    Glide.with(this@RestaurantesActivity).load(imageUri).into(imageSelect)

                    val txtImgSelec = findViewById<TextView>(R.id.txtImgSelec)
                    txtImgSelec.text = "Imagem Selecionada"
                    //println(imageUri)
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun processRestaurant() {
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


        val a = "https://restaurants4you-api.azurewebsites.net/Fotos/"
        val b = restaurant?.images?.get(0)?.path

        val sb = StringBuilder()
        sb.append(a).append(b)
        val c = sb.toString()

        println(c)

        Glide.with(this@RestaurantesActivity).load(c).fitCenter().into(imageview)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = restaurant?.name
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }
}