package br.com.ladoleste.simpleredditclient.common

import android.app.Application
import android.graphics.drawable.Drawable
import br.com.ladoleste.simpleredditclient.BuildConfig
import com.facebook.stetho.Stetho
import timber.log.Timber

@Suppress("unused")
class CustomApplication : Application() {

    companion object {

        //used for the transitions activity animation
        var drawableHolder: Drawable? = null

        lateinit var instance: CustomApplication
            private set

        var apiUrl: String = BuildConfig.API_URL
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(SuperLog())
        instance = this
    }
}