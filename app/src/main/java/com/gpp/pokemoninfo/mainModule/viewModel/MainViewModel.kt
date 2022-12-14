package com.gpp.pokemoninfo.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gpp.pokemoninfo.entities.Pokemon
import com.gpp.pokemoninfo.mainModule.model.MainInteractor

class MainViewModel: ViewModel() {
    private var pokemonList: MutableList<Pokemon>
    private var interactor: MainInteractor

    init {
        pokemonList = mutableListOf()
        interactor = MainInteractor()
    }


    private val pokemons: MutableLiveData<MutableList<Pokemon>> by lazy {
        MutableLiveData<MutableList<Pokemon>>().also {
            loadPokemons()
        }
    }

    fun getPokemons(): LiveData<MutableList<Pokemon>> {
        return pokemons
    }



    private fun loadPokemons(){

        interactor.getPokemons {
            pokemons.value = it
            pokemonList = it
        }
    }

    fun addPokemon(pokemon:Pokemon){
        pokemonList.add(pokemon)
    }


    fun returnPokemonList(): MutableList<Pokemon> {
        return pokemonList
    }



}