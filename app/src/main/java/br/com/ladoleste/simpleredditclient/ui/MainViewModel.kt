package br.com.ladoleste.simpleredditclient.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.ladoleste.simpleredditclient.api.Api
import br.com.ladoleste.simpleredditclient.api.Retrofit
import br.com.ladoleste.simpleredditclient.app.Category
import br.com.ladoleste.simpleredditclient.app.NewsItem
import br.com.ladoleste.simpleredditclient.ui.adapter.AdapterConstants

/**
 *Created by Anderson on 14/02/2018.
 */
class MainViewModel(private val api: Api = Retrofit.getApi()) : ViewModel() {

    val items = MutableLiveData<List<NewsItem>>()
    val error = MutableLiveData<Throwable>()
    var category = Category.NEW
    var loadingEnabled = false

    var lastAfter = ""

    private val loadingItem = object : NewsItem {
        override val getType: Int
            get() = AdapterConstants.LOADING_ITEM
    }

    fun getNews(refresh: Boolean = false) {
        if (refresh) {
            lastAfter = ""
        }
        api.getNews(category.toString().toLowerCase(), lastAfter)
                .subscribe({ x ->
                    lastAfter = x.dataHolderList.after ?: ""
                    val elements = x.dataHolderList.children.map { it.data.toNews() }

                    var list = items.value?.toMutableList()

                    if (refresh)
                        list = elements.toMutableList()
                    else if (list != null && !list.isEmpty()) {
                        list.remove(list.last())
                        list.addAll(elements)
                    } else {
                        list = elements.toMutableList()
                    }

                    if (lastAfter.isNotEmpty())
                        list.add(loadingItem)

                    loadingEnabled = lastAfter.isNotEmpty()

                    items.postValue(list)
                }, {
                    error.postValue(it)
                })
    }
}