package br.com.ladoleste.simpleredditclient.dto.linkp.previewp.imagep

import com.google.gson.annotations.SerializedName

data class Resolution(
        @SerializedName("url") val url: String,
        @SerializedName("width") val width: Int,
        @SerializedName("height") val height: Int
)