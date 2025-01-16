package com.example.graphicsmaker.sticker_fragment

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.graphicsmaker.R
import com.example.graphicsmaker.main.Constants
import com.example.graphicsmaker.main.OnGetImageOnTouch
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener

class FragmentGradient : Fragment(), View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    var colors: IntArray? = intArrayOf(Color.parseColor("#ffb400"), Color.parseColor("#f50c0c"))
    var getGradient: OnGetImageOnTouch? = null
    var imgArr: Array<ImageView?> = arrayOfNulls(4)
    var img_end: ImageView? = null
    var img_result: ImageView? = null
    var img_start: ImageView? = null
    var lay_orintation: LinearLayout? = null
    var lay_radius: LinearLayout? = null
    var ortnIs: GradientDrawable.Orientation? = null
    private var prog_radious = 50
    var radio_linear: RadioButton? = null
    var radio_radial: RadioButton? = null
    var ratioIs: String = "1:1"
    var seekBarRadial: SeekBar? = null
    var ttf: Typeface? = null
    var typeGradient: String? = "LINEAR"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gradient, container, false)
        this.img_result = view.findViewById<View>(R.id.img_result) as ImageView
        this.img_start = view.findViewById<View>(R.id.img_start) as ImageView
        this.img_end = view.findViewById<View>(R.id.img_end) as ImageView
        this.getGradient = activity as OnGetImageOnTouch?
        this.ortnIs = GradientDrawable.Orientation.TOP_BOTTOM
        img_start!!.setOnClickListener(this)
        img_end!!.setOnClickListener(this)
        if (arguments != null) {
            if (requireArguments().getString("typeGradient") == "") {
                img_start!!.setBackgroundColor(colors!![0])
                img_end!!.setBackgroundColor(colors!![1])
                this.typeGradient = "LINEAR"
                applyGradientBackground()
            } else {
                this.typeGradient = requireArguments().getString("typeGradient")
                this.colors = requireArguments().getIntArray("colorArr")
                this.ortnIs = requireArguments()["orintation"] as GradientDrawable.Orientation?
                this.prog_radious = requireArguments().getInt("prog_radious")
                if (this.colors != null) {
                    img_start!!.setBackgroundColor(colors!![0])
                    img_end!!.setBackgroundColor(colors!![1])
                    applyGradientBackground()
                }
            }
        }
        this.lay_orintation = view.findViewById<View>(R.id.lay_orintation) as LinearLayout
        this.lay_radius = view.findViewById<View>(R.id.lay_radius) as LinearLayout
        this.seekBarRadial = view.findViewById<View>(R.id.seek_radial) as SeekBar
        seekBarRadial!!.setOnSeekBarChangeListener(this)
        (view.findViewById<View>(R.id.img_1) as ImageButton).setOnClickListener(
            this
        )
        (view.findViewById<View>(R.id.img_3) as ImageButton).setOnClickListener(
            this
        )
        (view.findViewById<View>(R.id.img_5) as ImageButton).setOnClickListener(
            this
        )
        (view.findViewById<View>(R.id.img_6) as ImageButton).setOnClickListener(
            this
        )
        (view.findViewById<View>(R.id.img_flip) as ImageButton).setOnClickListener(
            this
        )
        (view.findViewById<View>(R.id.done_Go) as RelativeLayout).setOnClickListener(
            this
        )
        this.radio_linear = view.findViewById<View>(R.id.radio_linear) as RadioButton
        this.radio_radial = view.findViewById<View>(R.id.radio_radial) as RadioButton
        this.ttf = Constants.getTextTypeface(requireActivity())
        radio_linear!!.typeface = this.ttf
        radio_radial!!.typeface = this.ttf
        radio_linear!!.setOnClickListener(this)
        radio_radial!!.setOnClickListener(this)
        imgArr[0] = view.findViewById<View>(R.id.img_1) as ImageView
        imgArr[1] = view.findViewById<View>(R.id.img_3) as ImageView
        imgArr[2] = view.findViewById<View>(R.id.img_5) as ImageView
        imgArr[3] = view.findViewById<View>(R.id.img_6) as ImageView
        img_result!!.post {
            img_result!!.layoutParams.width =
                img_result!!.height
            img_result!!.postInvalidate()
            img_result!!.requestLayout()
        }
        seekBarRadial!!.progress = this.prog_radious
        if (this.typeGradient == "LINEAR") {
            radio_linear!!.isChecked = true
            radio_radial!!.isChecked = false
            lay_orintation!!.visibility = View.VISIBLE
            lay_radius!!.visibility = View.GONE
        } else {
            radio_linear!!.isChecked = false
            radio_radial!!.isChecked = true
            lay_orintation!!.visibility = View.GONE
            lay_radius!!.visibility = View.VISIBLE
        }
        (view.findViewById<View>(R.id.txt_Preview) as TextView).typeface = this.ttf
        (view.findViewById<View>(R.id.txt_StartColor) as TextView).typeface = this.ttf
        (view.findViewById<View>(R.id.txt_EndColor) as TextView).typeface = this.ttf
        (view.findViewById<View>(R.id.txt_Radius) as TextView).typeface = this.ttf
        (view.findViewById<View>(R.id.txt_Done) as TextView).typeface = this.ttf
        setOrientationImageSelection(this.ortnIs)
        return view
    }

    private fun setOrientationImageSelection(ortnIs: GradientDrawable.Orientation?) {
        if (ortnIs == GradientDrawable.Orientation.TOP_BOTTOM) {
            setSelected(R.id.img_1)
        } else if (ortnIs == GradientDrawable.Orientation.LEFT_RIGHT) {
            setSelected(R.id.img_3)
        } else if (ortnIs == GradientDrawable.Orientation.TL_BR) {
            setSelected(R.id.img_5)
        } else if (ortnIs == GradientDrawable.Orientation.TR_BL) {
            setSelected(R.id.img_6)
        }
    }

    override fun setMenuVisibility(visible: Boolean) {
        if (visible) {
            super.setMenuVisibility(visible)
        } else {
            super.setMenuVisibility(visible)
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.image1) {
            this.ratioIs = "1:1"
            applyGradientBackground()
            return
        } else if (id == R.id.image2) {
            this.ratioIs = "16:9"
            applyGradientBackground()
            return
        } else if (id == R.id.image3) {
            this.ratioIs = "9:16"
            applyGradientBackground()
            return
        } else if (id == R.id.image4) {
            this.ratioIs = "4:3"
            applyGradientBackground()
            return
        } else if (id == R.id.image5) {
            this.ratioIs = "3:4"
            applyGradientBackground()
            return
        } else if (id == R.id.img_start) {
            showColorPicker("start")
            return
        } else if (id == R.id.img_flip) {
            val colorCopy = colors!![0]
            colors!![0] = colors!![1]
            colors!![1] = colorCopy
            img_start!!.setBackgroundColor(colors!![0])
            img_end!!.setBackgroundColor(colors!![1])
            applyGradientBackground()
            return
        } else if (id == R.id.img_end) {
            showColorPicker("end")
            return
        } else if (id == R.id.radio_linear) {
            radio_linear!!.isChecked = true
            radio_radial!!.isChecked = false
            this.typeGradient = "LINEAR"
            lay_orintation!!.visibility = View.VISIBLE
            lay_radius!!.visibility = View.GONE
            applyGradientBackground()
            return
        } else if (id == R.id.radio_radial) {
            radio_linear!!.isChecked = false
            radio_radial!!.isChecked = true
            this.typeGradient = "RADIAL"
            lay_orintation!!.visibility = View.GONE
            lay_radius!!.visibility = View.VISIBLE
            applyGradientBackground()
            return
        } else if (id == R.id.done_Go) {
            getGradient!!.ongetPosition(
                "0", "Gradient", "", this.ortnIs, intArrayOf(
                    colors!![0],
                    colors!![1]
                ), this.typeGradient, this.prog_radious, this.ratioIs, "hideVideo", false
            )
            return
        } else if (id == R.id.img_1) {
            this.ortnIs = GradientDrawable.Orientation.TOP_BOTTOM
            setSelected(R.id.img_1)
            applyGradientBackground()
            return
        } else if (id == R.id.img_3) {
            this.ortnIs = GradientDrawable.Orientation.LEFT_RIGHT
            setSelected(R.id.img_3)
            applyGradientBackground()
            return
        } else if (id == R.id.img_5) {
            this.ortnIs = GradientDrawable.Orientation.TL_BR
            setSelected(R.id.img_5)
            applyGradientBackground()
            return
        } else if (id == R.id.img_6) {
            this.ortnIs = GradientDrawable.Orientation.TR_BL
            setSelected(R.id.img_6)
            applyGradientBackground()
            return
        }
        return
    }

    private fun showColorPicker(str: String) {
        val bColor = if (str == "start") {
            colors!![0]
        } else {
            colors!![1]
        }
        AmbilWarnaDialog(activity, bColor, object : OnAmbilWarnaListener {
            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                this@FragmentGradient.updateColor(color, str)
            }

            override fun onCancel(dialog: AmbilWarnaDialog) {
            }
        }).show()
    }

    private fun updateColor(color: Int, strIs: String) {
        if (strIs == "start") {
            colors!![0] = color
            img_start!!.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(color)))
        } else {
            colors!![1] = color
            img_end!!.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(color)))
        }
        applyGradientBackground()
    }

    private fun applyGradientBackground() {
        val gradientDrawable = GradientDrawable(
            this.ortnIs, intArrayOf(
                colors!![0],
                colors!![1]
            )
        )
        gradientDrawable.mutate()
        val btm: Bitmap?
        if (this.typeGradient == "LINEAR") {
            gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            btm = Constants.drawableToBitmap(gradientDrawable, 160, 160)
            if (btm != null) {
                img_result!!.setImageBitmap(btm)
                return
            }
            return
        }
        btm = Bitmap.createBitmap(160, 160, Bitmap.Config.ARGB_8888)
        if (btm != null) {
            getBitmap(gradientDrawable, btm)
        }
    }

    private fun getBitmap(gradientDrawable: GradientDrawable, bit: Bitmap) {
        gradientDrawable.gradientType = GradientDrawable.RADIAL_GRADIENT
        gradientDrawable.gradientRadius =
            ((bit.width * this.prog_radious) / 100).toFloat()
        img_result!!.setImageBitmap(
            Constants.drawableToBitmap(
                gradientDrawable,
                bit.width,
                bit.height
            )
        )
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (progress > 0) {
            this.prog_radious = progress
            applyGradientBackground()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

    fun setSelected(id: Int) {
        for (i in imgArr.indices) {
            if (imgArr[i]!!.id == id) {
                imgArr[i]!!.setBackgroundResource(R.drawable.down)
            } else {
                imgArr[i]!!.setBackgroundResource(R.drawable.down)
            }
        }
    }
}
