package com.dam.restaurants4you.fragmentos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.dam.restaurants4you.R
import com.dam.restaurants4you.activity.RoleRActivity
import com.dam.restaurants4you.model.Restaurant
import com.dam.restaurants4you.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class FragDelRest : Fragment() {

    private var listName: List<String> = listOf()

    private var listId: List<String> = listOf()

    private lateinit var arrayAdapter: ArrayAdapter<String>

    private var idRt: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_rest_del, container, false)

        listRestsUser()

        val btnDel = view.findViewById<Button>(R.id.btnDel)
        btnDel.setOnClickListener {
            if (idRt != -1) {
                deleteRest(listId.get(idRt))
            } else {
                Toast.makeText(context, "Selecionar Restaurante!!", Toast.LENGTH_LONG).show()
            }
        }


        // Inflate the layout for this fragment
        return view
    }

    private fun deleteRest(idRt: String) {
        val call =
            RetrofitInitializer().restaurantService().deleteRestaurant(loadToken(), idRt.toInt())
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(context, "Restaurante Eliminado com sucesso!!", Toast.LENGTH_SHORT)
                    .show()
                this@FragDelRest.idRt = -1
                this@FragDelRest.listId = listOf()
                this@FragDelRest.listName = listOf()
                listRestsUser()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Erro no Servidor!!", Toast.LENGTH_SHORT).show()
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

    private fun listRestsUser() {
        val call =
            RetrofitInitializer().restaurantService().listRestaurantsUser(loadToken())
        call.enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(
                call: Call<List<Restaurant>>,
                response: Response<List<Restaurant>>
            ) {
                response.body().let {
                    val list = it as List<Restaurant>
                    for (rt: Restaurant in list) {
                        listId += rt.id.toString()
                        listName += rt.name
                    }
                    if (listId.isNullOrEmpty()) {
                        Toast.makeText(
                            context,
                            "Não tem nenhum restaurante registado!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val it = Intent(context, Fragmentos::class.java)
                        startActivity(it)
                    }
                    configSpinner()
                }
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Ocorreu com o Servidor",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }

    private fun configSpinner() {
        val spinner = view?.findViewById<Spinner>(R.id.dropdownDel)

        arrayAdapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                listName
            )
        }!!

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner?.adapter = arrayAdapter

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                idRt = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }
}
