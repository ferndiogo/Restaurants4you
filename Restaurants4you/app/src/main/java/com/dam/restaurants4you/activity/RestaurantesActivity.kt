package com.dam.restaurants4you.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import com.dam.restaurants4you.model.ImageRest
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
    private lateinit var file: File
    private lateinit var imageUri: Uri

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
                    this@RestaurantesActivity, R.string.ErrorServer, Toast.LENGTH_LONG
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

            if (restaurant != null) {
                //val imagem = MultipartBody.Part.createFormData("imagem", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))

                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val imagePart = MultipartBody.Part.createFormData("imagem", file.name, requestFile)

                val call =
                    RetrofitInitializer().imageService().addImage(token, restaurant!!.id, imagePart)
                call.enqueue(object : Callback<ImageRest> {
                    override fun onResponse(call: Call<ImageRest>, response: Response<ImageRest>) {
                        Toast.makeText(
                            this@RestaurantesActivity, "Imagem Submetida", Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onFailure(call: Call<ImageRest>, t: Throwable) {
                        Toast.makeText(
                            this@RestaurantesActivity,
                            "Houve um erro a submeter a Imagem",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
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
                    imageUri = data?.data!!
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(imageUri!!, filePathColumn, null, null, null)
                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val imgDecodableString = cursor.getString(columnIndex)
                    cursor.close()
                    file = File(imgDecodableString)


                    println(file)

                    println(file)

                    val imageSelect = findViewById<ImageView>(R.id.imageSelect)
                    Glide.with(this@RestaurantesActivity).load(file).into(imageSelect)

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

        var i = 0

        val txtNome = findViewById<TextView>(R.id.txtNome)
        val txtDesc = findViewById<TextView>(R.id.txtDesc)
        val txtLocal = findViewById<TextView>(R.id.txtLocal)
        val txtContacto = findViewById<TextView>(R.id.txtContacto)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)
        val txtHorario = findViewById<TextView>(R.id.txtHorario)
        val imageview = findViewById<ImageView>(R.id.imageView)


        val btnDir = findViewById<TextView>(R.id.btnDir)
        val btnEsq = findViewById<TextView>(R.id.btnEsq)

        // tamanho do array que contém as imagens do restaurante
        var tam = restaurant?.images?.size

        // descontar 1 ao tamanho do array por ser [0,1,2,3,...]
        if (tam != null) {
            tam = tam - 1
        }

        // se o i for menor que tamanho do array das imagens então incrementa
        // senão volta ao indice 0
        btnDir.setOnClickListener(View.OnClickListener {
            if (i < tam!!) {
                i++

                //constrói a string para carregar a foto
                val a = "https://restaurants4you-api.azurewebsites.net/Fotos/"
                val b = restaurant?.images?.get(i)?.path
                val sb = StringBuilder()
                sb.append(a).append(b)
                val c = sb.toString()

                // carrega a foto atrés do URL
                Glide.with(this@RestaurantesActivity).load(c).fitCenter().into(imageview)
            } else {
                i = 0

                //constrói a string para carregar a foto
                val a = "https://restaurants4you-api.azurewebsites.net/Fotos/"
                val b = restaurant?.images?.get(i)?.path
                val sb = StringBuilder()
                sb.append(a).append(b)
                val c = sb.toString()

                // carrega a foto atrés do URL
                Glide.with(this@RestaurantesActivity).load(c).fitCenter().into(imageview)
            }
        })

        // se o i for diferente de 0 decrementa
        // senão volta ao indice 1 e apresenta a primeira imagem de indice 0
        btnEsq.setOnClickListener(View.OnClickListener {
            if (!(i == 0)) {
                i--

                //constrói a string para carregar a foto
                val a = "https://restaurants4you-api.azurewebsites.net/Fotos/"
                val b = restaurant?.images?.get(i)?.path
                println(b)
                val sb = StringBuilder()
                sb.append(a).append(b)
                val c = sb.toString()

                // carrega a foto atrés do URL
                Glide.with(this@RestaurantesActivity).load(c).fitCenter().into(imageview)

            } else {
                if (tam != null) {
                    i = tam + 1

                    //constrói a string para carregar a foto
                    val a = "https://restaurants4you-api.azurewebsites.net/Fotos/"
                    val b = restaurant?.images?.get(tam)?.path
                    val sb = StringBuilder()
                    sb.append(a).append(b)
                    val c = sb.toString()

                    // carrega a foto atrés do URL
                    Glide.with(this@RestaurantesActivity).load(c).fitCenter().into(imageview)
                }
            }
        })

        //txtNome.text = restaurant?.images?.get(1)?.path

        // define um valor à TextView
        txtNome.text = restaurant?.name
        txtDesc.text = restaurant?.description
        txtLocal.text = restaurant?.localization
        txtContacto.text = restaurant?.contact
        txtEmail.text = restaurant?.email
        txtHorario.text = restaurant?.Time


        val a = "https://restaurants4you-api.azurewebsites.net/Fotos/"
        val b = restaurant?.images?.get(i)?.path
        println(b)

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