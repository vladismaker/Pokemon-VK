package com.application.pokemonvktest.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.pokemonvktest.adapters.RecyclerViewAdapterStats
import com.application.pokemonvktest.databinding.FragmentFullDataPokemonBinding
import com.application.pokemonvktest.dataclasses.DataListStats
import com.application.pokemonvktest.viewModels.ListPokemonsViewModel
import com.squareup.picasso.Picasso
import java.util.Locale

class FullDataPokemonFragment : Fragment() {
    private var _binding: FragmentFullDataPokemonBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ListPokemonsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullDataPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ListPokemonsViewModel::class.java]

        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewModel.liveDataFullDataPokemon.observe(viewLifecycleOwner) {
            setScreen(it.name.capitalizeEachWord(), it.height, it.weight, it.sprites.front_default)
            setRecyclerView(it.stats)
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

    private fun setScreen(name:String, height:String, weight:String, image:String){
        binding.textViewName.text = name
        val stringHeight = "Height: $height decimetres"
        binding.textViewHeight.text = stringHeight
        val stringWeight = "Weight: $weight hectograms"
        binding.textViewWeight.text = stringWeight

        Picasso.get().load(image).into(binding.imageForFull)
    }

    private fun setRecyclerView(listDataStats: List<DataListStats>){
        val adapter = RecyclerViewAdapterStats(listDataStats)

        binding.recyclerViewStats.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(requireActivity())
            this.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}