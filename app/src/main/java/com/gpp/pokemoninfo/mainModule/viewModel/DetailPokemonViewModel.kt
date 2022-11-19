package com.gpp.pokemoninfo.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gpp.pokemoninfo.entities.Pokemon

class DetailPokemonViewModel: ViewModel() {
    private val pokemonSelected = MutableLiveData<Pokemon>()
    private val result = MutableLiveData<Any>()



    fun setPokemonSelected(pokemon: Pokemon){
        pokemonSelected.value = pokemon
    }

    fun getPokemonSelected(): LiveData<Pokemon> {
        return pokemonSelected
    }



    fun setResult(value: Any){
        result.value = value
    }

    fun getResult(): LiveData<Any> {
        return result
    }


}