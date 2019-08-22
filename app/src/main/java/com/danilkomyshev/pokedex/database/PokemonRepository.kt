package com.danilkomyshev.pokedex.database

import android.util.Log
import com.danilkomyshev.pokedex.entity.FavouriteList
import com.danilkomyshev.pokedex.entity.PokeListEntry
import com.danilkomyshev.pokedex.entity.Pokemon
import com.danilkomyshev.pokedex.network.PokeService
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

class PokemonRepository(
    private val pokeDatabaseService: DatabaseService,
    private val pokeApiService: PokeService
) {

    fun initEntries(): Single<List<PokeListEntry>> {
        return Single.concat(pokeDatabaseService.getEntries(""), fetchAndCacheEntries())
            .filter { it.isNotEmpty() }
            .firstElement()
            .toSingle()
    }

    fun getFavPokemons() : Single<List<FavouriteList>>{
        return pokeDatabaseService.getFavPokemons()
    }

//    fun isPokeFav (query: Int) : Int{
//        return pokeDatabaseService.isPokeFav(query)
//    }

    fun toFavourite (query: Int) : Completable{
        return pokeDatabaseService.toFavourite(query)
    }

    fun toUsual (query: Int) : Completable{
        return pokeDatabaseService.toUsual(query)
    }

    fun getEntries(query: String): Single<List<PokeListEntry>> {
        return pokeDatabaseService.getEntries(query)
    }

    fun getFavEntries(query: String): Single<List<FavouriteList>> {
        return pokeDatabaseService.getFavEntries(query)
    }

    fun getPokemon(query: Int): Single<Pokemon> {
        Log.i("Poke Repo", "getting a pokemon: $query")
        return Maybe.concat(pokeDatabaseService.getPokemon(query), fetchAndCachePokemon(query))
            .firstElement()
            .toSingle()
    }

    private fun fetchAndCacheEntries(): Single<List<PokeListEntry>> {
        val fetch = pokeApiService.getPokemonEntries().map { it.entryList }.cache()

        return fetch.flatMapCompletable {
            Log.i("Poke Repo", "Inserting entries...")
            pokeDatabaseService.insertEntries(it)
        }
            .andThen(fetch)
    }

    private fun fetchAndCachePokemon(query: Int): Maybe<Pokemon> {
        val fetch = pokeApiService.getPokemon(query.toString()).cache()

        return fetch
            .flatMapCompletable {
                Log.i("Poke Repo", "inserting a pokemon: $it")
                pokeDatabaseService.insertPokemon(it)
            }
            .andThen(fetch.toMaybe())
    }

//    fun deletePokemons(): Completable{
//        Log.i("Poke Repo", "DROPPING THE POKEMON TABLE!")
//        return pokeDatabaseService.deletePokemons()
//    }
//
//    fun deleteEntries(): Completable{
//        Log.i("Poke Repo", "DROPPING THE ENTRIES TABLE!")
//        return pokeDatabaseService.deleteEntries()
//    }
}