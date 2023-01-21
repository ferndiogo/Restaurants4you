package com.dam.restaurants4you.activity

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import kotlin.properties.Delegates


class MapaActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private var token: String? = null
    private var list: List<Restaurant>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapa)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        token = loadToken()

        map = findViewById(R.id.mapa)

        val call = RetrofitInitializer().restaurantService().listRestaurants(token!!)
        call.enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(
                call: Call<List<Restaurant>>, response: Response<List<Restaurant>>
            ) {
                response.body().let {
                    list = it as List<Restaurant>
                }
                showMap()
                addAllMarkers()
                getLocation()
                //list?.get(0)?.let { println(it.id)

            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                Toast.makeText(
                    this@MapaActivity,
                    "Token inválido ou erro no servidor",
                    Toast.LENGTH_LONG
                )
                val it = Intent(this@MapaActivity, LoginActivity::class.java)
                startActivity(it)
            }

        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logOut -> {
                Toast.makeText(this@MapaActivity, "Logout com sucesso!", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.info -> {
                val it = Intent(this@MapaActivity, SobreActivity::class.java)
                startActivity(it)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addAllMarkers() {
        for (rt: Restaurant in list!!) {
            criarMarcador(rt.latitude, rt.longitude, rt.name, rt, token)
        }
    }

    private fun showMap() {
        // define apenas um mapa em ºtodo o programa
        Configuration.getInstance().setUserAgentValue(this.getPackageName())

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.zoomTo(17.0)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        map.setMultiTouchControls(true) // para poder fazer zoom com os dedos

        var compassOverlay = CompassOverlay(this, map)
        compassOverlay.enableCompass()
        map.overlays.add(compassOverlay)


        //chama a função que cria o marcador
        //criarMarcador(39.60068, -8.38967, "IPT", )


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

    private fun loadToken(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            R.string.Name_File_Token.toString(),
            MODE_PRIVATE
        )
        return sharedPreferences.getString("token", "").toString()
    }





}