package br.com.ladoleste.simpleredditclient.dto

import android.webkit.URLUtil
import br.com.ladoleste.simpleredditclient.common.NewsItem
import br.com.ladoleste.simpleredditclient.dto.linkp.Preview
import br.com.ladoleste.simpleredditclient.ui.adapter.AdapterConstants

data class News(
        val author: String,
        val createdUtc: Long,
        val domain: String,
        val score: Int,
        val isSelf: Boolean,
        val numComments: Int,
        val selftext: String,
        val selftextHtml: String?,
        val thumbnail: String,
        val title: String,
        val url: String,
        val id: String,
        val preview: Preview?,
        val permalink: String
) : NewsItem {
    override val getType: Int
        get() = if (isSelf || !URLUtil.isValidUrl(thumbnail)) AdapterConstants.NEWS_ITEM_SELF else AdapterConstants.NEWS_ITEM
}