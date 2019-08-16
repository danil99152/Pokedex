package com.danilkomyshev.pokedex

import android.app.Application
import android.util.Log
import com.danilkomyshev.pokedex.dicomponent.AppComponent
import com.danilkomyshev.pokedex.dicomponent.DaggerAppComponent
import com.danilkomyshev.pokedex.dimodules.ContextModule
import io.reactivex.plugins.RxJavaPlugins


class PokeApp : Application() {

    companion object {
        private lateinit var appComponent: AppComponent

        fun getAppComponent(): AppComponent {
            return appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setErrorHandler { throwable -> Log.e("PokeApp", throwable.toString())}

        appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }
}