package com.gpp.pokemoninfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gpp.pokemoninfo.databinding.ItemPokemonBinding

class PokemonAdapter (private var pokemons: MutableList<Pokemon>, private var listener: OnClickListener) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_pokemon,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemons.get(position)

        with(holder){

            setListener(pokemon)
            mBinding.tvName.text = pokemon.name

        }
    }

    override fun getItemCount(): Int = pokemons.size

    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view){

        val mBinding = ItemPokemonBinding.bind(view)

        fun setListener(pokemon: Pokemon){
            mBinding.root.setOnClickListener{ listener.onClick(pokemon)}
        }
    }
}