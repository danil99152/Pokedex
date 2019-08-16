package com.danilkomyshev.pokedex.dimodules

import com.danilkomyshev.pokedex.database.DatabaseService
import com.danilkomyshev.pokedex.database.PokemonRepository
import com.danilkomyshev.pokedex.network.PokeService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseServiceModule::class, ServiceModule::class])
class RepositoryModule{

    @Provides
    @Singleton
    fun provideRepository(dbServiceModule: DatabaseService, service: PokeService): PokemonRepository {
        return PokemonRepository(dbServiceModule, service)
    }
}