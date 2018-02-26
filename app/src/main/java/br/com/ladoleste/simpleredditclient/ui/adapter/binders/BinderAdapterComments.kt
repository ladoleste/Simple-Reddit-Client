package br.com.ladoleste.simpleredditclient.ui.adapter.binders

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.ladoleste.simpleredditclient.app.toFriendlyTime
import br.com.ladoleste.simpleredditclient.app.toHtml
import br.com.ladoleste.simpleredditclient.dto.Comments
import kotlinx.android.synthetic.main.item_comments.view.*

/**
 *Created by Anderson on 16/02/2018.
 */
class BinderAdapterComments {

    companion object {
        @SuppressLint("SetTextI18n")
        fun bind(holder: RecyclerView.ViewHolder, com: Comments) {

            holder.itemView.tv_comment.text = "<u><b>${com.author}</b> commented</u> ${com.createdUtc.toFriendlyTime()} ${com.bodyHtml}".toHtml()

            holder.itemView.tv_reply.visibility = View.GONE

            if (com.replies != null) {

                var replies = ""
                com.replies.take(10).forEach {
                    replies += "<u><b>${it.author}</b> replied</u> ${it.createdUtc.toFriendlyTime()}:<p>${it.bodyHtml}<p/>"
                }

                holder.itemView.tv_reply.text = replies.toHtml()
                holder.itemView.tv_reply.visibility = View.VISIBLE
            }
        }
    }
}