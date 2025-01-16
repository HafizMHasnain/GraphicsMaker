package com.example.graphicsmaker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.graphicsmaker.JniUtils
import com.example.graphicsmaker.R
import com.example.graphicsmaker.premium.SubscriptionViewModel

class CatalogAdapter(
    private val context: Context,
    private val layoutResource: Int,
    private val resources: Array<String>,
    private val lockList: List<Int>,
    private val viewModel: SubscriptionViewModel
) : BaseAdapter() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = resources.size

    override fun getItem(position: Int): String = resources[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n", "DefaultLocale")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = layoutInflater.inflate(layoutResource, parent, false)

        val imageView = rowView.findViewById<ImageView>(R.id.catalog_imageview)
        val lock = rowView.findViewById<ImageView>(R.id.lock_image)

        // Fill the image
        fillImage(resources[position], imageView)

        // Check lock visibility safely
        val isPremiumActive = viewModel.isPremiumActive.value ?: false
        if (lockList.contains(position) && !isPremiumActive) {
            lock.visibility = View.VISIBLE
        } else {
            lock.visibility = View.GONE
        }

        return rowView
    }

    private fun fillImage(id: String, img: ImageView) {
        val decryptedResource = JniUtils.decryptResourceJNI(context, id)
        if (decryptedResource != null) {
            Glide.with(context)
                .load(decryptedResource)
                .thumbnail(0.1f)
                .dontAnimate()
                .centerCrop()
                .placeholder(R.drawable.loading2)
                .error(R.drawable.error2)
                .into(img)
        } else {
            img.setImageResource(R.drawable.error2)
        }
    }
}

