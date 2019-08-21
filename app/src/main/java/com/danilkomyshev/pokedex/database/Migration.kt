package com.danilkomyshev.pokedex.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration


val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Pokemon ADD COLUMN isFavourite INTEGER DEFAULT 0 NOT NULL")
        database.execSQL("ALTER TABLE PokeListEntry ADD COLUMN isFavourite INTEGER DEFAULT 0 NOT NULL")
    }
}