package com.example.graphicsmaker.sticker_fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.graphicsmaker.R
import com.example.graphicsmaker.main.Constants
import java.io.File

class FragmentImage : Fragment() {
    var f: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        (view.findViewById<View>(R.id.btn_cam) as Button).setOnClickListener {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            f = File(Environment.getExternalStorageDirectory(), "temp.jpg")
            intent.putExtra("output", Uri.fromFile(f))
            requireActivity().startActivityForResult(intent, SELECT_PICTURE_FROM_CAMERA)
        }
        (view.findViewById<View>(R.id.btn_gal) as Button).setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction("android.intent.action.PICK")
            requireActivity().startActivityForResult(
                Intent.createChooser(
                    intent, requireActivity().resources.getString(
                        R.string.select_picture
                    )
                ), SELECT_PICTURE_FROM_GALLERY
            )
        }
        (view.findViewById<View>(R.id.textH) as TextView).typeface = Constants.getHeaderTypeface(
            requireActivity()
        )
        (view.findViewById<View>(R.id.txtCam) as TextView).typeface = Constants.getTextTypeface(
            requireActivity()
        )
        (view.findViewById<View>(R.id.txtGal) as TextView).typeface = Constants.getTextTypeface(
            requireActivity()
        )
        return view
    }

    companion object {
        private const val SELECT_PICTURE_FROM_CAMERA = 9062
        private const val SELECT_PICTURE_FROM_GALLERY = 9072
    }
}
