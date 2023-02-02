package com.dam.restaurants4you.fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dam.restaurants4you.R
import com.dam.restaurants4you.activity.RoleRActivity
import com.dam.restaurants4you.model.Restaurant
import com.dam.restaurants4you.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        //lista os restaurants na dropdown
        listRestsUser()

        // referência para o botão de eliminar
        val btnDel = view.findViewById<Button>(R.id.btnDel)
        btnDel.setOnClickListener {
            if (idRt != -1) {
                //se o id de restaurante for valido elimina
                deleteRest(listId.get(idRt))
            } else {
                Toast.makeText(context, "Selecionar Restaurante!!", Toast.LENGTH_LONG).show()
            }
        }


        // "inflate" o layout deste fragmento
        return view
    }

    /**
     * Elimina um restaurante da API
     */
    private fun deleteRest(idRt: String) {
        // chamada à API (DELETE) para eliminar um resturante
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
                val it = Intent(context, RoleRActivity::class.java)
                startActivity(it)
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

    /**
     * lista os restaurantes de um utilizador na dropdown
     */
    private fun listRestsUser() {
        // chamada à API (GET) para listar os resturantes presentes de um determinado utilizador
        val call =
            RetrofitInitializer().restaurantService().listRestaurantsUser(loadToken())
        call.enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(
                call: Call<List<Restaurant>>,
                response: Response<List<Restaurant>>
            ) {
                response.body().let {
                    val list = it as List<Restaurant>
                    //faz duas listas com os IDs e os nomes dos restaurantes
                    for (rt: Restaurant in list) {
                        listId += rt.id.toString()
                        listName += rt.name
                    }
                    //no caso de a lista de restaurantes ser vazia mostra um Toast
                    if (listId.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Não tem nenhum restaurante registado!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val it = Intent(context, Fragmentos::class.java)
                        startActivity(it)
                    }
                    //configura a dropdown
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

    /**
     * configura o spinner
     */
    private fun configSpinner() {
        // referência para a dropdown dos restaurantes
        val spinner = view?.findViewById<Spinner>(R.id.dropdownDel)

        //configuração do spinner para mostrar os nomes dos restaurantes
        arrayAdapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                listName
            )
        }!!
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = arrayAdapter

        //adicionar evento onitemselected ao spinner
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //quando um item do spinner é selecionado vai guardar o id
                idRt = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }
}
