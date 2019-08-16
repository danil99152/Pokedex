package com.danilkomyshev.pokedex.dimodules

import android.content.Context
import androidx.room.Room
import com.danilkomyshev.pokedex.database.Dao
import com.danilkomyshev.pokedex.database.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDao(db: Database): Dao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providePokemonDataBase(context: Context): Database {
        return Room.databaseBuilder(context.applicationContext, Database::class.java, "pokemons").build()
    }
}