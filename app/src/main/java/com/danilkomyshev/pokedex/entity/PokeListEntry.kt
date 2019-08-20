package com.danilkomyshev.pokedex.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokeListEntry constructor(
    @PrimaryKey
    val id: Int,
    val name: String,
    val isFavourite : Boolean = false
)