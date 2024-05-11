package com.application.pokemonvktest.models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.application.pokemonvktest.App
import com.application.pokemonvktest.api.ApiEndpoints
import com.application.pokemonvktest.api.RetroFitInstance
import com.application.pokemonvktest.dataclasses.DataPokemon
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class RepositoryImpl {
    companion object {
        private var INSTANCE: RepositoryImpl? = null

        fun getInstance(): RepositoryImpl = INSTANCE ?: kotlin.run {
            INSTANCE = RepositoryImpl()
            INSTANCE!!
        }
    }

    suspend fun getDataRequestRetrofit(offset: Int): Pair<Int, MutableList<DataPokemon>> {
        val pokemonsCalls = RetroFitInstance.getInstance().create(ApiEndpoints::class.java)

        return try {
            val limit = 20
            val listPokemons = pokemonsCalls.getListPokemonsData(offset, limit)

            val listDataPokemons: MutableList<DataPokemon> = mutableListOf()
            val deferredList = mutableListOf<Deferred<DataPokemon>>()
            coroutineScope {

                for (result in listPokemons.results) {
                    deferredList.add(async(Dispatchers.IO) {
                        getDataRequestFullRetrofit(result.url)
                    })
                }
            }

            // Дожидаемся завершения всех запросов и добавляем результаты в список
            for (deferred in deferredList) {
                listDataPokemons.add(deferred.await())
            }

            Pair(200, listDataPokemons)
        } catch (e: Exception) {
            Log.e("debug", "error:${e.message}")
            Pair(404, mutableListOf())
        }
    }

    private suspend fun getDataRequestFullRetrofit(pokemonUrl: String): DataPokemon {
        val pokemonsCalls = RetroFitInstance.getInstance().create(ApiEndpoints::class.java)

        try {
            val oneDataPokemon = pokemonsCalls.getPokemonsData(pokemonUrl)
            Log.d("debug", "oneDataPokemon:$oneDataPokemon")
            return oneDataPokemon

        } catch (e: Exception) {
            throw e
        }
    }

    fun checkInternet(): Boolean {
        val connectivityManager =
            App.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNet = connectivityManager.activeNetwork
        if (activeNet != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNet)
            return networkCapabilities != null && networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
        }

        return false
    }
}