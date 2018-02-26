package br.com.ladoleste.simpleredditclient.dto

import com.google.gson.annotations.SerializedName

data class Thing(
        @SerializedName("kind") val kind: String,
        @SerializedName("data") val dataHolderList: DataHolderList
)