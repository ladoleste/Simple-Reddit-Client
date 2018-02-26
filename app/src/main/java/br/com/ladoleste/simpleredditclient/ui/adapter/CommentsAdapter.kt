package br.com.ladoleste.simpleredditclient.ui.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.ladoleste.simpleredditclient.R
import br.com.ladoleste.simpleredditclient.app.inflate
import br.com.ladoleste.simpleredditclient.dto.Comments
import br.com.ladoleste.simpleredditclient.ui.adapter.binders.BinderAdapterComments

class CommentsAdapter(private var items: List<Comments>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_comments))

    @SuppressLint("SetTextI18n")
    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        BinderAdapterComments.bind(holder, items[position])
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

}