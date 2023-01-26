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
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragEditRest : Fragment() {

    private var listName: List<String> = listOf()

    private var listRt: List<Restaurant> = listOf()

    private lateinit var arrayAdapter: ArrayAdapter<String>

    private var rt: Restaurant? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_rest_edit, container, false)

        listRestsUser()

        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        btnEdit.setOnClickListener {
            if (rt != null) {
                editRest()
            } else {
                Toast.makeText(context, "Selecionar Restaurante!!", Toast.LENGTH_LONG).show()
            }
        }

        // Inflate the layout for this fragment
        return view
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
                        listRt += rt
                        listName += rt.name
                    }
                    if (listRt.isNullOrEmpty()) {
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

    private fun loadToken(): String {
        val sharedPreferences = this.requireActivity()
            .getSharedPreferences(R.string.Name_File_Token.toString(), Context.MODE_PRIVATE)

        return sharedPreferences.getString("token", "").toString()
    }

    private fun configSpinner() {
        val spinner = view?.findViewById<Spinner>(R.id.dropdownEdit)

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
                rt = listRt.get(p2)
                processRest()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    private fun processRest(){
        val nome = view?.findViewById<TextView>(R.id.txtRest)
        val desc = view?.findViewById<TextView>(R.id.txtDesc)
        val morada = view?.findViewById<TextView>(R.id.txtMorada)
        val contact = view?.findViewById<TextView>(R.id.txtContacto)
        val email = view?.findViewById<TextView>(R.id.txtEmail)
        val time = view?.findViewById<TextView>(R.id.txtHorario)
        val lat = view?.findViewById<TextView>(R.id.txtLat)
        val lon = view?.findViewById<TextView>(R.id.txtLong)

        nome?.text = rt?.name
        desc?.text = rt?.description
        morada?.text = rt?.localization
        contact?.text = rt?.contact
        email?.text = rt?.email
        time?.text = rt?.Time
        lat?.text = rt?.latitude
        lon?.text = rt?.longitude

    }

    private fun editRest(){
        val nome = view?.findViewById<TextView>(R.id.txtRest)
        val desc = view?.findViewById<TextView>(R.id.txtDesc)
        val morada = view?.findViewById<TextView>(R.id.txtMorada)
        val contact = view?.findViewById<TextView>(R.id.txtContacto)
        val email = view?.findViewById<TextView>(R.id.txtEmail)
        val time = view?.findViewById<TextView>(R.id.txtHorario)
        val lat = view?.findViewById<TextView>(R.id.txtLat)
        val lon = view?.findViewById<TextView>(R.id.txtLong)

        val idBd: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), rt?.id.toString())

        val nameBd: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), nome?.text.toString())

        val descBd: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), desc?.text.toString())

        val moradaBd: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), morada?.text.toString())

        val contactoBd: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), contact?.text.toString())

        val emailBd: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), email?.text.toString())

        val horarioBd: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), time?.text.toString())

        val latitudeBd: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), lat?.text.toString())

        val longitudeBd: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), lon?.text.toString())

        val userFkBdBd: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), rt?.userFK.toString())

        val call = rt?.id?.let {
            RetrofitInitializer().restaurantService().editRestaurant(loadToken(),idBd, nameBd, descBd,moradaBd,contactoBd,emailBd,horarioBd,latitudeBd,longitudeBd,userFkBdBd,
                rt!!.id!!
            )
        }

        call?.enqueue(object: Callback<Restaurant>{
            override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                response.body().let {
                    Toast.makeText(context, (it as Restaurant).name+" editado com sucesso!", Toast.LENGTH_SHORT).show()
                    val it = Intent(context, RoleRActivity::class.java)
                    startActivity(it)
                }
            }

            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                Toast.makeText(context,"Ocorreu um erro com o Servidor!", Toast.LENGTH_SHORT).show()
            }

        })

    }

}