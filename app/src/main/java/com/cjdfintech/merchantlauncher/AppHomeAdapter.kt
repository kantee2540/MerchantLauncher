package com.cjdfintech.merchantlauncher

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.app_item.view.*
import kotlin.collections.ArrayList

class AppHomeAdapter(val items: ArrayList<AppInfo>, val context: Context) : RecyclerView.Adapter<AppHomeAdapter.ViewHolder>() {

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
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.app_home_item, viewgroup, false))
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appTitleTv = itemView.appNameTv
        val appIcon = itemView.iconView
        val appLayout = itemView.appLayout
    }
}