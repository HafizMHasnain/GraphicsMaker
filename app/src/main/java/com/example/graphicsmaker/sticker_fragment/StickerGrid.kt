package com.example.graphicsmaker.sticker_fragment

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
import com.example.graphicsmaker.utility.CustomSquareImageView

class StickerGrid(
    private val mContext: Context,
    var stkrName: Array<String>,
    var prefs: SharedPreferences,
    val lock: List<Int>
) : BaseAdapter() {

    val inflater: LayoutInflater = LayoutInflater.from(mContext)
    inner class ViewHolder(
        var image: CustomSquareImageView?,
        var img_lock: ImageView?,
    )

    override fun getCount(): Int {
        return stkrName.size
    }

    override fun getItem(position: Int): Any {
        return stkrName[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_snap, parent,false)
            holder = ViewHolder(
                img_lock = view.findViewById(R.id.img_lock),
                image = view.findViewById(R.id.thumbnail_image))
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }
        ViewLockImg(holder, position)
        fillImage(stkrName[position], holder.image)
        return view
    }

    private fun ViewLockImg(holder: ViewHolder, position: Int) {
        if (lock.contains(position)) {
            holder.img_lock!!.visibility = View.VISIBLE
        } else if (prefs.getBoolean("isAdsDisabled", false)) {
            holder.img_lock!!.visibility = View.GONE
        } else {
            holder.img_lock!!.visibility = View.GONE
        }
    }

    fun fillImage(id: String?, img: ImageView?) {
        Glide.with(this.mContext).load(JniUtils.decryptResourceJNI(this.mContext, id))
            .thumbnail(0.1f).dontAnimate().centerCrop().placeholder(
            R.drawable.loading2
        ).error(R.drawable.error2).into(
            img!!
        )
    }
}
