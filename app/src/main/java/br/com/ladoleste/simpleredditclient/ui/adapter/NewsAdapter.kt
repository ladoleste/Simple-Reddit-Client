package br.com.ladoleste.simpleredditclient.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.ladoleste.simpleredditclient.R
import br.com.ladoleste.simpleredditclient.common.NewsItem
import br.com.ladoleste.simpleredditclient.common.inflate
import br.com.ladoleste.simpleredditclient.dto.News
import br.com.ladoleste.simpleredditclient.ui.ItemClick
import br.com.ladoleste.simpleredditclient.ui.adapter.AdapterConstants.LOADING_ITEM
import br.com.ladoleste.simpleredditclient.ui.adapter.AdapterConstants.NEWS_ITEM
import br.com.ladoleste.simpleredditclient.ui.adapter.AdapterConstants.NEWS_ITEM_SELF
import br.com.ladoleste.simpleredditclient.ui.adapter.binders.BinderAdapterNews
import br.com.ladoleste.simpleredditclient.ui.adapter.binders.BinderAdapterNewsSelf
import timber.log.Timber

class NewsAdapter(private var items: List<NewsItem>, private val itemClick: ItemClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return items[position].getType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            NEWS_ITEM -> ViewHolder(parent.inflate(R.layout.item_news))
            NEWS_ITEM_SELF -> ViewHolder(parent.inflate(R.layout.item_news_self))
            LOADING_ITEM -> ViewHolder(parent.inflate(R.layout.item_loading))
            else -> throw RuntimeException("Unsupported Adapter Type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            NEWS_ITEM -> BinderAdapterNews.bind(holder, items[position] as News, itemClick)
            NEWS_ITEM_SELF -> BinderAdapterNewsSelf.bind(holder, items[position] as News, itemClick)
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    fun updateItems(it: List<NewsItem>) {

        Timber.i("-->> ${it.count()}")

        if (items.count() == it.count()) {
            items = it
            notifyItemRangeChanged(0, it.count() - 1)
        } else {
            items = it
            notifyDataSetChanged()
        }
    }
}