package com.gpp.pokemoninfo.mainModule

import android.os.Bundle
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gpp.pokemoninfo.R
import com.gpp.pokemoninfo.databinding.ActivityMainBinding
import com.gpp.pokemoninfo.entities.Pokemon
import com.gpp.pokemoninfo.mainModule.adapter.OnClickListener
import com.gpp.pokemoninfo.mainModule.adapter.PokemonAdapter
import com.gpp.pokemoninfo.mainModule.viewModel.DetailPokemonViewModel
import com.gpp.pokemoninfo.mainModule.viewModel.MainViewModel


class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: PokemonAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mDetailPokemonViewModel: DetailPokemonViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupViewModel()
        setupRecyclerView()

    }



    private fun setupRecyclerView() {
        mAdapter = PokemonAdapter(mutableListOf(), this)
        mLayoutManager = LinearLayoutManager(this)


        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
    }

    private fun setupViewModel() {
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mMainViewModel.getPokemons().observe(this, { pokemons ->
            mAdapter.setPokemons(pokemons)
        })

        mDetailPokemonViewModel = ViewModelProvider(this).get(DetailPokemonViewModel::class.java)

    }


    private fun launchDetailFragment(pokemon: Pokemon = Pokemon()) {

        mDetailPokemonViewModel.setPokemonSelected(pokemon)

        val fragment = PokemonFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()


    }



    /**
     * OnClickListener
     */
    override fun onClick(pokemon: Pokemon) {

        launchDetailFragment(pokemon)
    }
}