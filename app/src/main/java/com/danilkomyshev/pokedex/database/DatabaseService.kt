package com.danilkomyshev.pokedex.database

import android.util.Log
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

    fun getFavPokemons(fav: Boolean) : Single<List<PokeListEntry>> {
        return if (fav) dao.getFavPokemons(fav)
        else dao.getAllEntries()
    }

    fun isPokeFav (entry: Int) : Boolean{
        return dao.isPokeFav(entry)
    }

//    fun deletePokemons(): Completable{
//        return dao.deleteAllPokemons()
//    }
//
//    fun deleteEntries(): Completable{
//        return dao.deleteAllEntries()
//    }
}