package com.dam.restaurants4you.activity

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
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


class RestaurantesActivity : AppCompatActivity() {

    private var restaurant: Restaurant? = null
    private val options = arrayOf<CharSequence>("Câmara ", "Galeria")
    private lateinit var file: File
    private lateinit var imageUri: Uri

    // flag para prevenir que não seja possível submeter uma imagem
    // antes de ser selecionada
    private var verificar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.janela_detalhes)

        // lê o token guardado no dispositivo
        val token = loadToken()

        // recebe do marcador a id do restaurante que irá mostrar os detalhes
        val id = intent.getIntExtra("id", -1)

        // recebe da camera a imagem que irá ser adicionada e o respetivo id do restaurante
        val img = intent.getStringExtra("pathImg")
        val idR = intent.getIntExtra("idR", -1)

        // referências para a imageView da imagem selecionada e o texto
        val imageSelect = findViewById<ImageView>(R.id.imageSelect)
        val txtImgSelec = findViewById<TextView>(R.id.txtImgSelec)


        // caso a imagem que vem da câmera não seja null ou vazia ou o id null irá
        // ser feita uma chamada à API (GET) para obter os detalhes do restaurante que vamos adicionar uma imagem
        if (!(img.isNullOrBlank())) {

            val call = RetrofitInitializer().restaurantService().listRestaurants(loadToken(), idR)
            call.enqueue(object : Callback<Restaurant> {
                override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                    response.body().let {
                        restaurant = it as Restaurant
                    }

                    // chama a função que carrega todas as informações acerca de uma restaurante
                    processRestaurant()

                    // coloca a imagem capturda na câmara no imageView
                    Glide.with(this@RestaurantesActivity).load(img).into(imageSelect)

                    // altera o texto para o utilizador ser informado que a foto está selecionada
                    val aux = "Imagem Selecionada"
                    txtImgSelec.text = aux

                    // se não houver nenhum erro ao carregar a imagem trata
                    // da imagem para ser possível ser enviada para a API
                    if (restaurant != null) {

                        // URI da imagem
                        val myUri = Uri.parse(img)
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor =
                            contentResolver.query(myUri!!, filePathColumn, null, null, null)
                        cursor!!.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val imgDecodableString = cursor.getString(columnIndex)
                        cursor.close()
                        // converte em ficheiro
                        file = File(imgDecodableString)

                        // true = imagem "pronta" para enviar
                        verificar = true
                    }
                }

                override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                    Toast.makeText(
                        this@RestaurantesActivity, R.string.ErrorServer, Toast.LENGTH_LONG
                    ).show()
                    // reecaminha para outra activity, neste caso para o Mapa activity em caso de falha
                    val it = Intent(this@RestaurantesActivity, MapaActivity::class.java)
                    startActivity(it)
                }
            })

        } else {

            // chamada à API (GET) para obter os detalhes do restaurante selecionado no marcador
            val call = RetrofitInitializer().restaurantService().listRestaurants(token, id)
            call.enqueue(object : Callback<Restaurant> {
                override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                    response.body().let {
                        restaurant = it as Restaurant
                    }

                    // chama a função que carrega todas as informações acerca de uma restaurante
                    processRestaurant()
                }


                override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                    Toast.makeText(
                        this@RestaurantesActivity, R.string.ErrorServer, Toast.LENGTH_LONG
                    ).show()
                    // reecaminha para outra activity, neste caso para o Mapa activity em caso de falha
                    val it = Intent(this@RestaurantesActivity, MapaActivity::class.java)
                    startActivity(it)
                }
            })

            // referência para o botão para selecionar a imagem
            val btnUpload = findViewById<Button>(R.id.btnUpload)
            btnUpload.setOnClickListener {

                // "Pop up" para o utilizador escolher se quer selecionar uma imagem
                //  da galeria ou capturar uma imagem da câmara
                val builder = AlertDialog.Builder(this@RestaurantesActivity)
                builder.setTitle("Selecionar Imagem")
                builder.setItems(options, object : DialogInterface.OnClickListener {

                    override fun onClick(dialog: DialogInterface, which: Int) {
                        println(which)
                        // selecionou câmara (0)
                        if (which == 0) {

                            // reecaminha para outra activity, neste caso para a Câmara activity
                            val it1 = Intent(this@RestaurantesActivity, CamaraActivity::class.java)
                            // envia o id do restaurante ao que irá ser adicionada a imagem mas depois
                            // ser novamente recebido
                            it1.putExtra("idR", id)
                            startActivity(it1)

                            // selecionou galeria (1)
                        } else if (which == 1) {

                            // abre a galeria
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            // encaminha a imgem selecionada
                            activityResultLauncher.launch(intent)

                        } else {
                            // fecha o "Pop up"
                            dialog.dismiss()
                        }
                    }
                })
                builder.show()
            }

        }

        // referência para o botão para submeter a imagem
        val btnSubmeter = findViewById<Button>(R.id.btnSub)
        btnSubmeter.setOnClickListener {
            // verifica se há alguma imagem selecionada
            if (!verificar) {
                Toast.makeText(
                    this@RestaurantesActivity, "Precisa de selecionar uma imagem", Toast.LENGTH_LONG
                ).show()
            } else {

                // trata a imagem para ser recebida pela API
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val imagePart = MultipartBody.Part.createFormData("imagem", file.name, requestFile)

                // chamada à API (POST) para submeter a imagem selecionada no respetivo resturante
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
                        // reecaminha para outra activity, neste caso para o Mapa activity
                        val it2 = Intent(this@RestaurantesActivity, MapaActivity::class.java)
                        startActivity(it2)
                    }

                    override fun onFailure(call: Call<ImageRest>, t: Throwable) {
                        Toast.makeText(
                            this@RestaurantesActivity,
                            "Houve um erro a submeter a Imagem",
                            Toast.LENGTH_LONG
                        ).show()
                        // reecaminha para outra activity, neste caso para o Mapa activity em caso de falha
                        val it3 = Intent(this@RestaurantesActivity, MapaActivity::class.java)
                        startActivity(it3)
                    }
                })
            }
        }

    }

    // recebe o resultado da seleção da galeria e trata a imagem para poder ser enviada para a API
    var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode != RESULT_CANCELED) {

                //confirma que o resultCode está bem e se forem recebidos dados
                if (result.resultCode == RESULT_OK && result.data != null) {
                    //recebe a URI da imagem da galeria
                    imageUri = result.data!!.data!!
                    // tratar a imagem para poder ser enviada para a API
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(imageUri, filePathColumn, null, null, null)
                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val imgDecodableString = cursor.getString(columnIndex)
                    cursor.close()
                    // converte em ficheiro
                    file = File(imgDecodableString)

                    // true = imagem "pronta" para enviar
                    verificar = true

                    // coloca a imagem capturda na câmara no imageView
                    val imageSelect = findViewById<ImageView>(R.id.imageSelect)
                    Glide.with(this@RestaurantesActivity).load(file).into(imageSelect)

                    // altera o texto para o utilizador ser informado que a foto está selecionada
                    val txtImgSelec = findViewById<TextView>(R.id.txtImgSelec)
                    val aux = "Imagem Selecionada"
                    txtImgSelec.text = aux
                }

            }
        }

    /**
     * função para permitir que seja possível voltar atrás na activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * função que irá processar os dados todos de cada restaurante
     */
    private fun processRestaurant() {


        // index do array que irá conter as imagens
        var i = 0

        // referências para as diferentes TextViews e ImageViews
        val txtDesc = findViewById<TextView>(R.id.txtDesc)
        val txtLocal = findViewById<TextView>(R.id.txtLocal)
        val txtContacto = findViewById<TextView>(R.id.txtContacto)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)
        val txtHorario = findViewById<TextView>(R.id.txtHorario)
        val imageview = findViewById<ImageView>(R.id.imageView)

        // referências para os botões que permitem ver as imagens (efeito rotatório)
        val btnDir = findViewById<ImageButton>(R.id.btnDir)
        val btnEsq = findViewById<ImageButton>(R.id.btnEsq)

        // define um valor à TextView
        txtDesc.text = restaurant?.description
        txtLocal.text = restaurant?.localization
        txtContacto.text = restaurant?.contact
        txtEmail.text = restaurant?.email
        txtHorario.text = restaurant?.Time

        // link para ir buscar as imagens onde será adicionado o path da imagem que está na BD

        if (restaurant?.images?.size != 0) {

            // carrega a foto atrés do URL
            Glide.with(this@RestaurantesActivity).load(constString(i)).fitCenter().into(imageview)
        }

        // tamanho do array que contém as imagens do restaurante
        var tam = restaurant?.images?.size

        // caso o restaurante não tenha imagens coloca uma imagem para
        // referência esse facto, se não irá carrega as imagens
        if (tam == 0) {
            // carrega a foto atrés do URL
            Glide.with(this).load(
                resources.getIdentifier("vazio", "drawable", this.packageName)
            ).into(imageview)
        } else {

            // descontar 1 ao tamanho do array por ser [0,1,2,3,...]
            if (tam != null) {
                tam -= 1
            }

            // se o i for menor que tamanho do array das imagens então incrementa
            // senão volta ao indice 0
            btnDir.setOnClickListener {
                if (i < tam!!) {
                    i++

                    // carrega a foto atrés do URL
                    Glide.with(this@RestaurantesActivity).load(constString(i)).fitCenter()
                        .into(imageview)
                } else {
                    i = 0

                    // carrega a foto atrés do URL
                    Glide.with(this@RestaurantesActivity).load(constString(i)).fitCenter()
                        .into(imageview)
                }
            }

            // se o i for diferente de 0 decrementa
            // senão volta ao indice 1 e apresenta a primeira imagem de indice 0
            btnEsq.setOnClickListener {
                if (i != 0) {
                    i--

                    // carrega a foto atrés do URL
                    Glide.with(this@RestaurantesActivity).load(constString(i)).fitCenter()
                        .into(imageview)

                } else {
                    if (tam != null) {
                        i = tam + 1

                        // carrega a foto atrés do URL
                        Glide.with(this@RestaurantesActivity).load(constString(tam)).fitCenter()
                            .into(imageview)
                    }
                }
            }
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

    /**
     * função que irá contruir a string do URL para as imagens
     * dos restaurantes
     */
    fun constString(tam: Int): String {
        // string para a contrução do caminha das imagens
        val base = "https://restaurants4you-api.azurewebsites.net/Fotos/"

        //constrói a string para carregar a foto
        val b = restaurant?.images?.get(tam)?.path
        val sb = StringBuilder()
        sb.append(base).append(b)
        return sb.toString()
    }
}