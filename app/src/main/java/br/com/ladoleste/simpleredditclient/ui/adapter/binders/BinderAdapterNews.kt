package br.com.ladoleste.simpleredditclient.ui.adapter.binders

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.ladoleste.simpleredditclient.common.CustomApplication.Companion.drawableHolder
import br.com.ladoleste.simpleredditclient.common.loadImage
import br.com.ladoleste.simpleredditclient.common.toFriendlyTime
import br.com.ladoleste.simpleredditclient.dto.News
import br.com.ladoleste.simpleredditclient.ui.ItemClick
import br.com.ladoleste.simpleredditclient.ui.adapter.getCommentsText
import kotlinx.android.synthetic.main.inc_info.view.*
import kotlinx.android.synthetic.main.item_news.view.*


/**
 *Created by Anderson on 16/02/2018.
 */
class BinderAdapterNews {

    companion object {
        @SuppressLint("SetTextI18n")
        fun bind(holder: RecyclerView.ViewHolder, news: News, itemClick: ItemClick) {
            holder.itemView.tv_title.text = news.title
            holder.itemView.tv_created.text = "Posted ${news.createdUtc.toFriendlyTime()}"
            if (news.score > 0) {
                holder.itemView.tv_avg.visibility = View.VISIBLE
                holder.itemView.tv_avg.text = news.score.toString()
            } else
                holder.itemView.tv_avg.visibility = View.GONE
            holder.itemView.tv_num_comments.text = getCommentsText(news.numComments)

            holder.itemView.setOnClickListener {
                drawableHolder = holder.itemView.thumbnail.drawable
                itemClick.onItemClick(news.id, holder.itemView.thumbnail)
            }

            holder.itemView.thumbnail.visibility = View.VISIBLE
            holder.itemView.thumbnail.loadImage(news.thumbnail)
        }
    }
}