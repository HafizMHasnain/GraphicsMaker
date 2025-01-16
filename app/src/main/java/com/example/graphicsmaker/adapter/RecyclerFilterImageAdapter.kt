package com.example.graphicsmaker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graphicsmaker.R

class RecyclerFilterImageAdapter(var context: Context, private var filterImageArr: Array<String>) :
    RecyclerView.Adapter<RecyclerFilterImageAdapter.ViewHolder>() {
    private var selected_position: Int = -1

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @JvmField
        var imageView: ImageView = view.findViewById<View>(R.id.item_image) as ImageView
        @JvmField
        var layout: LinearLayout = view.findViewById<View>(R.id.lay) as LinearLayout
        @JvmField
        var viewImage: ImageView = view.findViewById<View>(R.id.view_image) as ImageView
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return filterImageArr.size
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Glide.with(this.context).load(
            context.resources.getIdentifier(
                filterImageArr[position], "drawable", context.packageName
            )
        ).thumbnail(0.1f).dontAnimate().placeholder(R.drawable.loading2).error(
            R.drawable.error2)
            .into(viewHolder.imageView)
        if (this.selected_position == position) {
            viewHolder.viewImage.visibility = View.VISIBLE
        } else {
            viewHolder.viewImage.visibility = View.INVISIBLE
        }
        viewHolder.layout.setOnClickListener {
            this@RecyclerFilterImageAdapter.notifyItemChanged(
                this@RecyclerFilterImageAdapter.selected_position
            )
            this@RecyclerFilterImageAdapter.selected_position = position
            this@RecyclerFilterImageAdapter.notifyItemChanged(this@RecyclerFilterImageAdapter.selected_position)
        }
    }

    fun setSelected(nameIs: String): Int {
        var po = -1
        for (i in filterImageArr.indices) {
            if (filterImageArr[i] == nameIs) {
                po = i
                break
            }
        }
        this.selected_position = po
        notifyDataSetChanged()
        return po
    }

    override fun onCreateViewHolder(arg0: ViewGroup, position: Int): ViewHolder {
        val viewHolder = ViewHolder(
            LayoutInflater.from(arg0.context).inflate(R.layout.recycler_adapter, arg0, false)
        )
        arg0.id = position
        arg0.isFocusable = false
        arg0.isFocusableInTouchMode = false
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
