package com.dam.restaurants4you.fragmentos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.dam.restaurants4you.R
import com.dam.restaurants4you.model.Restaurant
import com.dam.restaurants4you.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragAddRest : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_rest_add, container, false)

        val btnAdd: Button = view?.findViewById(R.id.btnAdd) as Button
        btnAdd.setOnClickListener({ addRestaurante() })
        // Inflate the layout for this fragment
        return view
    }

    private fun addRestaurante() {

        val txtRest: EditText = view?.findViewById(R.id.txtRest) as EditText
        val txtDesc: EditText = view?.findViewById(R.id.txtDesc) as EditText
        val txtMorada: EditText = view?.findViewById(R.id.txtMorada) as EditText
        val txtContacto: EditText = view?.findViewById(R.id.txtContacto) as EditText
        val txtEmail: EditText = view?.findViewById(R.id.txtEmail) as EditText
        val txtHorario: EditText = view?.findViewById(R.id.txtHorario) as EditText
        val txtLat: EditText = view?.findViewById(R.id.txtLat) as EditText
        val txtLong: EditText = view?.findViewById(R.id.txtLong) as EditText

        val titulo = txtRest.text.toString()
        val descricao = txtDesc.text.toString()
        val morada = txtMorada.text.toString()
        val contacto = txtContacto.text.toString()
        val email = txtEmail.text.toString()
        val horario = txtHorario.text.toString()
        val latitude = txtLat.text.toString()
        val longitude = txtLong.text.toString()


        val call = RetrofitInitializer().restaurantService().addRestaurant(loadToken(), titulo, descricao, morada,
            contacto, email, horario, latitude, longitude)

        call.enqueue(object : Callback<Restaurant> {
            override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
            }

            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    /**
     * função que irá ler o token guardado em memória
     */
    private fun loadToken(): String {

        val sharedPreferences = this.requireActivity()
            .getSharedPreferences(R.string.Name_File_Token.toString(), Context.MODE_PRIVATE)

        return sharedPreferences.getString("token", "").toString()
    }
}


