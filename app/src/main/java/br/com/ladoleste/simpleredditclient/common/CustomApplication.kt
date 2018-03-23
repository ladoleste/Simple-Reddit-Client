package br.com.ladoleste.simpleredditclient.common

import android.app.Application
import android.graphics.drawable.Drawable
import br.com.ladoleste.simpleredditclient.BuildConfig
import br.com.ladoleste.simpleredditclient.dagger.DaggerMainComponent
import br.com.ladoleste.simpleredditclient.dagger.MainComponent
import br.com.ladoleste.simpleredditclient.dagger.NetworkModule
import com.facebook.stetho.Stetho
import timber.log.Timber

@Suppress("unused")
class CustomApplication : Application() {

    companion object {

        lateinit var component: MainComponent
            private set

        //used for the transitions activity animation
        var drawableHolder: Drawable? = null

        var apiUrl: String = BuildConfig.API_URL
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(SuperLog())

        component = DaggerMainComponent.builder()
                .networkModule(NetworkModule())
                .build()
    }
}