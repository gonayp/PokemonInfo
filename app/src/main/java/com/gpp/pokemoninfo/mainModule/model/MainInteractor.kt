package com.gpp.pokemoninfo.mainModule.model

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gpp.pokemoninfo.PokemonApplication
import com.gpp.pokemoninfo.entities.Pokemon

class MainInteractor {


    fun getPokemons(callback: (MutableList<Pokemon>) -> Unit){
        val url = "https://pokeapi.co/api/v2/pokemon"

        var pokemonList = mutableListOf<Pokemon>()

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->

            val jsonList = response.optJSONArray("results")?.toString()
            if (jsonList != null) {

                val mutableListType = object : TypeToken<MutableList<Pokemon>>() {}.type
                pokemonList = Gson().fromJson(jsonList, mutableListType)

                callback(pokemonList)
                return@JsonObjectRequest
            }


            callback(pokemonList)
        },{
            it.printStackTrace()
            callback(pokemonList)
        })

        PokemonApplication.pokemonAPI.addToRequestQueue(jsonObjectRequest)
    }




}