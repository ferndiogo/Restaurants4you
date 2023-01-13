package com.dam.restaurants4you.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
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
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapaActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2

    //Marcado para a minha localização
    private val myMarker: Overlay? = null


    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private lateinit var map: MapView
    private var token: String? = null
    private var list: List<Restaurant>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapa)

        token = intent.getStringExtra("token").toString()

        map = findViewById(R.id.mapa)

        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )

        if (token.isNullOrBlank()) {
            val it = Intent(this@MapaActivity, LoginActivity::class.java)
            startActivity(it)
        } else {
            val call = RetrofitInitializer().restaurantService().listRestaurants(token!!)
            println("Iam Here")
            call.enqueue(object : Callback<List<Restaurant>> {
                override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>
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
                    Toast.makeText(this@MapaActivity,"Token inválido ou erro no servidor", Toast.LENGTH_LONG)
                    val it = Intent(this@MapaActivity, LoginActivity::class.java)
                    startActivity(it)
                }

            })

        }
    }

    private fun addAllMarkers(){
        for (rt: Restaurant in list!!){
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
     * função para coletar as permissões do utilizador
     */
    private fun requestPermissionsIfNecessary(permissions: Array<out String>) {
        val permissionsToRequest = ArrayList<String>();
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // a permissão não é concedida
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(arrayOf<String>()),
                REQUEST_PERMISSIONS_REQUEST_CODE
            );
        }
    }

    /**
     * função que cria um marcador com as coordenadas passadas por parametro
     */
    private fun criarMarcador(latitude: Double, logintude: Double, local: String, rt: Restaurant, token : String?) {

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

        Handler(Looper.getMainLooper()).postDelayed({
            map.controller.setCenter(point)
        }, 1000) //espera um segundo para centrar o mapa
    }


    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }
    override fun onLocationChanged(location: Location) {
        //tvGpsLocation = findViewById(R.id.textView)
        //tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude

        //criarMarcador(location.latitude,location.longitude,"EU", null)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requestPermissionsIfNecessary(permissions)
    }

}