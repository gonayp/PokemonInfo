package com.gpp.pokemoninfo

data class Pokemon(var id: Long,
                   var name: String,
                   var baseExperience: Int,
                   var height: Double,
                   var weight: Double,
                   var urlPhoto: String)
