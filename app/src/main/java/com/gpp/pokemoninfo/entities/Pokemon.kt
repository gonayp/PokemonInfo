package com.gpp.pokemoninfo.entities


data class Pokemon(var id: Long = 0,
                   var name: String = "",
                   var baseExperience: Int = 0,
                   var height: Int = 0,
                   var weight: Int = 0,
                   var url: String = "",
                   var photoURL: String = "") {


}
