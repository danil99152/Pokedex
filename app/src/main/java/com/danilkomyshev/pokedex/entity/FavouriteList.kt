package com.danilkomyshev.pokedex.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteList constructor(
    @PrimaryKey
    val id: Int,
    val name: String,
    val isFavourite : Int
)