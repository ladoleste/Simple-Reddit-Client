package br.com.ladoleste.simpleredditclient.dto

import com.google.gson.annotations.SerializedName

data class DataHolder(
        @SerializedName("kind") val kind: String,
        @SerializedName("data") val data: Data
)