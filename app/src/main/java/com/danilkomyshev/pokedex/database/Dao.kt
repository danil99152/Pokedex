package com.danilkomyshev.pokedex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("select * from Pokemon where id = :entry")
    fun getPokemon(entry: Int): Maybe<Pokemon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEntries(entries: List<PokeListEntry>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemon(pokemon: Pokemon): Completable

    @Query("delete from PokeListEntry")
    fun deleteAllEntries(): Completable

    @Query("delete from Pokemon")
    fun deleteAllPokemons(): Completable

}