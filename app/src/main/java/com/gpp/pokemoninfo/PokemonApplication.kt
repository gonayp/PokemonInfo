package com.gpp.pokemoninfo

import android.app.Application
import com.gpp.pokemoninfo.database.PokemonAPI

class PokemonApplication : Application() {
    companion object{
        lateinit var pokemonAPI: PokemonAPI
    }

    override fun onCreate() {
        super.onCreate()

        //Volley
        pokemonAPI = PokemonAPI.getInstance(this)
    }
}