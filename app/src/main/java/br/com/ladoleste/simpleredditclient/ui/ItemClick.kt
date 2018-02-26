package br.com.ladoleste.simpleredditclient.ui

import android.widget.ImageView

/**
 *Created by Anderson on 15/02/2018.
 */
interface ItemClick {
    fun onItemClick(id: String, image: ImageView?)
}