package br.com.ladoleste.simpleredditclient.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.ladoleste.simpleredditclient.api.Api
import br.com.ladoleste.simpleredditclient.api.Retrofit
import br.com.ladoleste.simpleredditclient.dto.Comments
import br.com.ladoleste.simpleredditclient.dto.News

/**
 *Created by Anderson on 14/02/2018.
 */
class CommentsViewModel(private val api: Api = Retrofit.getApi()) : ViewModel() {

    val news = MutableLiveData<News>()
    val comments = MutableLiveData<List<Comments>>()
    val error = MutableLiveData<Throwable>()
    lateinit var id: String

    fun getComments() {
        api.getComments(id)
                .subscribe({
                    news.postValue(it.first().dataHolderList.children.map { it.data.toNews() }.single())
                    comments.postValue(it.last().dataHolderList.children.filter { it.kind == "t1" }.map { it.data.toComments() })
                }, {
                    error.postValue(it)
                })
    }
}