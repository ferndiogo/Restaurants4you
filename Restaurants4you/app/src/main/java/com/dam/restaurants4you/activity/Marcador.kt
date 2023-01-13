package com.dam.restaurants4you.activity

import android.content.Intent
import android.widget.Button
import android.widget.TextView
import com.dam.restaurants4you.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class Marcador: InfoWindow {

    private var parent: MapaActivity
    private var text: String

    constructor(
        mapView: MapView, parent: MapaActivity, text: String
    ) : super(R.layout.janela_info, mapView) {
        this.parent = parent
        this.text = text
    }

    override fun onOpen(item: Any?) {
        // fecha todas as janelas no início
        closeAllInfoWindowsOn(mapView)

        // acesso ao botão e textView
        val myHelloButton = mView.findViewById<Button>(R.id.HelloBT)
        val myTextView = mView.findViewById<TextView>(R.id.textView)

        // define um valor à TextView
        myTextView.text = text

        // define o que acontece quando se clica no botão
        myHelloButton.setOnClickListener {
            //Toast.makeText(parent, "Hello $text", Toast.LENGTH_SHORT).show()
            val act = Intent(parent, RestaurantesActivity::class.java)
            parent.startActivity(act)
        }

        // quando um clique na área da janela, ela fecha
        mView.setOnClickListener{
            close()
        }
    }

    override fun onClose() {
        // TODO("Not yet implemented")
    }
}