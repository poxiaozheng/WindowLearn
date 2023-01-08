package com.zzm.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AutoFillToolAdapter(
    private val dataList: List<String>,
    private val windowManager: WindowManager,
    private val params: WindowManager.LayoutParams,
    private val originView: View?
) :
    RecyclerView.Adapter<AutoFillToolAdapter.ViewHolder>() {

    private var isFold = true

    companion object {
        //默认展示一个
        private const val COUNT_1 = 1
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.fruitName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_item, parent, false)
        val viewHolder = ViewHolder(view)
        val detailsView = LayoutInflater.from(parent.context)
            .inflate(R.layout.details_view, parent, false)
        viewHolder.name.setOnClickListener {
            windowManager.removeView(originView)
            windowManager.addView(detailsView, params)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.name.text = data
    }

    fun setShowCount(isFold: Boolean) {
        this.isFold = isFold
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (isFold) {
            COUNT_1.coerceAtMost(dataList.size)
        } else {
            dataList.size
        }
    }
}