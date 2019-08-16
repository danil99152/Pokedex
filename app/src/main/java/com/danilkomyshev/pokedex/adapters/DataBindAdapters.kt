package com.danilkomyshev.pokedex.adapters

import com.danilkomyshev.pokedex.PokeApp
import com.danilkomyshev.pokedex.R
import com.danilkomyshev.pokedex.entity.PokeListEntry
import com.danilkomyshev.pokedex.entity.Pokemon
import com.danilkomyshev.pokedex.entity.PokemonType
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

@BindingAdapter(value = ["bind_entry_name"])
fun TextView.bindEntryName(item: PokeListEntry?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter(value = ["bind_entry_num"])
fun TextView.bindEntryNum(item: PokeListEntry?) {
    item?.let {
        text = item.id.toString()
    }
}


@BindingAdapter(value = ["bind_detail_sprite"])
fun ImageView.bindSprite(pokemon: Pokemon?) {
    pokemon?.let {
        val imgUri = pokemon.sprite.toUri().buildUpon().scheme("https").build()
        Glide.with(context)
            .load(imgUri)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation))
            .into(this)
    }
}

@BindingAdapter(value = ["bind_pokemon_name"])
fun TextView.bindPokemonName(pokemon: Pokemon?){
    pokemon?.let {
        text = pokemon.name
    }
}

@BindingAdapter(value = ["bind_pokemon_entry"])
fun TextView.bindPokemonEntry(pokemon: Pokemon?){
    pokemon?.let {
        text = pokemon.id.toString()
    }
}

@BindingAdapter(value = ["bind_pokemon_height"])
fun TextView.bindPokemonHeight(pokemon: Pokemon?){
    pokemon?.let {
        text = pokemon.height.toString()
    }
}

@BindingAdapter(value = ["bind_pokemon_weight"])
fun TextView.bindPokemonWeight(pokemon: Pokemon?){
    pokemon?.let {
        text = pokemon.weight.toString()
    }
}

@BindingAdapter(value = ["bind_pokemon_type1"])
fun TextView.bindPokemonType1(pokemon: Pokemon?){
    pokemon?.let {
        visibility = View.VISIBLE
        text = pokemon.type1.type
        setBackgroundColor(ContextCompat.getColor(PokeApp.getAppComponent().getContext(), pokemon.type1.color))
    }
}

@BindingAdapter(value = ["bind_pokemon_type2"])
fun TextView.bindPokemonType2(pokemon: Pokemon?) {
    pokemon?.let {
        if (pokemon.type2 == PokemonType.NO)
            return

        visibility = View.VISIBLE
        text = pokemon.type2.type
        setBackgroundColor(ContextCompat.getColor(PokeApp.getAppComponent().getContext(), pokemon.type2.color))
    }
}