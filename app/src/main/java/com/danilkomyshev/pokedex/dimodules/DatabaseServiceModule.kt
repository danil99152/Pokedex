package com.danilkomyshev.pokedex.dimodules

import com.danilkomyshev.pokedex.database.Dao
import com.danilkomyshev.pokedex.database.DatabaseService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class])
class DatabaseServiceModule {
    @Provides
    @Singleton
    fun provideDatabaseService(dao: Dao): DatabaseService {
        return DatabaseService(dao)
    }
}