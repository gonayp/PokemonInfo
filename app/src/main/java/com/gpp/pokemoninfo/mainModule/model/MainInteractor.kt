package com.gpp.pokemoninfo.mainModule.model

import android.util.Log
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

            val next_url: String = response.getString("next")
            val jsonList = response.optJSONArray("results")?.toString()
            //Log.i("resultado",jsonList.toString())
            if (jsonList != null) {

                var restoPokemonList = mutableListOf<Pokemon>()

                if(next_url.compareTo("null")!= 0){
                    //Log.i("next_url",next_url)
                    restoPokemonList = getSiguientes(next_url,restoPokemonList)
                    //Log.i("Pokemons prev",restoPokemonList.toString())
                }

                val mutableListType = object : TypeToken<MutableList<Pokemon>>() {}.type
                pokemonList = Gson().fromJson(jsonList, mutableListType)

                pokemonList.addAll(restoPokemonList)
                Log.i("Pokemons",pokemonList.toString())

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

    private fun getSiguientes(url: String, pokemonList: MutableList<Pokemon>): MutableList<Pokemon> {

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val next_url : String = response.getString("next")
            val jsonList = response.optJSONArray("results")?.toString()
            if (jsonList != null) {
                var restoPokemonList = mutableListOf<Pokemon>()

                if(next_url.compareTo("null")!= 0){
                    Log.i("next_url",next_url)
                    val mutableListType = object : TypeToken<MutableList<Pokemon>>() {}.type
                    restoPokemonList = getSiguientes(next_url,Gson().fromJson(jsonList, mutableListType))
                }

                pokemonList.addAll(restoPokemonList)
            }
        },{
            it.printStackTrace()
        })
        PokemonApplication.pokemonAPI.addToRequestQueue(jsonObjectRequest)
        //Log.i("Pokemons",pokemonList.toString())
        return pokemonList
    }

}