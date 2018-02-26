package br.com.ladoleste.simpleredditclient.api

import br.com.ladoleste.simpleredditclient.dto.Thing
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 *Created by Anderson on 08/12/2017.
 */
interface Api {
    @GET("/r/Android/{category}/.json?raw_json=1")
    fun getNews(@Path("category") category: String, @Query("after") after: String = "", @Query("limit") limit: Int = 20): Single<Thing>

    @GET("/r/Android/comments/{id}/.json?raw_json=1")
    fun getComments(@Path("id") id: String = ""): Single<List<Thing>>
}