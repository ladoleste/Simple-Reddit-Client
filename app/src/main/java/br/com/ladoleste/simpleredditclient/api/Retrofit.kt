package br.com.ladoleste.simpleredditclient.api

import br.com.ladoleste.simpleredditclient.app.CustomApplication.Companion.apiUrl
import br.com.ladoleste.simpleredditclient.app.CustomDeserializer
import br.com.ladoleste.simpleredditclient.dto.Thing
import br.com.ladoleste.simpleredditclient.model.Api
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 *Created by Anderson on 15/02/2018.
 */
class Retrofit {
    companion object {
        fun getApi(url: String = apiUrl): Api {

            val gson = GsonBuilder().registerTypeAdapter(Thing::class.java, CustomDeserializer()).create()

            val retrofit = Retrofit.Builder()
                    .client(OkHttpProvider.okHttpInstance)
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()

            return retrofit.create(Api::class.java)
        }
    }
}