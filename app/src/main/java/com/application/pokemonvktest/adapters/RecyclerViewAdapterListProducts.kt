package com.application.pokemonvktest.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.application.pokemonvktest.R
import com.application.pokemonvktest.dataclasses.DataPokemon
import com.application.pokemonvktest.interfaces.OnItemClickListenerForListPokemons
import com.squareup.picasso.Picasso
import java.util.Locale

class RecyclerViewAdapterListProducts(
    private var listPokemonData: MutableList<DataPokemon>,
    private val itemClickListener: OnItemClickListenerForListPokemons
) :
    RecyclerView.Adapter<RecyclerViewAdapterListProducts.ViewHolder>() {
    private lateinit var contextAdapter: Context

    class ViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return listPokemonData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contextAdapter = parent.context
        val cv = LayoutInflater.from(contextAdapter)
            .inflate(R.layout.product_card, parent, false) as CardView
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        val pokemonName: TextView = cardView.findViewById(R.id.text_view_name)
        val productImage: ImageView = cardView.findViewById(R.id.image_for_card)

        Picasso.get().load(listPokemonData[position].sprites.front_default).into(productImage)

        pokemonName.text = listPokemonData[position].name.capitalizeEachWord()

        cardView.setOnClickListener {
            itemClickListener.onItemClickPokemon(position)
        }
    }

    private fun String.capitalizeEachWord(): String {
        return this.split(" ").joinToString(" ") { it ->
            it.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newDataList: List<DataPokemon>) {
        val uniqueNewData =
            newDataList.filter { newData -> !listPokemonData.any { it.id == newData.id } }
        listPokemonData.addAll(uniqueNewData)
        notifyDataSetChanged()
    }

}
