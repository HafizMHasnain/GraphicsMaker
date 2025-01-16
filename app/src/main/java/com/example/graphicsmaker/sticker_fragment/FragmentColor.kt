package com.example.graphicsmaker.sticker_fragment

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.graphicsmaker.R
import com.example.graphicsmaker.main.Constants
import com.example.graphicsmaker.main.OnGetImageOnTouch
import com.example.graphicsmaker.main.SelectImageTwoActivity
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener

class FragmentColor : Fragment() {
    private var bColor = 0
    var getImage: OnGetImageOnTouch? = null
    var hex: String = ""
    var img_color: ImageView? = null
    var screenWidth: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_color, container, false)
        this.img_color = view.findViewById<View>(R.id.img_color) as ImageView
        val dimension = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(dimension)
        this.screenWidth = dimension.widthPixels.toFloat()
        this.getImage = activity as OnGetImageOnTouch?
        try {
            if (SelectImageTwoActivity.hex == "no" || SelectImageTwoActivity.hex == "") {
                this.bColor = Color.parseColor("#4149b6")
                img_color!!.setBackgroundColor(this.bColor)
                (view.findViewById<View>(R.id.img_colorPicker) as ImageButton).setOnClickListener {
                    AmbilWarnaDialog(
                        this@FragmentColor.activity,
                        this@FragmentColor.bColor,
                        object : OnAmbilWarnaListener {
                            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                                this@FragmentColor.updateColor(color)
                            }

                            override fun onCancel(dialog: AmbilWarnaDialog) {
                            }
                        }).show()
                }
                img_color!!.setOnClickListener {
                    if (hex != "") {
                        getImage!!.ongetPosition(
                            "0",
                            "Color",
                            this@FragmentColor.hex,
                            null,
                            null,
                            "",
                            0,
                            "",
                            "hideVideo",
                            false
                        )
                    }
                }
                (view.findViewById<View>(R.id.textH) as TextView).typeface =
                    Constants.getHeaderTypeface(
                        requireActivity()
                    )
                (view.findViewById<View>(R.id.txtGal) as TextView).typeface =
                    Constants.getTextTypeface(
                        requireActivity()
                    )
                (view.findViewById<View>(R.id.img_) as TextView).typeface =
                    Constants.getTextTypeface(
                        requireActivity()
                    )
                return view
            }
            this.bColor = Color.parseColor("#" + SelectImageTwoActivity.hex)
            img_color!!.setBackgroundColor(this.bColor)
            (view.findViewById<View>(R.id.textH) as TextView).typeface =
                Constants.getHeaderTypeface(
                    requireActivity()
                )
            (view.findViewById<View>(R.id.txtGal) as TextView).typeface = Constants.getTextTypeface(
                requireActivity()
            )
            (view.findViewById<View>(R.id.img_) as TextView).typeface = Constants.getTextTypeface(
                requireActivity()
            )
            return view
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view
    }

    override fun setMenuVisibility(visible: Boolean) {
        if (visible) {
            try {
                AmbilWarnaDialog(activity, this.bColor, object : OnAmbilWarnaListener {
                    override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                        this@FragmentColor.updateColor(color)
                    }

                    override fun onCancel(dialog: AmbilWarnaDialog) {
                    }
                }).show()
            } catch (e: NullPointerException) {
            }
        }
        super.setMenuVisibility(visible)
    }

    private fun updateColor(color: Int) {
        this.bColor = color
        this.hex = Integer.toHexString(color)
        img_color!!.setBackgroundColor(Color.parseColor("#" + this.hex))
        getImage!!.ongetPosition("0", "Color", this.hex, null, null, "", 0, "", "hideVideo", false)
    }
}
