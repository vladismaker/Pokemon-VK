package com.application.pokemonvktest.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.pokemonvktest.App
import com.application.pokemonvktest.dataclasses.DataPokemon
import com.application.pokemonvktest.models.RepositoryImpl
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListPokemonsViewModel:ViewModel() {
    val liveDataArrayPokemons = MutableLiveData<MutableList<DataPokemon>>()
    val liveDataToast = MutableLiveData<String>()
    val liveDataProgressBar = MutableLiveData<Boolean>()
    val liveDataSelectedPosition = MutableLiveData<Int>()
    val liveDataFullDataPokemon = MutableLiveData<DataPokemon>()
    var count = 0
    private val repositoryForListPokemons = RepositoryImpl.getInstance()

    fun updateListPokemons(totalItemCount:Int){
        val list = liveDataArrayPokemons.value

        if (list != null){
            if (list.isNotEmpty()){
                if (count!=totalItemCount){
                    count = totalItemCount
                    getTwentyPokemons()
                }
            }else{
                getTwentyPokemons()
            }
        }else{
            getTwentyPokemons()
        }
    }

    fun getTwentyPokemons() {
        //Проверка на интернет и запрос
        App.ioScope.launch {
            val requestInternet = async { repositoryForListPokemons.checkInternet() }.await()
            if (requestInternet){
                liveDataProgressBar.postValue(true)
                val requestWorkplace = async { repositoryForListPokemons.getDataRequestRetrofit(count) }.await()
                liveDataProgressBar.postValue(false)

                if (requestWorkplace.first==200){
                    liveDataArrayPokemons.postValue(requestWorkplace.second)
                }else{
                    liveDataToast.postValue("Ошибка запроса")
                }
            }else{
                liveDataToast.postValue("Отсутствует интернет")
            }
        }
    }

    fun setFullDataPokemon(dataProduct: DataPokemon) {
        liveDataFullDataPokemon.value = dataProduct
    }

    fun setSelectedPosition(position: Int){
        liveDataSelectedPosition.value = position
    }
}