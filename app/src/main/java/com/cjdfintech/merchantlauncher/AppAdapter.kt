package com.cjdfintech.merchantlauncher

import android.content.ClipData
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.app_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class AppAdapter(val items: ArrayList<AppInfo>, val context: Context) : RecyclerView.Adapter<AppAdapter.ViewHolder>(), CustomItemTouchHelperListener {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.appTitleTv?.text = items[position].label
        holder.appIcon.setImageDrawable(items[position].icon)
        holder.appLayout.setOnClickListener {
            val intent = context.packageManager.getLaunchIntentForPackage(items.get(position).packageName.toString())
            context.startActivity(intent)

        }
    }

    override fun onCreateViewHolder(viewgroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.app_item, viewgroup, false))
    }

    override fun onItemDismiss(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appTitleTv = itemView.appNameTv
        val appIcon = itemView.iconView
        val appLayout = itemView.appLayout
    }
}