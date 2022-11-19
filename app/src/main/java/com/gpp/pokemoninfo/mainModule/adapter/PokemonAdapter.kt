package com.gpp.pokemoninfo.mainModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gpp.pokemoninfo.R
import com.gpp.pokemoninfo.databinding.ItemPokemonBinding
import com.gpp.pokemoninfo.entities.Pokemon

class PokemonAdapter (private var pokemons: MutableList<Pokemon>, private var listener: OnClickListener) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_pokemon, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemons.get(position)

        with(holder){
            setListener(pokemon)

            binding.tvName.text = pokemon.name

            /*Glide.with(mContext)
                .load(pokemon.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgPhoto)*/
        }
    }

    override fun getItemCount(): Int = pokemons.size

    fun setPokemons(pokemons: MutableList<Pokemon>) {
        this.pokemons = pokemons
        notifyDataSetChanged()
    }



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemPokemonBinding.bind(view)

        fun setListener(pokemon: Pokemon){
            with(binding.root) {
                setOnClickListener { listener.onClick(pokemon) }

            }


        }
    }
}