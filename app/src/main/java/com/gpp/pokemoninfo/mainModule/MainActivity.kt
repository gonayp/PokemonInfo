package com.gpp.pokemoninfo.mainModule

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gpp.pokemoninfo.R
import com.gpp.pokemoninfo.databinding.ActivityMainBinding
import com.gpp.pokemoninfo.entities.Pokemon
import com.gpp.pokemoninfo.mainModule.adapter.OnClickListener
import com.gpp.pokemoninfo.mainModule.adapter.PokemonAdapter
import com.gpp.pokemoninfo.mainModule.viewModel.DetailPokemonViewModel
import com.gpp.pokemoninfo.mainModule.viewModel.MainViewModel
import org.json.JSONException


class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: PokemonAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mDetailPokemonViewModel: DetailPokemonViewModel

    private var page: Int = 0
    private var limit: Int = 57



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupViewModel()
        setupRecyclerView()


        mBinding.idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                page++
                mBinding.idPBLoading.setVisibility(View.VISIBLE)
                getDataFromAPI(page, limit)
            }
        })


    }

    private fun getDataFromAPI(page: Int, limit: Int) {
        if (page > limit) {
            // checking if the page number is greater than limit.
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show()
            // hiding our progress bar.
            mBinding.idPBLoading.setVisibility(View.GONE)
            return
        }
        // creating a string variable for url .
        val offset = page * 20
        val url = "https://pokeapi.co/api/v2/pokemon?offset=$offset&limit=20"

        val queue = Volley.newRequestQueue(this@MainActivity)

        // creating a variable for our json object request and passing our url to it.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                    // on below line we are extracting data from our json array.
                    val dataArray = response.getJSONArray("results")

                    // passing data from our json array in our array list.
                    for (i in 0 until dataArray.length()) {
                        val jsonObject = dataArray.getJSONObject(i)

                        mMainViewModel.addPokemon(Pokemon(name=jsonObject.getString("name"),url = jsonObject.getString("url")))

                        mAdapter = PokemonAdapter(mMainViewModel.returnPokemonList(), this@MainActivity)

                        mBinding.recyclerView.apply {
                            setHasFixedSize(true)
                            layoutManager = mLayoutManager
                            adapter = mAdapter
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) {
            Toast.makeText(this@MainActivity, "Fail to get data..", Toast.LENGTH_SHORT).show()
        }

        queue.add(jsonObjectRequest)
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
            launchDetailFragment(pokemons[0])
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