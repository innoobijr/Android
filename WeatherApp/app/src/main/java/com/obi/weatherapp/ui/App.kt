package com.obi.weatherapp.ui

import android.app.Application
import com.obi.weatherapp.extensions.DelegatesExt

// Creating an Application singleton

class App : Application() {

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()
    /**
    * The original implementation was:
    *   companion object {
     *   lateinit var instance: App
     *   }
     *
     *   with this implementation App is mutable. If we did not what this we could write the following
     *   ompanion object {
     *   lateinit var instance: App
     *      private set
     *   }
     *
     *   But instead will use the Delegate instead:
     *   which in this case will return a avalue if the alue is assigned otherwise it throws an exception
    */
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}