package com.application.pokemonvktest.dataclasses

data class DataPokemon(
    val id: Int,
    val name: String = "",
    val height: String = "",
    val weight: String = "",
    val sprites: DataSprite,
    val stats: List<DataListStats>
)

data class DataListStats(
    val base_stat: String = "",
    val stat: DataStat
)

data class DataStat(
    val name: String = ""
)

data class DataSprite(
    val front_default: String = ""
)
