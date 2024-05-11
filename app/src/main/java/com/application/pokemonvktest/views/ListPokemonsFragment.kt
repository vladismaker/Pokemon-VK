package com.application.pokemonvktest.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.application.pokemonvktest.R
import com.application.pokemonvktest.adapters.RecyclerViewAdapterListProducts
import com.application.pokemonvktest.databinding.FragmentListPokemonsBinding
import com.application.pokemonvktest.dataclasses.DataPokemon
import com.application.pokemonvktest.interfaces.OnItemClickListenerForListPokemons
import com.application.pokemonvktest.viewModels.ListPokemonsViewModel


class ListPokemonsFragment : Fragment(), OnItemClickListenerForListPokemons {
    private var _binding: FragmentListPokemonsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ListPokemonsViewModel
    private var list:MutableList<DataPokemon> = mutableListOf()
    private lateinit var adapterListProduct: RecyclerViewAdapterListProducts
    private var adapterInit: Boolean = false
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListPokemonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ListPokemonsViewModel::class.java]

        viewModel.liveDataArrayPokemons.observe(viewLifecycleOwner) {
            val uniqueItems = it.filter { newItem ->
                list.none { existingItem -> existingItem.id == newItem.id }
            }
            list.addAll(uniqueItems)

            Log.d("debug", "Обновились данные в списке")
            setRecyclerViewListProducts(list)
        }

        viewModel.liveDataToast.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.liveDataSelectedPosition.observe(viewLifecycleOwner) { position ->
            // Прокрутите RecyclerView к выбранной позиции
            binding.recyclerView.layoutManager?.scrollToPosition(position)
        }

        viewModel.liveDataProgressBar.observe(viewLifecycleOwner) {
            if (it){
                binding.lottieProgressBar.visibility = View.VISIBLE
            }else{
                binding.lottieProgressBar.visibility = View.GONE
                isLoading = false
            }
        }

        Log.d("debug", "onViewCreated")
        Log.d("debug", "viewModel.count:${viewModel.count}")
        if (viewModel.count==0){
            viewModel.getTwentyPokemons()
        }
    }

    private fun setRecyclerViewListProducts(listProducts:MutableList<DataPokemon>){

        if (!adapterInit) {
            //адаптера еще нет
            Log.d("debug", "адаптера еще нет")
            adapterListProduct = RecyclerViewAdapterListProducts(listProducts,this)
            adapterInit = true

            binding.recyclerView.apply {
                isNestedScrollingEnabled = false
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                this.adapter = adapterListProduct

                addOnScrollListener(object : RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val layoutManager = layoutManager as StaggeredGridLayoutManager

                        val totalItemCount = layoutManager.itemCount
                        val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)

                        // Проверяем, долистали ли до конца списка
                        if (!isLoading && lastVisibleItemPositions.isNotEmpty()) {
                            val lastVisibleItem = lastVisibleItemPositions.maxOrNull() ?: 0
                            if (lastVisibleItem == totalItemCount - 1) {
                                isLoading = true

                                viewModel.updateListPokemons(totalItemCount)
                            }
                        }
                    }
                })
            }
        } else {
            // Если адаптер уже инициализирован,обновляем данные
            Log.d("debug", "адаптер уже инициализирован,обновляем данные")
            Log.d("debug", "listProducts:$listProducts")
            adapterListProduct.updateData(listProducts)
        }

    }

    override fun onItemClickPokemon(position: Int) {
        //Переход на экран товара
        viewModel.setFullDataPokemon(list[position])
        adapterInit = false
        viewModel.setSelectedPosition(position)
        //viewModel.count+=20
        viewModel.liveDataFullDataPokemon.observe(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_main, FullDataPokemonFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()

        Log.d("debug", "onStart()")
    }

    override fun onResume() {
        super.onResume()

        Log.d("debug", "onResume()")


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}