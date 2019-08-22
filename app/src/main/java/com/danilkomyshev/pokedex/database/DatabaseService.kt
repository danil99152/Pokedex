package com.danilkomyshev.pokedex.database

import android.util.Log
import com.danilkomyshev.pokedex.entity.FavouriteList
import com.danilkomyshev.pokedex.entity.PokeListEntry
import com.danilkomyshev.pokedex.entity.Pokemon
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

class DatabaseService(private val dao: Dao) {


    fun getEntries(query: String): Single<List<PokeListEntry>> {
        return if (query.isBlank())
            dao.getAllEntries()
        else
            dao.getEntries(query)
    }

    fun getFavEntries(query: String): Single<List<FavouriteList>> {
        return dao.getFavEntries(1,query)
    }

    fun getPokemon(entry: Int): Maybe<Pokemon>{
        Log.i("PokeDB", "queueing a pokemon $entry")
        return dao.getPokemon(entry)
    }

    fun insertEntries(entries: List<PokeListEntry>): Completable{
        return dao.insertAllEntries(entries)
    }

    fun insertPokemon(pokemon: Pokemon): Completable{
        return dao.insertPokemon(pokemon)
    }

    fun getFavPokemons() : Single<List<FavouriteList>> {
        return dao.getFavPokemons(1)
    }

//    fun isPokeFav (entry: Int) : Int{
//        return dao.isPokeFav(entry)
//    }

    fun toFavourite (entry: Int) :Completable{
        dao.toFavouritePokeListEntry(entry)
        return dao.toFavouritePokemon(entry)
    }

    fun toUsual (entry: Int) : Completable{
        dao.toUsualPokeListEntry(entry)
        return dao.toUsualPokemon(entry)
    }

//    fun deletePokemons(): Completable{
//        return dao.deleteAllPokemons()
//    }
//
//    fun deleteEntries(): Completable{
//        return dao.deleteAllEntries()
//    }
}