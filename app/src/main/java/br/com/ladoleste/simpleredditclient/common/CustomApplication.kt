package br.com.ladoleste.simpleredditclient.common

import android.app.Application
import android.graphics.drawable.Drawable
import br.com.ladoleste.simpleredditclient.BuildConfig
import br.com.ladoleste.simpleredditclient.dagger.DaggerMyAppComponent
import br.com.ladoleste.simpleredditclient.dagger.MyAppComponent
import br.com.ladoleste.simpleredditclient.dagger.MyAppContextModule
import br.com.ladoleste.simpleredditclient.dagger.MyAppModule
import com.facebook.stetho.Stetho
import timber.log.Timber

@Suppress("unused")
class CustomApplication : Application() {

    companion object {

        lateinit var component: MyAppComponent
            private set

        //used for the transitions activity animation
        var drawableHolder: Drawable? = null

        var apiUrl: String = BuildConfig.API_URL
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(SuperLog())

        component = DaggerMyAppComponent.builder()
                .myAppModule(MyAppModule())
                .myAppContextModule(MyAppContextModule(this))
                .build()
    }
}