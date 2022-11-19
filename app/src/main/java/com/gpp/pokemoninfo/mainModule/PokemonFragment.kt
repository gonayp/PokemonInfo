package com.gpp.pokemoninfo.mainModule

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gpp.pokemoninfo.PokemonApplication
import com.gpp.pokemoninfo.R
import com.gpp.pokemoninfo.databinding.FragmentPokemonBinding
import com.gpp.pokemoninfo.entities.Pokemon
import com.gpp.pokemoninfo.mainModule.viewModel.DetailPokemonViewModel


class PokemonFragment : Fragment() {
    private lateinit var mBinding: FragmentPokemonBinding
    //MVVM
    private lateinit var mDetailPokemonViewModel: DetailPokemonViewModel

    private var mActivity: MainActivity? = null
    private lateinit var mPokemonEntity: Pokemon


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDetailPokemonViewModel = ViewModelProvider(requireActivity()).get(DetailPokemonViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {
        mBinding = FragmentPokemonBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //MVVM
        setupViewModel()
    }

    private fun setupViewModel() {
        mDetailPokemonViewModel.getPokemonSelected().observe(viewLifecycleOwner, {
            mPokemonEntity = it
            getPokemon(it)

            setupActionBar()
        })

        mDetailPokemonViewModel.getResult().observe(viewLifecycleOwner, { result ->

            when(result){
                is Long -> {
                    mPokemonEntity.id = result

                    mDetailPokemonViewModel.setPokemonSelected(mPokemonEntity)

                    mActivity?.onBackPressed()
                }
                is Pokemon -> {
                    mDetailPokemonViewModel.setPokemonSelected(mPokemonEntity)
                }
            }
        })
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setHasOptionsMenu(true)
    }

    private fun loadImage(url: String){
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.imgPhoto)
    }

    private fun setUiPokemon(pokemon: Pokemon) {

        with(mBinding){

            tvName.text = pokemon.name.editable()
            (getString(R.string.tv_base_experience)+pokemon.baseExperience.toString()).also { tvBaseExperience.text = it }
            (getString(R.string.tv_height)+pokemon.height.toString()).also { tvHeight.text = it }
            (getString(R.string.tv_weight)+pokemon.weight.toString()).also { tvWeight.text = it }
            loadImage(pokemon.photoURL)
        }
    }

    private fun String.editable(): Editable = Editable.Factory.getInstance().newEditable(this)


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                mActivity?.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mDetailPokemonViewModel.setResult(Any())

        setHasOptionsMenu(false)
        super.onDestroy()
    }


    fun getPokemon(pokemon: Pokemon) {

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, pokemon.url, null,
            { response ->
                Log.i("RESPONSE",response.toString())
                val name = response.getString("name")// .optJSONArray("name")?.toString()
                val baseExperience = response.getInt("base_experience")
                val height = response.getInt("height")
                val weight = response.getInt("weight")
                val sprites = response.getJSONObject("sprites")
                val front_default = sprites.getString("front_default")
                val pokemon_detalle: Pokemon = Pokemon(name= name, baseExperience = baseExperience, height = height, weight = weight, photoURL = front_default)
                setUiPokemon(pokemon_detalle)
            },
            { error ->
                setUiPokemon(pokemon)
            }
        )

        PokemonApplication.pokemonAPI.addToRequestQueue(jsonObjectRequest)


    }

}