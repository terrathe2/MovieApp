package com.redhaputra.movieapp

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for maintaining global application state.
 *
 * @see Application
 */
@HiltAndroidApp
class MovieApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initStetho()
    }

    /**
     *  Network logger initialization
     */
    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }
}