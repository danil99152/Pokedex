package com.danilkomyshev.pokedex.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.danilkomyshev.pokedex.R

enum class PokemonType(val type: String, val color: Int) {
    BUG("Bug", R.color.bug),
    DRAGON("Dragon", R.color.dragon),
    ELECTRIC("Electric", R.color.electric),
    FAIRY("Fairy", R.color.fairy),
    FIGHTING("Fighting", R.color.fighting),
    FIRE("Fire", R.color.fire),
    FLYING("Flying", R.color.flying),
    GHOST("Ghost", R.color.ghost),
    GRASS("Grass", R.color.grass),
    GROUND("Ground", R.color.ground),
    ICE("Ice", R.color.ice),
    NORMAL("Normal", R.color.normal),
    POISON("Poison", R.color.poison),
    PSYCHIC("Psychic", R.color.psychic),
    ROCK("Rock", R.color.rock),
    WATER("Water", R.color.water),
    NO("No", R.color.colorAccent)
}

@Entity
data class Pokemon(
    @PrimaryKey
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprite: String,
    val type1: PokemonType,
    val type2: PokemonType,
    val isFavourite : Int = 0
)