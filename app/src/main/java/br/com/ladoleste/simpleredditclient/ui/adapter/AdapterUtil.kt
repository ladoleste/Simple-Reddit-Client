package br.com.ladoleste.simpleredditclient.ui.adapter

/**
 *Created by Anderson on 16/02/2018.
 */
fun getCommentsText(numComments: Int): CharSequence? {
    return when (numComments) {
        0 -> "no comments yet"
        1 -> "one comment"
        else -> "$numComments comments"
    }
}