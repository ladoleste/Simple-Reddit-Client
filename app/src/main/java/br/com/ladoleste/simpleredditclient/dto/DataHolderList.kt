package br.com.ladoleste.simpleredditclient.dto

import com.google.gson.annotations.SerializedName

data class DataHolderList(
        @SerializedName("after") val after: String?,
        @SerializedName("dist") val dist: Int,
        @SerializedName("modhash") val modhash: String,
        @SerializedName("whitelist_status") val whitelistStatus: String,
        @SerializedName("children") val children: List<DataHolder>,
        @SerializedName("before") val before: Any
)