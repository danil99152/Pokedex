package com.danilkomyshev.pokedex.dicomponent

import android.content.Context
import com.danilkomyshev.pokedex.FavListFragment
import com.danilkomyshev.pokedex.PokeDetailsFragment
import com.danilkomyshev.pokedex.PokeListFragment
import com.danilkomyshev.pokedex.dimodules.ContextModule
import com.danilkomyshev.pokedex.dimodules.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, RepositoryModule::class])
interface AppComponent {
    fun getContext(): Context

    fun inject(pokeDetailsFragment: PokeDetailsFragment)
    fun inject(pokeListFragment: PokeListFragment)
    fun inject(favListFragment: FavListFragment)
}