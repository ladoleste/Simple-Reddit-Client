package br.com.ladoleste.simpleredditclient.dto.linkp

import br.com.ladoleste.simpleredditclient.dto.linkp.previewp.Image
import com.google.gson.annotations.SerializedName

data class Preview(
        @SerializedName("images") val images: List<Image>
)