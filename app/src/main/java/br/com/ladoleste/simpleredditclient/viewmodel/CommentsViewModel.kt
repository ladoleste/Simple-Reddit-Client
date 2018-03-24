package br.com.ladoleste.simpleredditclient.viewmodel

import android.arch.lifecycle.MutableLiveData
import br.com.ladoleste.simpleredditclient.common.CustomApplication
import br.com.ladoleste.simpleredditclient.dto.Comments
import br.com.ladoleste.simpleredditclient.dto.News
import br.com.ladoleste.simpleredditclient.model.Api
import javax.inject.Inject

/**
 *Created by Anderson on 14/02/2018.
 */
class CommentsViewModel : BaseViewModel() {

    @Inject
    lateinit var api: Api

    init {
        CustomApplication.component.inject(this)
    }

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