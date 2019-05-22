package com.obi.weather.ui

import android.app.Application
import android.database.sqlite.SQLiteOpenHelper
import com.obi.weather.ui.utils.DelegatesExt

class App :  Application() {

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()// property modifier
        //fun instance() = instance!!
    }

    override fun onCreate(){
        super.onCreate()
        instance = this
        //val db = database.writableDatabase
    }
}