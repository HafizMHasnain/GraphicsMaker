package com.example.graphicsmaker.sticker_fragment

import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.example.graphicsmaker.R
import com.example.graphicsmaker.main.Constants

class FragmentStepTwo : Fragment(), View.OnClickListener {
    var editor: SharedPreferences.Editor? = null
    private val layArr = arrayOfNulls<TextView>(4)
    var lay_font1: RelativeLayout? = null
    var lay_font2: RelativeLayout? = null
    var lay_font3: RelativeLayout? = null
    var lay_font4: RelativeLayout? = null
    var onCallFragment: CallFragmentInterface? = null
    var prefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_two, container, false)
        this.editor = requireActivity().getSharedPreferences("MY_PREFS_NAME", 0).edit()
        this.prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", 0)
        this.onCallFragment = requireActivity() as CallFragmentInterface
        this.lay_font1 = view.findViewById<View>(R.id.lay_font1) as RelativeLayout
        this.lay_font2 = view.findViewById<View>(R.id.lay_font2) as RelativeLayout
        this.lay_font3 = view.findViewById<View>(R.id.lay_font3) as RelativeLayout
        this.lay_font4 = view.findViewById<View>(R.id.lay_font4) as RelativeLayout
        lay_font1!!.setOnClickListener(this)
        lay_font2!!.setOnClickListener(this)
        lay_font3!!.setOnClickListener(this)
        lay_font4!!.setOnClickListener(this)
        layArr[0] = view.findViewById<View>(R.id.txt_font1) as TextView
        layArr[1] = view.findViewById<View>(R.id.txt_font2) as TextView
        layArr[2] = view.findViewById<View>(R.id.txt_font3) as TextView
        layArr[3] = view.findViewById<View>(R.id.txt_font4) as TextView
        (view.findViewById<View>(R.id.txt_appname) as TextView).setTypeface(
            Constants.getTextTypeface(
                requireActivity()
            )
        )
        layArr[0]!!
            .setTypeface(Typeface.createFromAsset(requireActivity().assets, "akifont3.ttf"))
        layArr[1]!!
            .setTypeface(Typeface.createFromAsset(requireActivity().assets, "akifont5.ttf"))
        layArr[2]!!
            .setTypeface(Typeface.createFromAsset(requireActivity().assets, "akifont6.ttf"))
        layArr[3]!!
            .setTypeface(Typeface.createFromAsset(requireActivity().assets, "akifont8.ttf"))
        if (prefs?.getString("fontName", null) != null) {
            val fontName = prefs?.getString("fontName", null)
            if (fontName == "Calligraphy") {
                setSelected(R.id.txt_font1)
            } else if (fontName == "Contemporary") {
                setSelected(R.id.txt_font2)
            } else if (fontName == "Handwritten") {
                setSelected(R.id.txt_font3)
            } else if (fontName == "Modern") {
                setSelected(R.id.txt_font4)
            }
        }
        return view
    }

    override fun setMenuVisibility(visible: Boolean) {
        if (visible) {
            try {
                val fontName =
                    prefs!!.getString("fontName", null)
                if (fontName == "Calligraphy") {
                    setSelected(R.id.txt_font1)
                } else if (fontName == "Contemporary") {
                    setSelected(R.id.txt_font2)
                } else if (fontName == "Handwritten") {
                    setSelected(R.id.txt_font3)
                } else if (fontName == "Modern") {
                    setSelected(R.id.txt_font4)
                }
            } catch (e: NullPointerException) {
            }
        }
        super.setMenuVisibility(visible)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.lay_font1) {
            setFontName("Calligraphy", R.id.txt_font1)
            return
        } else if (id == R.id.lay_font2) {
            setFontName("Contemporary", R.id.txt_font2)
            return
        } else if (id == R.id.lay_font4) {
            setFontName("Modern", R.id.txt_font4)
            return
        } else if (id == R.id.lay_font3) {
            setFontName("Handwritten", R.id.txt_font3)
            return
        }
        return
    }

    private fun setFontName(fontName: String, ids: Int) {
        val companyName = prefs!!.getString("companyName", null)
        if (companyName == null || companyName == "") {
            Toast.makeText(
                requireActivity(),
                resources.getString(R.string.txt_companyNametoast),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (prefs!!.getString("tagLine", null) == null) {
            editor!!.putString("tagLine", "")
        }
        editor!!.putString("fontName", fontName)
        editor!!.commit()
        setSelected(ids)
        onCallFragment!!.onCallFragment("step2", "step3")
    }

    fun setSelected(id: Int) {
        for (i in layArr.indices) {
            if (layArr[i]!!.id == id) {
                layArr[i]!!.setTextColor(requireActivity().resources.getColor(vocsy.ads.R.color.colorPrimary))
            } else {
                layArr[i]!!.setTextColor(ViewCompat.MEASURED_STATE_MASK)
            }
        }
    }
}
