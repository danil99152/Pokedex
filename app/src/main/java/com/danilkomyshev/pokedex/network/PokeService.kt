package com.danilkomyshev.pokedex.network

import android.util.Log
import com.danilkomyshev.pokedex.entity.PokeListEntryWrapper
import com.danilkomyshev.pokedex.entity.Pokemon
import io.reactivex.Single

class PokeService(private val pokeApi: PokeApi) {

    fun getPokemonEntries(): Single<PokeListEntryWrapper> {
        Log.i("PokeService", "fetching entries...")
        return pokeApi.getPokemonList()
    }

    fun getPokemon(entry: String): Single<Pokemon> {
        Log.i("PokeService", "fetching a img_pokemon $entry")
        return pokeApi.getPokemon(entry)
    }
}