package com.danilkomyshev.pokedex.network

import com.danilkomyshev.pokedex.entity.PokeListEntryWrapper
import com.danilkomyshev.pokedex.entity.Pokemon
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokedex/1")
    fun getPokemonList(): Single<PokeListEntryWrapper>

    @GET("pokemon/{entry}")
    fun getPokemon(@Path("entry") entry: String): Single<Pokemon>
}