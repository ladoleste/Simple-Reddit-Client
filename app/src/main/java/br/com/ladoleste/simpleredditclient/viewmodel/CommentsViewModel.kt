package br.com.ladoleste.simpleredditclient.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.ladoleste.simpleredditclient.api.RetrofitConfig
import br.com.ladoleste.simpleredditclient.dto.Comments
import br.com.ladoleste.simpleredditclient.dto.News
import br.com.ladoleste.simpleredditclient.model.Api

/**
 *Created by Anderson on 14/02/2018.
 */
class CommentsViewModel(private val api: Api = RetrofitConfig.getApi) : BaseViewModel() {

    val news = MutableLiveData<News>()
    val comments = MutableLiveData<List<Comments>>()
    val error = MutableLiveData<Throwable>()
    lateinit var id: String

    fun getComments() {
        cDispose.add(api.getComments(id)
                .subscribe({
                    news.postValue(it.first().dataHolderList.children.map { it.data.toNews() }.single())
                    comments.postValue(it.last().dataHolderList.children.filter { it.kind == "t1" }.map { it.data.toComments() })
                }, {
                    error.postValue(it)
                }))
    }
}