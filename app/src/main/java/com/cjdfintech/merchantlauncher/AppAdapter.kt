package com.cjdfintech.merchantlauncher

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.app_item.view.*

class AppAdapter(val items: ArrayList<AppInfo>, val context: Context) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.appTitleTv?.text = items.get(position).label
        holder.appIcon.setImageDrawable(items.get(position).icon)
    }

    override fun onCreateViewHolder(viewgroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item, viewgroup, false))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appTitleTv = itemView.appNameTv
        val appIcon = itemView.iconView
    }
}