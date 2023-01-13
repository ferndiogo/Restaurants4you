package com.dam.restaurants4you.fragmentos

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.dam.restaurants4you.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay


class FragmentoMapa : Fragment() {

    private lateinit var root: View
    private lateinit var map : MapView
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        root = inflater.inflate(R.layout.mapa, container, false)

        showMap()

        return root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.mapa, container, false)
    }


    private fun showMap() {




        map = MapView(activity)

        Configuration.getInstance().setUserAgentValue(this.activity?.getPackageName())

        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.zoomTo(17.0)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        map.setMultiTouchControls(true) // para poder fazer zoom com os dedos

        var compassOverlay = CompassOverlay(this.activity, map)
        compassOverlay.enableCompass()
        map.overlays.add(compassOverlay)

        //chama a função que cria o marcador
        criarMarcador(39.60068, -8.38967, "IPT", map)

        (root.findViewById(R.id.mapa) as LinearLayout).addView(map)

    }

    override fun onPause() {
        super.onPause()
        showMap()
    }

    override fun onResume() {
        super.onResume()
        showMap()
    }
    

    /**
     * função para coletar as permissões do utilizador
     */
    private fun requestPermissionsIfNecessary(permissions: Array<out String>) {
        val permissionsToRequest = ArrayList<String>();
        permissions.forEach { permission ->
            if (this.activity?.let {
                    ContextCompat.checkSelfPermission(
                        it, permission
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                // a permissão não é concedida
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size > 0) {
            this.activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    permissionsToRequest.toArray(arrayOf<String>()),
                    REQUEST_PERMISSIONS_REQUEST_CODE
                )
            };
        }
    }

    /**
     * função que cria um marcador com as coordenadas passadas por parametro
     */
    private fun criarMarcador(latitude: Double, logintude: Double, local: String, map: MapView) {

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
        //startMarker.infoWindow = MarcadorActivity(map, this.activity, local)
        // adiciona o marcador ao Mapa
        map.overlays.add(startMarker)

        Handler(Looper.getMainLooper()).postDelayed({
            map.controller.setCenter(point)
        }, 1000) //espera um segundo para centrar o mapa
    }


}