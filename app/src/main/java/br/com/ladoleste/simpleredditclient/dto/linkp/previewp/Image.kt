package br.com.ladoleste.simpleredditclient.dto.linkp.previewp

import br.com.ladoleste.simpleredditclient.dto.linkp.previewp.imagep.Resolution
import br.com.ladoleste.simpleredditclient.dto.linkp.previewp.imagep.Source
import com.google.gson.annotations.SerializedName

data class Image(
        @SerializedName("source") val source: Source,
        @SerializedName("resolutions") val resolutions: List<Resolution>,
        @SerializedName("id") val id: String
)