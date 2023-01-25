package com.dam.restaurants4you.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dam.restaurants4you.R
import com.dam.restaurants4you.model.Restaurant
import com.dam.restaurants4you.retrofit.RetrofitInitializer
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapaActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private var token: String? = null
    private var list: List<Restaurant>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapa)

        // coloca a action bar visível
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // lê o token guardado no dispositivo
        token = loadToken()

        // referência para o mapa
        map = findViewById(R.id.mapa)

        // faz uma chamada (POST) à API com o token para obter todos os restaurantes
        val call = RetrofitInitializer().restaurantService().listRestaurants(token!!)
        call.enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(
                call: Call<List<Restaurant>>, response: Response<List<Restaurant>>
            ) {
                response.body().let {
                    // guarda todos os restaurantes nma lista
                    list = it as List<Restaurant>
                }
                // chama a função que mostra o mapa
                showMap()
                // chama a função que irá criar os marcadores no mapa
                addAllMarkers()
                // chama a função para obter a localização real do dispositivo
                getLocation()
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                Toast.makeText(
                    this@MapaActivity,
                    "Token inválido ou erro no servidor",
                    Toast.LENGTH_LONG
                )
                // reecaminha para outra activity, neste caso para o Login activity
                val it = Intent(this@MapaActivity, LoginActivity::class.java)
                startActivity(it)
            }

        })

    }

    /**
     * função para permitir que seja possível voltar atrás na activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * função para mostrar a action bar personalizada
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return true
    }

    /**
     * função para atribuir funções ao clicar nos diferentes item da action bar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logOut -> {
                logout()
                return true
            }
            R.id.info -> {
                // reecaminha para outra activity, neste caso para o Sobre activity
                val it = Intent(this@MapaActivity, SobreActivity::class.java)
                startActivity(it)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout(){
        val sharedPref: SharedPreferences = this.getSharedPreferences(
            R.string.Name_File_Token.toString(),
            MODE_PRIVATE
        )
        val edit: SharedPreferences.Editor = sharedPref.edit()
        edit.putString("token", "")
        edit.apply()
        edit.commit()
        val inte = Intent(this,LoginActivity::class.java)
        startActivity(inte)
        Toast.makeText(this, "Logout com sucesso!", Toast.LENGTH_LONG).show()
    }

    /**
     * função que irá chamar outra função para criar os marcadores no mapa
     */
    private fun addAllMarkers() {
        for (rt: Restaurant in list!!) {
            criarMarcador(rt.latitude, rt.longitude, rt.name, rt, token)
        }
    }

    /**
     * função para tornar o mapa visível
     */
    private fun showMap() {
        // define apenas um mapa em ºtodo o programa
        Configuration.getInstance().setUserAgentValue(this.getPackageName())

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.zoomTo(17.0)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        // capacidade para ser possível fazer zoom com os dedos
        map.setMultiTouchControls(true)

        var compassOverlay = CompassOverlay(this, map)
        compassOverlay.enableCompass()
        map.overlays.add(compassOverlay)

    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    /**
     * função que cria um marcador com as coordenadas passadas por parametro
     */
    private fun criarMarcador(
        latitude: Double,
        logintude: Double,
        local: String,
        rt: Restaurant,
        token: String?
    ) {

        // define um ponto no mapa
        // Instituto Politécnico de Tomar
        var point = GeoPoint(latitude, logintude)
        // define um marcador num ponto
        var startMarker = Marker(map)
        startMarker.setIcon(getResources().getDrawable(R.drawable.marcador));
        // atribui o ponto ao marcador
        startMarker.position = point
        // diz ao mapa que o marcador deve ser desenhado no centro da tela
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        // define o conteúdo da infoWindow
        startMarker.infoWindow = Marcador(map, this, local, rt, token)
        // adiciona o marcador ao Mapa
        map.overlays.add(startMarker)
    }


    /**
     * função para obter a localização atual
     */
    private fun getLocation() {
        val myLocationoverlay = MyLocationNewOverlay(map)
        myLocationoverlay.enableFollowLocation()
        myLocationoverlay.enableMyLocation()
        map.getOverlays().add(myLocationoverlay)
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





}