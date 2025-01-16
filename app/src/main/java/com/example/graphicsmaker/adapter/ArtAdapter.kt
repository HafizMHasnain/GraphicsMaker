package com.example.graphicsmaker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graphicsmaker.JniUtils
import com.example.graphicsmaker.R
import com.example.graphicsmaker.databinding.SampleFileBinding
import com.example.graphicsmaker.main.Constants

class ArtAdapter(val _context: Context, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<ArtAdapter.ArtVHolder>() {

    private val list = listOf<ArtItem>(
        ArtItem(_context.resources.getString(R.string.badge), Constants.badge_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.bakery), Constants.bakery_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.ribbon), Constants.ribbon_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.icon), Constants.icon_stkr_list[0]),
        ArtItem(
            _context.resources.getString(R.string.payment),
            Constants.payment_stkr_list[0]
        ),
        ArtItem(_context.resources.getString(R.string.beautyy), Constants.beauty_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.bistroo), Constants.bistro_stkr_list[0]),
        ArtItem(
            _context.resources.getString(R.string.profession),
            Constants.profession_stkr_list[0]
        ),
        ArtItem(_context.resources.getString(R.string.peopl), Constants.people_stkr_list[0]),
        ArtItem(
            _context.resources.getString(R.string.christianity),
            Constants.christianity_stkr_list[0]
        ),
        ArtItem(_context.resources.getString(R.string.pets), Constants.pets_stkr_list[0]),
        ArtItem(
            _context.resources.getString(R.string.letters),
            Constants.letters_stkr_list[0]
        ),
        ArtItem(
            _context.resources.getString(R.string.babymom),
            Constants.babymom_stkr_list[0]
        ),
        ArtItem(_context.resources.getString(R.string.fashion), Constants.fasion_stkr_list[0]),
        ArtItem(
            _context.resources.getString(R.string.business),
            Constants.business_stkr_list[0]
        ),
        ArtItem(_context.resources.getString(R.string.threeD), Constants.threed_stkr_list[0]),
        ArtItem(
            _context.resources.getString(R.string.corporate),
            Constants.corporate_stkr_list[0]
        ),
        ArtItem(
            _context.resources.getString(R.string.property),
            Constants.property_stkr_list[0]
        ),
        ArtItem(
            _context.resources.getString(R.string.restaurant_cafe),
            Constants.restaurant_cafe_stkr_list[0]
        ),
        ArtItem(_context.resources.getString(R.string.camera), Constants.camera_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.video), Constants.video_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.shapes), Constants.shape_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.circle), Constants.circle_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.leaf), Constants.leaf_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.social), Constants.social_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.party), Constants.party_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.text), Constants.text_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.ngo), Constants.ngo_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.sports), Constants.sport_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.square), Constants.square_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.star), Constants.star_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.toys), Constants.toys_stkr_list[0]),
        ArtItem(
            _context.resources.getString(R.string.butterfly),
            Constants.butterfly_stkr_list[0]
        ),
        ArtItem(_context.resources.getString(R.string.cars), Constants.cars_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.music), Constants.music_stkr_list[0]),
        ArtItem(
            _context.resources.getString(R.string.festival),
            Constants.festival_stkr_list[0]
        ),
        ArtItem(_context.resources.getString(R.string.tattoo), Constants.tattoo_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.flower), Constants.flower_stkr_list[0]),
        ArtItem(_context.resources.getString(R.string.heart), Constants.heart_stkr_list[0]),
        ArtItem(
            _context.resources.getString(R.string.halloween),
            Constants.halloween_stkr_list[0]
        ),
        ArtItem(
            _context.resources.getString(R.string.holiday),
            Constants.holiday_stkr_list[0]
        ),
        ArtItem(
            _context.resources.getString(R.string.animals_birds),
            Constants.animal_bird_stkr_list[0]
        ),
    )

    inner class ArtVHolder(val binding: SampleFileBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtVHolder {
        val sampleFileBinding = SampleFileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArtVHolder(sampleFileBinding)
    }

    override fun onBindViewHolder(holder: ArtVHolder, position: Int) {
        val item = list[position]
        holder.binding.artTitle.text = item.title
        fillImage(item.image, holder.binding.artImg)
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

    data class ArtItem(val title: String, val image: String)

}