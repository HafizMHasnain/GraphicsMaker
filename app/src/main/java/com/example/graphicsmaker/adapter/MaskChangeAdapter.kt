package com.example.graphicsmaker.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graphicsmaker.R
import com.example.graphicsmaker.databinding.MaskableItemBinding

class MaskChangeAdapter(var imgBtmap: Bitmap?,val resId: Int,val onChangeMask: (PorterDuff.Mode) -> Unit) : RecyclerView.Adapter<MaskChangeAdapter.MaskVHolder>() {

   private val list = listOf(
        PorterDuff.Mode.DST_IN,
        PorterDuff.Mode.DST,
        PorterDuff.Mode.DST_ATOP,
        PorterDuff.Mode.DST_IN,
        PorterDuff.Mode.DST_OUT,
        PorterDuff.Mode.DST_OVER,
        PorterDuff.Mode.SRC,
        PorterDuff.Mode.SRC_ATOP,
        PorterDuff.Mode.SRC_IN,
        PorterDuff.Mode.SRC_OUT,
        PorterDuff.Mode.SRC_OVER,
        PorterDuff.Mode.ADD,
        PorterDuff.Mode.MULTIPLY,
        PorterDuff.Mode.DARKEN,
        PorterDuff.Mode.OVERLAY,
        PorterDuff.Mode.XOR,
        PorterDuff.Mode.CLEAR,
        )

    inner class MaskVHolder(val binding: MaskableItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaskVHolder {
        val binding = MaskableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaskVHolder(binding)
    }

    override fun onBindViewHolder(holder: MaskVHolder, position: Int) {

        val mode = list[position]
        holder.binding.resImg.setOnClickListener {
            onChangeMask.invoke(mode)
        }

        imgBtmap = imgBtmap ?: BitmapFactory.decodeResource(holder.binding.resImg.resources, R.drawable.background)
        holder.binding.resImg.setImageBitmap(imgBtmap)
        holder.binding.maskFrame.setMask(resId)
        if (holder.binding.maskFrame.getPorterDuffXferMode() != mode) {
            holder.binding.maskFrame.setPorterDuffXferMode(mode)
        }
        holder.binding.modeName.text = mode.name

    }

    override fun getItemCount(): Int {
        return list.size
    }
}