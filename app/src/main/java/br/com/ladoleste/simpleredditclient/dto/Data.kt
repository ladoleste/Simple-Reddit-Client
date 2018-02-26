package br.com.ladoleste.simpleredditclient.dto

import br.com.ladoleste.simpleredditclient.dto.linkp.Preview
import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("author") private val author: String,
        @SerializedName("created_utc") private val createdUtc: Long,
        @SerializedName("domain") private val domain: String,
        @SerializedName("id") private val id: String,
        @SerializedName("is_self") private val isSelf: Boolean,
        @SerializedName("num_comments") private val numComments: Int,
        @SerializedName("preview") private val preview: Preview?,
        @SerializedName("score") private val score: Int,
        @SerializedName("permalink") private val permalink: String,
        @SerializedName("selftext_html") private val selftextHtml: String?,
        @SerializedName("selftext") private val selftext: String,
        @SerializedName("thumbnail") private val thumbnail: String,
        @SerializedName("title") private val title: String,
        @SerializedName("url") private val url: String,
        @SerializedName("body_html") private val bodyHtml: String,
        @SerializedName("replies") private val replies: Thing?,
        @SerializedName("body") private val body: String) {
    fun toNews() = News(author, createdUtc, domain, score, isSelf, numComments, selftext, selftextHtml, thumbnail, title, url, id, preview, permalink)

    fun toComments(): Comments =
            Comments(author, createdUtc, body, bodyHtml, replies?.let {
                it.dataHolderList.children
                        .filter { it.kind == "t1" }
                        .map { it.data.toComments() }
            })
}