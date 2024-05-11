package com.application.pokemonvktest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.application.pokemonvktest.R
import com.application.pokemonvktest.dataclasses.DataListStats
import java.util.Locale

class RecyclerViewAdapterStats(
    private var listDataStats: List<DataListStats>,
) :
    RecyclerView.Adapter<RecyclerViewAdapterStats.ViewHolder>() {
    private lateinit var contextAdapter: Context

    class ViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun getItemCount(): Int {
        return listDataStats.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contextAdapter = parent.context
        val cv = LayoutInflater.from(contextAdapter)
            .inflate(R.layout.stats_card, parent, false) as CardView
        return ViewHolder(cv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView

        val pokemonName: TextView = cardView.findViewById(R.id.text_view_stats)

        val stringStat =
            "${listDataStats[position].stat.name.capitalizeEachWord()}: ${listDataStats[position].base_stat}"
        pokemonName.text = stringStat
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
}
