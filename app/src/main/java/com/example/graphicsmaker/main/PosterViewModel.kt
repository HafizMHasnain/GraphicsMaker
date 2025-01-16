package com.example.graphicsmaker.main

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

class PosterViewModel: ViewModel() {
    private var _myBitmap: Bitmap? = null
    private var _typeOfDesign: String? = null

    var myBitmap: Bitmap?
        get() = _myBitmap
        set(value) {
            _myBitmap = value
        }

    var typeOfDesign: String?
        get() = _typeOfDesign
        set(value) {
            _typeOfDesign = value
        }
}