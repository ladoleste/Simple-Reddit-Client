package br.com.ladoleste.simpleredditclient.dto

data class Comments(
        val author: String,
        val createdUtc: Long,
        val bodyHtml: String,
        val body: String,
        val replies: List<Comments>?
)