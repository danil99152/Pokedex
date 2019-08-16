package com.danilkomyshev.pokedex.database

import com.danilkomyshev.pokedex.entity.PokeListEntry
import com.danilkomyshev.pokedex.entity.Pokemon
import com.danilkomyshev.pokedex.entity.PokemonType
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [PokeListEntry::class, Pokemon::class], version = 1)
@TypeConverters(MyConverters::class)
abstract class Database : RoomDatabase() {
    abstract val dao: Dao
}

class MyConverters{
    @TypeConverter
    fun fromTypeToString(value: PokemonType?): String?{
        return value?.let { it.type }
    }

    @TypeConverter
    fun fromStringToType(value: String?): PokemonType?{
        return value?.let { PokemonType.valueOf(it.toUpperCase()) }
    }
}