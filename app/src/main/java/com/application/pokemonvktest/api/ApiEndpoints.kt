package com.application.pokemonvktest.api

import com.application.pokemonvktest.dataclasses.DataListPokemons
import com.application.pokemonvktest.dataclasses.DataPokemon
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiEndpoints {
    @GET("pokemon")
    suspend fun getListPokemonsData(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataListPokemons

    @GET
    suspend fun getPokemonsData(
        @Url url: String
    ): DataPokemon
}