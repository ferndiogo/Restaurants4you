package com.dam.restaurants4you.activity

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageButton
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
    private var verificar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.janela_detalhes)

        val token = loadToken()

        //receber do marcador
        val id = intent.getIntExtra("id", -1)

        //receber da camera
        val img = intent.getStringExtra("pathImg")
        val idR = intent.getIntExtra("idR", -1)

        val imageSelect = findViewById<ImageView>(R.id.imageSelect)
        val txtImgSelec = findViewById<TextView>(R.id.txtImgSelec)


        if (!((img.isNullOrBlank()) || (idR == null))) {

            val call = RetrofitInitializer().restaurantService().listRestaurants(loadToken(), idR)
            call.enqueue(object : Callback<Restaurant> {
                override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                    response.body().let {
                        restaurant = it as Restaurant
                    }
                    //restaurant?.let { println(it.id) }
                    // restaurant?.let { println(it.name) }
                    processRestaurant()

                    Glide.with(this@RestaurantesActivity).load(img).into(imageSelect)

                    txtImgSelec.text = "Imagem Selecionada"

                    if (restaurant != null) {

                        val myUri = Uri.parse(img)
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor =
                            contentResolver.query(myUri!!, filePathColumn, null, null, null)
                        cursor!!.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val imgDecodableString = cursor.getString(columnIndex)
                        cursor.close()
                        file = File(imgDecodableString)
                        verificar = true
                    }
                }


                override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                    Toast.makeText(
                        this@RestaurantesActivity, R.string.ErrorServer, Toast.LENGTH_LONG
                    ).show()
                    val it = Intent(this@RestaurantesActivity, LoginActivity::class.java)
                    startActivity(it)
                }


            })


        } else {

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

                            val it = Intent(this@RestaurantesActivity, CamaraActivity::class.java)
                            it.putExtra("idR", id)
                            startActivity(it)

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

        }
        val btnSubmeter = findViewById<Button>(R.id.btnSub)
        btnSubmeter.setOnClickListener(View.OnClickListener {
            if (!verificar) {
                Toast.makeText(
                    this@RestaurantesActivity, "Precisa de selecionar uma imagem", Toast.LENGTH_LONG
                ).show()
            } else {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val imagePart = MultipartBody.Part.createFormData("imagem", file.name, requestFile)

                val call = restaurant!!.id?.let { it1 ->
                    RetrofitInitializer().imageService().addImage(
                        loadToken(), it1, imagePart
                    )
                }
                call?.enqueue(object : Callback<ImageRest> {
                    override fun onResponse(
                        call: Call<ImageRest>, response: Response<ImageRest>
                    ) {
                        Toast.makeText(
                            this@RestaurantesActivity, "Imagem Submetida", Toast.LENGTH_LONG
                        ).show()
                        val it = Intent(this@RestaurantesActivity, MapaActivity::class.java)
                        startActivity(it)
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

                1 -> if (resultCode === RESULT_OK && data != null) {
                    imageUri = data?.data!!
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(imageUri!!, filePathColumn, null, null, null)
                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val imgDecodableString = cursor.getString(columnIndex)
                    cursor.close()
                    file = File(imgDecodableString)
                    verificar = true


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

        val txtDesc = findViewById<TextView>(R.id.txtDesc)
        val txtLocal = findViewById<TextView>(R.id.txtLocal)
        val txtContacto = findViewById<TextView>(R.id.txtContacto)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)
        val txtHorario = findViewById<TextView>(R.id.txtHorario)
        val imageview = findViewById<ImageView>(R.id.imageView)


        val btnDir = findViewById<ImageButton>(R.id.btnDir)
        val btnEsq = findViewById<ImageButton>(R.id.btnEsq)

        // define um valor à TextView
        txtDesc.text = restaurant?.description
        txtLocal.text = restaurant?.localization
        txtContacto.text = restaurant?.contact
        txtEmail.text = restaurant?.email
        txtHorario.text = restaurant?.Time


        val a = "https://restaurants4you-api.azurewebsites.net/Fotos/"
        if (restaurant?.images?.size != 0) {
            val b = restaurant?.images?.get(i)?.path

            val sb = StringBuilder()
            sb.append(a).append(b)
            val c = sb.toString()

            println(c)

            Glide.with(this@RestaurantesActivity).load(c).fitCenter().into(imageview)
        }

        // tamanho do array que contém as imagens do restaurante
        var tam = restaurant?.images?.size

        if (tam == 0) {
            // carrega a foto atrés do URL
            Glide.with(this)
                .load(
                    getResources()
                        .getIdentifier("vazio", "drawable", this.getPackageName())
                )
                .into(imageview)
        } else {

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
        }

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = restaurant?.name
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * função que irá ler o token guardado em memória
     */
    private fun loadToken(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            R.string.Name_File_Token.toString(), MODE_PRIVATE
        )
        return sharedPreferences.getString("token", "").toString()
    }
}