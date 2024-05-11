package com.application.pokemonvktest.dataclasses

data class DataListPokemons(
    val results: List<DataFromListPokemons>
)

data class DataFromListPokemons(
    val name: String = "",
    val url: String = ""
)
