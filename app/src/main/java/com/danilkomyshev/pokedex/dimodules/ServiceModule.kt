package com.danilkomyshev.pokedex.dimodules

import com.danilkomyshev.pokedex.network.PokeApi
import com.danilkomyshev.pokedex.network.PokeService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
class ServiceModule {
    @Provides
    @Singleton
    fun provideService(pokeApi: PokeApi): PokeService {
        return PokeService(pokeApi)
    }
}