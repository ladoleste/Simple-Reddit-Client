package br.com.ladoleste.simpleredditclient.api

/**
 *Created by Anderson on 13/12/2017.
 */
import br.com.ladoleste.simpleredditclient.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

object OkHttpProvider {
    private var instance: OkHttpClient? = null

    val okHttpInstance: OkHttpClient
        get() {
            if (instance == null) {

                val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { mensagem -> Timber.tag("OkHttp").d(mensagem); })

                @Suppress("ConstantConditionIf")
                logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE

                instance = OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .addNetworkInterceptor(StethoInterceptor())
                        .build()
            }
            return instance as OkHttpClient
        }
}