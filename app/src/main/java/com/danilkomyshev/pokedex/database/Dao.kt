package com.danilkomyshev.pokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.danilkomyshev.pokedex.entity.FavouriteList
import com.danilkomyshev.pokedex.entity.PokeListEntry
import com.danilkomyshev.pokedex.entity.Pokemon
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface Dao {

    @Query("select * from PokeListEntry")
    fun getAllEntries(): Single<List<PokeListEntry>>

    @Query("select * from PokeListEntry where id = :query or name like '%' || :query || '%'")
    fun getEntries(query: String): Single<List<PokeListEntry>>

    @Query("select * from PokeListEntry where isFavourite =:fav and id = :query or name like '%' || :query || '%'")
    fun getFavEntries(fav: Int, query: String): Single<List<FavouriteList>>

    @Query("select * from Pokemon where id = :entry")
    fun getPokemon(entry: Int): Maybe<Pokemon>

    @Query("select * from PokeListEntry where isFavourite = :fav")
    fun getFavPokemons(fav: Int): Single<List<FavouriteList>>

    @Query("select isFavourite from Pokemon where id = :entry")
    fun isPokeFav(entry: Int): Int

    @Query("update Pokemon set isFavourite = 1 where id = :entry")
    fun toFavouritePokemon(entry: Int): Completable

    @Query("update PokeListEntry set isFavourite = 1 where id = :entry")
    fun toFavouritePokeListEntry(entry: Int): Completable

    @Query("update Pokemon set isFavourite = 0 where id = :entry")
    fun toUsualPokemon(entry: Int): Completable

    @Query("update PokeListEntry set isFavourite = 0 where id = :entry")
    fun toUsualPokeListEntry(entry: Int): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEntries(entries: List<PokeListEntry>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemon(pokemon: Pokemon): Completable

//    @Query("delete from PokeListEntry")
//    fun deleteAllEntries(): Completable
//
//    @Query("delete from Pokemon")
//    fun deleteAllPokemons(): Completable
}