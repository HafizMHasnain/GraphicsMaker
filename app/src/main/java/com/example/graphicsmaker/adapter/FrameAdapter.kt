package com.example.graphicsmaker.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.graphicsmaker.JniUtils
import com.example.graphicsmaker.R
import com.example.graphicsmaker.utility.CustomSquareFrameLayout
import com.example.graphicsmaker.utility.CustomSquareImageView

class FrameAdapter(
    private val mContext: Context,
    private val valArr: Array<String>,
    private val sharedDefault: SharedPreferences,
    private val sharedUser: SharedPreferences,
    private val lock: List<Int>,
    private val fragemnt: String
) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(mContext)

    private class ViewHolder(
        val imgLock: ImageView?,
        val thumbnail: CustomSquareImageView?,
        val root: CustomSquareFrameLayout?
    )


    override fun getCount(): Int = valArr.size

    override fun getItem(position: Int): Any = valArr[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.picker_grid_item_gallery_thumbnail, parent, false)
            holder = ViewHolder(
                imgLock = view.findViewById(R.id.img_lock),
                thumbnail = view.findViewById(R.id.thumbnail_image),
                root = view.findViewById(R.id.root)
            )


            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        // Load image using Glide
        Glide.with(mContext)
            .load(JniUtils.decryptResourceJNI(mContext, valArr[position]))
            .thumbnail(0.1f)
            .useAnimationPool(true)
            .centerCrop()
            .placeholder(R.drawable.loading2)
            .error(R.drawable.error2)
            .into(holder.thumbnail!!)

        // Set lock visibility
        holder.imgLock?.visibility = if (lock.contains(position)) {
            View.VISIBLE
        } else {
            View.GONE
        }


        return view
    }
}

