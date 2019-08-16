package com.danilkomyshev.pokedex.network

import com.danilkomyshev.pokedex.entity.PokeListEntryWrapper
import com.danilkomyshev.pokedex.entity.Pokemon
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokedex/1")
    fun getPokemonList(): Single<PokeListEntryWrapper>

    @GET("img_pokemon/{entry}")
    fun getPokemon(@Path("entry") entry: String): Single<Pokemon>






    @GET("pokedex/1")
    fun getText(): Single<String>

    @GET("img_pokemon/{entry}")
    fun getPokemonText(@Path("entry") entry: String): Single<String>
}