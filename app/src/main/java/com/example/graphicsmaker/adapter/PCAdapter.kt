package com.example.graphicsmaker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graphicsmaker.JniUtils
import com.example.graphicsmaker.R
import com.example.graphicsmaker.databinding.PickerGridItemVerticallyBinding

class PCAdapter(
    val _context: Context,
    val list: Array<String>,
    private val lock: List<Int>,
    private val fragemnt: String,
    val onItemClick: (Int) -> Unit,
    private val isPremiumActive : Boolean
) : RecyclerView.Adapter<PCAdapter.PCVHolder>() {

    inner class PCVHolder(val binding: PickerGridItemVerticallyBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PCVHolder {
        val picGridItem = PickerGridItemVerticallyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PCVHolder(picGridItem)
    }

    override fun onBindViewHolder(holder: PCVHolder, position: Int) {
        val item = list[position]
        fillImage(item, holder.binding.thumbnailImage)
        if (lock.contains(position) && !isPremiumActive){
            holder.binding.imgLock.visibility = View.VISIBLE
        } else holder.binding.imgLock.visibility = View.GONE
        holder.binding.root.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun fillImage(id: String?, img: ImageView?) {

        Glide.with(_context).load(JniUtils.decryptResourceJNI(this._context, id))
            .thumbnail(0.1f).dontAnimate().centerCrop().placeholder(
                R.drawable.loading2
            ).error(R.drawable.error2).into(
                img!!
            )
    }

}