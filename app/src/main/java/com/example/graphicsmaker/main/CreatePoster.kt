package com.example.graphicsmaker.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.example.graphicsmaker.JniUtils
import com.example.graphicsmaker.R
import com.example.graphicsmaker.adapter.ArtAdapter
import com.example.graphicsmaker.adapter.AssetsGridMain
import com.example.graphicsmaker.adapter.MaskChangeAdapter
import com.example.graphicsmaker.adapter.RecyclerFilterImageAdapter
import com.example.graphicsmaker.adapter.RecyclerItemClickListener
import com.example.graphicsmaker.adapter.RecyclerTextBgAdapter
import com.example.graphicsmaker.adapter.StickerViewPagerAdapter
import com.example.graphicsmaker.adapter.filter_recycler_adapter
import com.example.graphicsmaker.create.BitmapDataObject
import com.example.graphicsmaker.create.DatabaseHandler
import com.example.graphicsmaker.create.MaskableFrameLayout
import com.example.graphicsmaker.create.TemplateInfo
import com.example.graphicsmaker.create.ViewTemplateCanvasFinal
import com.example.graphicsmaker.databinding.ActivityPosterBinding
import com.example.graphicsmaker.msl.demo.view.ComponentInfo
import com.example.graphicsmaker.msl.demo.view.ResizableStickerView
import com.example.graphicsmaker.msl.textmodule.AutofitTextRel
import com.example.graphicsmaker.msl.textmodule.TextActivity
import com.example.graphicsmaker.msl.textmodule.TextInfo
import com.example.graphicsmaker.premium.SubscriptionViewModel
import com.example.graphicsmaker.scale.SubsamplingScaleImageView
import com.example.graphicsmaker.sticker_fragment.GetSnapListener
import com.example.graphicsmaker.sticker_fragment.ListFragment
import com.example.graphicsmaker.toBitmap
import com.example.graphicsmaker.utility.ImageUtils
import com.example.graphicsmaker.utility.StorageConfiguration
import org.json.JSONException
import org.json.JSONObject
import uz.shift.colorpicker.OnColorChangedListener
import vocsy.ads.GoogleAds
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import kotlin.math.max

class CreatePoster : AppCompatActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener,
    View.OnDragListener, ResizableStickerView.TouchEventListener, AutofitTextRel.TouchEventListener,
    GetSnapListener, OnSetImageSticker, GetColorListener {


    private var _binding: ActivityPosterBinding? = null
    val binding get() = _binding!!
    var TAG: String = "CreatePoster"

    val viewModel: PosterViewModel by viewModels<PosterViewModel>()
    val subscriptionViewModel: SubscriptionViewModel by viewModels<SubscriptionViewModel>()

    var OneShow: Boolean = true

    var _adapter: StickerViewPagerAdapter? = null

    var adapter: AssetsGridMain? = null
    var adaptor_filter: filter_recycler_adapter? = null
    var adaptor_shape: RecyclerFilterImageAdapter? = null
    var adaptor_txtBg: RecyclerTextBgAdapter? = null
    var alpha: Int = 80
    private var animSlideDown: Animation? = null
    private var animSlideUp: Animation? = null
    var bgAlpha: Int = 0
    var bgColor: Int = 0
    var bgDrawable: String = "0"
    var bottomImgArr: Array<ImageView?> = arrayOfNulls(9)
    var bottomLayArr: Array<LinearLayout?> = arrayOfNulls(9)

    var checkTrans: Boolean = true
    var color_Type: String? = null
    var colors: IntArray? = null
    var colotType: String? = null
    var countSize: Int = 0

    var dialogIs: ProgressDialog? = null
    var dialogShow: Boolean = true
    var distance: Float = 0f
    var distanceScroll: Int = 0
    var dsfc: Int = 0
    private var editMode = false
    var editor: SharedPreferences.Editor? = null
    private var f: File? = null
    var focusedCopy: View? = null
    var focusedView: View? = null
    var fontName: String = ""
    var draName: String? = null
    var frame_Name: String? = ""
    var hex: String? = ""
    var holdSize: Float = 0.0f
    var imageId: Array<String>? = arrayOf(
        "btxt0",
        "btxt1",
        "btxt2",
        "btxt3",
        "btxt4",
        "btxt5",
        "btxt6",
        "btxt7",
        "btxt8",
        "btxt9",
        "btxt10",
        "btxt11",
        "btxt12",
        "btxt13",
        "btxt14",
        "btxt15",
        "btxt16",
        "btxt17",
        "btxt18",
        "btxt19",
        "btxt20",
        "btxt21",
        "btxt22",
        "btxt23",
        "btxt24"
    )
    private var imagePath = ""
    var imageType: String? = null
    private var imgBtmap: Bitmap? = null
    var file1: File? = null
    var initViewPager: Boolean = true
    private val layArr = arrayOfNulls<View>(6)
    var mDrawableName: String = ""

    var options: BitmapFactory.Options = BitmapFactory.Options()
    var orient: GradientDrawable.Orientation? = null
    var overlay_Name: String = ""
    var overlay_blur: Int = 0
    var overlay_opacty: Int = 0
    var pallete: Array<String>? = arrayOf(
        "#ffffff",
        "#cccccc",
        "#999999",
        "#666666",
        "#333333",
        "#000000",
        "#ffee90",
        "#ffd700",
        "#daa520",
        "#b8860b",
        "#b8860b",
        "#ccff66",
        "#adff2f",
        "#00fa9a",
        "#00ff7f",
        "#00ff00",
        "#32cd32",
        "#3cb371",
        "#99cccc",
        "#66cccc",
        "#339999",
        "#669999",
        "#006666",
        "#336666",
        "#ffcccc",
        "#ff9999",
        "#ff6666",
        "#ff3333",
        "#ff0033",
        "#cc0033"
    )
    var parentY: Float = 0f
    var pos: Int = 0
    var prefs: SharedPreferences? = null

    private var processs = 0
    var profile: String? = "no"
    var prog_radious: Int = 0
    var ratio: String? = "1:1"

    //    var remove_ad_pref: SharedPreferences? = null
    var saveImageSize: Float = 0.0f
    var saveTemplateAsync: SaveTemplateAsync? = null

    var screenHeight: Float = 0f
    var screenWidth: Float = 0f
    private var hr = 1.0f
    private var wr = 1.0f

    private var seekValue = 90
    var shadowColor: Int = Color.parseColor("#000000")
    var shadowProg: Int = 0
    var shap_Name: String = "shape_0"
    var showtailsSeek: Boolean = false
    var sizeFull: Int = 0
    var stkrColorSet: Int = Color.parseColor("#ffffff")
    var stkrName: Array<String>? = null
    var tAlpha: Int = 100
    var tColor: Int = Color.parseColor("#000000")
    var targetDensity: Int = 640
    var temp_Type: String? = ""
    var temp_path: String = ""
    var templateId_Save: Int = -1
    var template_id: Int = 0
    var textColorSet: Int = Color.parseColor("#ffffff")
    var touchChange: Boolean = false
    private var ttf: Typeface? = null
    private var ttfHeader: Typeface? = null
    var txtGravity: String = "C"
    var txtShapeList: HashMap<Int?, Any?>? = null
    var typeGradient: String? = ""
    var uriArry: ArrayList<String?> = ArrayList()
    var yAtLayoutCenter: Float = -1.0f


    inner class LoadingStickers :
        AsyncTask<String?, String?, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): Boolean {
            val dh = DatabaseHandler.getDbHandler(this@CreatePoster)
            val shapeInfoList = dh.getComponentInfoList(template_id, "SHAPE")
            val textInfoList = dh.getTextInfoList(template_id)
            val stickerInfoList = dh.getComponentInfoList(template_id, "STICKER")
            dh.close()
            txtShapeList = HashMap<Int?, Any?>()
            var it: Iterator<*> = textInfoList.iterator()
            while (it.hasNext()) {
                val ti = it.next() as TextInfo
                txtShapeList!![ti.order] = ti
            }
            it = stickerInfoList.iterator()
            while (it.hasNext()) {
                val ci = it.next() as ComponentInfo
                txtShapeList!![ci.order] = ci
            }
            return true
        }

        override fun onPostExecute(isDownloaded: Boolean) {
            super.onPostExecute(isDownloaded)

            if (txtShapeList!!.size == 0) {
                dialogIs!!.dismiss()
            }
            val sortedKeys: List<*> = ArrayList<Any?>(
                txtShapeList!!.keys
            )
            val len = sortedKeys.size
            for (j in 0 until len) {
                val obj = txtShapeList!![sortedKeys[j]]
                if (obj is ComponentInfo) {
                    val stkr_path = obj.stkR_PATH
                    var riv: ResizableStickerView
                    if ((stkr_path == "")) {
                        riv = ResizableStickerView(this@CreatePoster)
                        binding.txtStkrRel.addView(riv)
                        riv.setOnTouchCallbackListener(this@CreatePoster)
                        riv.optimizeScreen(
                            screenWidth,
                            screenHeight
                        )
                        riv.componentInfo = obj
                        riv.optimize(wr, hr)
                        riv.setMainLayoutWH(
                            binding.mainRel.width.toFloat(),
                            binding.mainRel.height.toFloat()
                        )
                        riv.setBorderVisibility(false)
                        sizeFull++
                    } else {
                        val pictureFileDir = StorageConfiguration.getDesignPath()
                        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
                            Log.d("", "Can't create directory to save image.")
                            Toast.makeText(
                                this@CreatePoster, resources.getString(
                                    R.string.create_dir_err
                                ), Toast.LENGTH_LONG
                            ).show()
                            return
                        } else if (StorageConfiguration.getDesignPath().exists()) {
                            file1 = File(stkr_path)
                            if (file1!!.exists()) {
                                riv = ResizableStickerView(this@CreatePoster)
                                binding.txtStkrRel.addView(riv)
                                riv.setOnTouchCallbackListener(this@CreatePoster)
                                riv.optimizeScreen(
                                    screenWidth,
                                    screenHeight
                                )
                                riv.componentInfo = obj
                                riv.optimize(wr, hr)
                                riv.setMainLayoutWH(
                                    binding.mainRel.width.toFloat(),
                                    binding.mainRel.height.toFloat()
                                )
                                riv.setBorderVisibility(false)
                                sizeFull++
                            } else {
                                if (file1!!.name.replace(".png", "").length < 7) {
                                    dialogShow = false
                                    SaveStickersAsync(obj).execute(draName)
                                } else {
                                    if (OneShow) {
                                        dialogShow = true
                                        errorDialogTempInfo("Hide", "", "", "")
                                        OneShow = false
                                    }
                                    sizeFull++
                                }
                            }
                        } else {
                            file1 = File(stkr_path)
                            if (file1!!.exists()) {
                                riv = ResizableStickerView(this@CreatePoster)
                                binding.txtStkrRel.addView(riv)
                                riv.setOnTouchCallbackListener(this@CreatePoster)
                                riv.optimizeScreen(
                                    screenWidth,
                                    screenHeight
                                )
                                riv.componentInfo = obj
                                riv.optimize(wr, hr)
                                riv.setMainLayoutWH(
                                    binding.mainRel.width.toFloat(),
                                    binding.mainRel.height.toFloat()
                                )
                                riv.setBorderVisibility(false)
                                sizeFull++
                            } else {
                                if (file1!!.name.replace(".png", "").length < 7) {
                                    dialogShow = false
                                    SaveStickersAsync(obj).execute(draName)
                                } else {
                                    if (OneShow) {
                                        dialogShow = true
                                        errorDialogTempInfo("Hide", "", "", "")
                                        OneShow = false
                                    }
                                    sizeFull++
                                }
                            }
                        }
                    }
                } else {
                    val rl = AutofitTextRel(this@CreatePoster)
                    binding.txtStkrRel.addView(rl)
                    rl.setTextInfo(obj as TextInfo?, false)
                    rl.optimize(wr, hr)
                    rl.setMainLayoutWH(
                        binding.mainRel.width.toFloat(),
                        binding.mainRel.height.toFloat()
                    )
                    rl.setOnTouchCallbackListener(this@CreatePoster)
                    rl.refreshText()
                    val finalObj = obj
                    rl.post {
                        rl.borderVisibility = true
                        rl.borderVisibility = false
                    }
                    fontName =
                        obj!!.fonT_NAME
                    tColor =
                        obj.texT_COLOR
                    shadowColor =
                        obj.shadoW_COLOR
                    shadowProg =
                        obj.shadoW_PROG
                    tAlpha =
                        obj.texT_ALPHA
                    bgDrawable =
                        obj.bG_DRAWABLE
                    bgAlpha =
                        obj.bG_ALPHA
                    bgColor =
                        obj.bG_COLOR
                    txtGravity =
                        obj.texT_GRAVITY
                    sizeFull++
                }
            }
            if (txtShapeList!!.size == sizeFull && dialogShow) {
                removeImageViewControll()
                setbottomLayerSelected(R.id.select_backgnd)
                dialogIs!!.dismiss()
            }
        }

    }

    inner class LoadingTemplateAsync :
        AsyncTask<String?, String?, Boolean>() {
        var templateIdPassed: Int = 0

        override fun onPreExecute() {
            super.onPreExecute()
            dialogIs = ProgressDialog(this@CreatePoster)
            dialogIs!!.setMessage(resources.getString(R.string.plzwait))
            dialogIs!!.setCancelable(false)
            dialogIs!!.show()
        }

        @RequiresApi(Build.VERSION_CODES.S)
        override fun doInBackground(vararg p0: String?): Boolean {
            templateIdPassed = p0[0]?.toInt() ?: 0
            val templateInfo = DatabaseHandler.getDbHandler(this@CreatePoster).getTemplateByID(
                templateIdPassed
            )
            template_id = templateInfo.templatE_ID
            viewModel.typeOfDesign = templateInfo.art_Type
            viewModel.myBitmap = templateInfo.art_BG
            frame_Name = templateInfo.framE_NAME
            shap_Name = templateInfo.shaP_NAME
            temp_path = templateInfo.temP_PATH
            ratio = templateInfo.ratio
            profile = templateInfo.profilE_TYPE
            val seekValue1 = templateInfo.seeK_VALUE
            if ((profile == "Gradient")) {
                try {
                    val json = JSONObject(templateInfo.tempcolor)
                    typeGradient = json.getString("Type")
                    val oorient = json.getString("Orient")
                    orient = GradientDrawable.Orientation.valueOf(oorient)
                    val Color1 = json.getInt("Color1")
                    val Color2 = json.getInt("Color2")
                    val iArr = intArrayOf(Color1, Color2)
                    colors = iArr
                    prog_radious = json.getInt("Prog_radius")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                hex = templateInfo.tempcolor
            }
            overlay_Name = templateInfo.overlaY_NAME
            overlay_opacty = templateInfo.overlaY_OPACITY
            overlay_blur = templateInfo.overlaY_BLUR
            seekValue = seekValue1.toInt()
            return true
        }

        override fun onPostExecute(isDownloaded: Boolean) {
            super.onPostExecute(isDownloaded)
            if (overlay_Name != "") {
                setBitmapOverlay(
                    resources.getIdentifier(
                        overlay_Name,
                        "drawable",
                        packageName
                    )
                )
                binding.recylrFilter.scrollToPosition(adaptor_filter!!.setSelected(overlay_Name))
            }
            binding.seek.progress = overlay_opacty
            binding.seekOpacity.progress = overlay_blur
            binding.backgroundImg.alpha =
                (overlay_blur.toFloat()) / 255.0f
            binding.seekTailys.progress = seekValue
            drawBackgroundImage(
                ratio,
                frame_Name,
                profile,
                "created"
            )
        }
    }

    inner class SaveStickersAsync(var objk: Any) :
        AsyncTask<String?, String?, Boolean>() {
        var stkr_path: String? = null

        override fun doInBackground(vararg params: String?): Boolean {
            val stkrName = params.get(0)
            stkr_path = (objk as ComponentInfo).stkR_PATH
            try {
                val bitmap = BitmapFactory.decodeResource(
                    resources,
                    resources.getIdentifier(
                        stkrName,
                        "drawable",
                        packageName
                    )
                )
                if (bitmap != null) {
                    return Constants.saveBitmapObject(this@CreatePoster, bitmap, stkr_path)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(isDownloaded: Boolean) {
            super.onPostExecute(isDownloaded)
            sizeFull++
            if (txtShapeList!!.size == sizeFull) {
                dialogShow = true
            }
            if (isDownloaded) {
                val riv = ResizableStickerView(this@CreatePoster)
                binding.txtStkrRel.addView(riv)
                riv.setOnTouchCallbackListener(this@CreatePoster)
                riv.optimizeScreen(screenWidth, screenHeight)
                riv.componentInfo = objk as ComponentInfo?
                riv.optimize(wr, hr)
                riv.setMainLayoutWH(
                    binding.mainRel.width.toFloat(),
                    binding.mainRel.height.toFloat()
                )
                riv.setBorderVisibility(false)
            }
            if (dialogShow) {
                dialogIs!!.dismiss()
            }
        }
    }

    inner class SaveTemplateAsync(var transparent: String, var dbWork: Boolean) :
        AsyncTask<String?, String?, Boolean>() {
        var dialogIs: ProgressDialog? = null
        var isIdsTemplate: Boolean = false
        var logo: Bitmap? = null

        init {
            if (!dbWork && !subscriptionViewModel.isPremiumActive.value!!) {
                binding.logoLl.visibility = View.VISIBLE
                logo = Bitmap.createBitmap(binding.by.drawable.toBitmap())
                binding.logoLl.visibility = View.INVISIBLE
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            dialogIs = ProgressDialog(this@CreatePoster)
            dialogIs!!.setMessage(resources.getString(R.string.plzwait))
            dialogIs!!.setCancelable(false)
            dialogIs!!.show()
        }

        override fun doInBackground(vararg p0: String?): Boolean {
            var saveImageIs = false
            if (dbWork) {
                Log.d(TAG, "dbWork: $dbWork")
                templateId_Save = insertDataTemplate("SAVEPICTURE", transparent).toInt()
            } else {
                try {
                    Log.d(TAG, "templateId_Save != -1: $templateId_Save")
                    if (templateId_Save != -1) {
                        Log.d(TAG, "templateId_Save != -1: $templateId_Save")
                        var saveImageCanvas: ViewTemplateCanvasFinal
                        var bitmap: Bitmap?
                        if (countSize == 1) {
                            targetDensity = 480
                        } else if (countSize == 2) {
                            targetDensity = 320
                        } else if (countSize == 3) {
                            targetDensity = 240
                        } else if (countSize == 4) {
                            targetDensity = 160
                        }
                        if (saveImageSize >= (512f)) {
                            when(viewModel.typeOfDesign){
                                "poster" -> {
                                    val width = saveImageSize/1.7778
                                    saveImageCanvas = ViewTemplateCanvasFinal(  this@CreatePoster,
                                        screenWidth,
                                        screenHeight,
                                        width.toFloat(),
                                        saveImageSize,
                                        this,
                                        false,
                                        targetDensity
                                    )

                                }
                                "card" -> {
                                    val sh = typeOfDesign(w = screenWidth.toInt(), h = screenHeight.toInt()).second
                                    val height = saveImageSize/1.7778
                                    saveImageCanvas = ViewTemplateCanvasFinal(  this@CreatePoster,
                                        screenWidth,
                                        sh.toFloat(),
                                        saveImageSize,
                                        height.toFloat(),
                                        this,
                                        false,
                                        targetDensity
                                    )

                                }
                                else -> {
                                    Log.d(TAG, "saveImageSize >= (512f) when else ")

                                    saveImageCanvas = ViewTemplateCanvasFinal(  this@CreatePoster,
                                        screenWidth,
                                        screenWidth,
                                        saveImageSize,
                                        saveImageSize,
                                        this,
                                        false,
                                        targetDensity
                                    )

                                }
                            }
                            bitmap =
                                saveImageCanvas.getTemplateBitmap(templateId_Save)
                        }
                        else {

                            when(viewModel.typeOfDesign){
                                "poster" -> {
                                    val width = 512.0/1.7778
                                    saveImageCanvas = ViewTemplateCanvasFinal(  this@CreatePoster,
                                        screenWidth,
                                        screenHeight,
                                        width.toFloat(),
                                        512f,
                                        this,
                                        false,
                                        targetDensity
                                    )

                                }
                                "card" -> {
                                    val sh = typeOfDesign(w = screenWidth.toInt(), h = screenHeight.toInt()).second
                                    val height = 512.0/1.7778
                                    saveImageCanvas = ViewTemplateCanvasFinal(  this@CreatePoster,
                                        screenWidth,
                                        sh.toFloat(),
                                        512f,
                                        height.toFloat(),
                                        this,
                                        false,
                                        targetDensity
                                    )

                                }
                                else -> {

                                    saveImageCanvas = ViewTemplateCanvasFinal(  this@CreatePoster,
                                        screenWidth,
                                        screenWidth,
                                        512f,
                                        512f,
                                        this,
                                        false,
                                        targetDensity
                                    )

                                }
                            }
                            bitmap =
                                saveImageCanvas.getTemplateBitmap(templateId_Save)
                        }
                        if (/*remove_ad_pref!!.getBoolean("removeWatermark", false)*/!subscriptionViewModel.isPremiumActive.value!!) {
                            isIdsTemplate = false
                        } else {
                            isIdsTemplate = true
                            bitmap =  saveImageCanvas.getWaterMarkedBitmap(logo!!)
                        }
                        if (saveImageSize < (512f)) {
                            bitmap =
                                Bitmap.createScaledBitmap(
                                    bitmap!!,
                                    saveImageSize.toInt(),
                                    saveImageSize.toInt(), false
                                )

                        }
                        saveImageIs = saveImage(this@CreatePoster, bitmap, true)
                        bitmap?.recycle()
                        if (logo != null) {
                            logo!!.recycle()
                            logo = null
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } catch (e2: OutOfMemoryError) {
                    e2.printStackTrace()
                }
            }
            return saveImageIs
        }

        override fun onPostExecute(isDownloaded: Boolean) {
            super.onPostExecute(isDownloaded)
            dialogIs!!.dismiss()
            Constants.freeMemory()
            if (dbWork) {
                if (screenWidth > 720.0f) {
                    showDialogSaveImage("Option")
                    return
                }

                saveImageSize = 512.0f
                saveUserLogoPicture(imageType!!, false)

            } else if (isDownloaded && imagePath != "") {
                val intent = Intent(this@CreatePoster, ShareImageActivity::class.java)
                intent.putExtra("uri", imagePath)
                if (!isIdsTemplate || templateId_Save == -1) {
                    intent.putExtra("templateId", -1)
                    intent.putExtra("saveImageSize", saveImageSize)
                } else {
                    intent.putExtra("templateId", templateId_Save)
                    intent.putExtra("saveImageSize", saveImageSize)
                }

                GoogleAds.getInstance().showRewardedAd(this@CreatePoster) {
                    intent.putExtra("way", viewModel.typeOfDesign)
                    startActivity(intent)
                    finish()
                }
            } else if (countSize == 0) {
                showDialogSaveImage("BestSize")
            } else if (countSize < 4) {
                countSize++
                saveImageSize = (saveImageSize * 80.0f) / 100.0f
                imageType?.let {
                    saveUserLogoPicture(
                        it,
                        false
                    )
                }
            } else {
                showDialogSaveImage("Error")
            }
        }
    }

    inner class SavebackgrundAsync() :
        AsyncTask<String?, String?, Boolean>() {
        private var crted: String? = null
        private var profile: String? = null
        private var ratio: String? = null

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): Boolean {
            val stkrName = params[0]
            ratio = params[1]
            profile = params[2]
            crted = params[3]
            try {
                val bitmap = BitmapFactory.decodeResource(
                    resources,
                    resources.getIdentifier(
                        stkrName,
                        "drawable",
                        packageName
                    )
                )
                if (bitmap != null) {
                    return Constants.saveBitmapObject(
                        this@CreatePoster,
                        bitmap,
                        temp_path
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(isDownloaded: Boolean) {
            super.onPostExecute(isDownloaded)
            if (isDownloaded) {
                try {
                    bitmapRatio(
                        ratio, profile, ImageUtils.getResampleImageBitmap(
                            Uri.parse(
                                temp_path
                            ),
                            this@CreatePoster,
                            (if (screenWidth < screenHeight) screenWidth else screenHeight).toInt()
                        ), crted
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                binding.txtStkrRel.removeAllViews()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(1024, 1024)
        _binding = ActivityPosterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (intent.extras?.getString("key") == "poster") {
            val mintent = Intent(this, SelectImageTwoActivity::class.java)
            mintent.putExtra("tabposition", 0)
            mintent.putExtra("hex", "no")
            startActivityForResult(mintent, 4)
        } else if (intent.extras?.getString("key") == "card") {
            val mintent = Intent(this, SelectImageTwoActivity::class.java)
            mintent.putExtra("tabposition", 1)
            mintent.putExtra("hex", "no")
            startActivityForResult(mintent, 4)
        }

        prefs = getSharedPreferences("MY_PREFS_NAME", 0)
        editor = getSharedPreferences("MY_PREFS_NAME", 0).edit()
        viewModel.typeOfDesign = prefs?.getString("typeOfDesign", "").toString()

        binding.title.text = viewModel.typeOfDesign
        binding.title.typeface = ttfHeader

        options.inScaled = false
        activity = this

        val displayMetrics = resources.displayMetrics
        screenWidth = displayMetrics.widthPixels.toFloat()
        screenHeight =
            displayMetrics.heightPixels.toFloat() - ImageUtils.dpToPx(this, 105).toFloat()

        intilization()
        ttfHeader = Constants.getHeaderTypeface(this)
        showTextFontRecycler()

        if (intent.getBooleanExtra("loadUserFrame", false)) {
            Log.e(TAG, "oncreate: if ${intent.data}")
            val extra = intent.extras
            ratio = extra!!.getString("ratio")
            profile = extra.getString("profile")
            hex = extra.getString("hex")
            drawBackgroundImage(ratio, "shape_0", profile, "nonCreated")
        } else {
            Log.e(TAG, "oncreate: else ${intent.data}")
            temp_Type = intent.extras!!.getString("Temp_Type")
            val intExtra = intent.getIntExtra("templateId", 0)
            binding.centerRel.post {
                LoadingTemplateAsync().execute(*arrayOf("" + intExtra))
            }
        }
        val colors = IntArray(pallete!!.size)
        for (i in colors.indices) {
            colors[i] = Color.parseColor(pallete!![i])
        }
        binding.horizontalPicker.colors = colors
        binding.horizontalPickerColor.colors = colors
        binding.shadowPickerColor.colors = colors
        binding.pickerBg.colors = colors
        binding.horizontalPicker.setSelectedColor(textColorSet)
        binding.horizontalPickerColor.setSelectedColor(stkrColorSet)
        binding.shadowPickerColor.setSelectedColor(colors[0])
        binding.pickerBg.setSelectedColor(colors[5])
        val color = binding.horizontalPicker.color
        val color1 = binding.horizontalPickerColor.color
        val color2 = binding.shadowPickerColor.color
        val color3 = binding.pickerBg.color
        updateColor(color)
        updateColor(color1)
        updateShadow(color2)
        updateBgColor(color3)
        val anonymousClass1: OnColorChangedListener = object : OnColorChangedListener {
            override fun onColorChanged(c: Int) {
                updateColor(c)
            }
        }
        val anonymousClass2: OnColorChangedListener = object : OnColorChangedListener {
            override fun onColorChanged(c: Int) {
                updateShadow(c)
            }
        }
        val anonymousClass3: OnColorChangedListener = object : OnColorChangedListener {
            override fun onColorChanged(c: Int) {
                updateBgColor(c)
            }
        }
        binding.horizontalPicker.setOnColorChangedListener(anonymousClass1)
        binding.horizontalPickerColor.setOnColorChangedListener(anonymousClass1)
        binding.shadowPickerColor.setOnColorChangedListener(anonymousClass2)
        binding.pickerBg.setOnColorChangedListener(anonymousClass3)

        loadRewardedVideoAd()

        setbottomLayerSelected(R.id.lay_shape)
        binding.btnBck1.setOnClickListener(this)
        binding.layScroll.setOnTouchListener { v, event -> true }
        val layoutParams = RelativeLayout.LayoutParams(-1, -2)
        layoutParams.addRule(18)
        binding.layScroll.layoutParams = layoutParams
        binding.layScroll.postInvalidate()
        binding.layScroll.requestLayout()

        binding.txtFonts.typeface = ttf
        binding.txtColors.typeface = ttf
        binding.txtShadow.typeface = ttf
        binding.txtBg.typeface = ttf
        binding.txtControls.typeface = ttf
        binding.txtTD.typeface = ttf
        binding.edit.typeface = ttf
        binding.dub2.typeface = ttf
        binding.txtTDS.typeface = ttf
        binding.txtheader1.typeface = ttf


    }


    fun showTextFontRecycler() {

        var newResId: Int = R.drawable.shape_1

//        val onChangeMask: (PorterDuff.Mode) -> Unit = { mode: PorterDuff.Mode ->
//            Toast.makeText(this,mode.name,Toast.LENGTH_LONG).show()
//            if (binding.pipFrame.visibility == View.VISIBLE){
//                binding.pipFrame.setPorterDuffXferMode(mode)
//            } else if (binding.pipOverlay.visibility == View.VISIBLE) {
//                binding.pipOverlay.setPorterDuffXferMode(mode)
//            } else binding.pipFrame.setPorterDuffXferMode(mode)
//
//        }

        val maskChangeAdapter =
            MaskChangeAdapter(imgBtmap = this.imgBtmap, resId = newResId) { mode: PorterDuff.Mode ->
//            Toast.makeText(this,mode.name,Toast.LENGTH_LONG).show()
                if (binding.pipFrame.visibility == View.VISIBLE) {
                    binding.pipFrame.setPorterDuffXferMode(mode)
                    binding.pipFrame.requestLayout()
                    binding.pipFrame.invalidate()
                } else if (binding.pipOverlay.visibility == View.VISIBLE) {
                    binding.pipOverlay.setPorterDuffXferMode(mode)
                    binding.pipOverlay.requestLayout()
                    binding.pipOverlay.invalidate()
                } else {
                    binding.pipFrame.setPorterDuffXferMode(mode)
                    binding.pipFrame.requestLayout()
                    binding.pipFrame.invalidate()
                }
            }

        binding.maskChangeRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.maskChangeRV.adapter = maskChangeAdapter

        adaptor_shape = RecyclerFilterImageAdapter(this, Constants.shapes_list)
        binding.recylrShape.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recylrShape.setHasFixedSize(true)
        binding.recylrShape.adapter = adaptor_shape
        binding.recylrShape.addOnItemTouchListener(RecyclerItemClickListener(
            this
        ) { view, position ->
            if (checkTrans) {
                binding.seekOpacity.progress = 255
                checkTrans = false
            }
            binding.imgOkHide.visibility = View.GONE
            shap_Name = Constants.shapes_list[position]
            temp_path = ""
            val resID = resources.getIdentifier(
                shap_Name,
                "drawable",
                packageName
            )
            newResId = resID
            binding.pipFrame.setMask(resID)
            binding.pipOverlay.setMask(resID)



        })
        adaptor_filter = filter_recycler_adapter(this, Constants.overlayImg)

        binding.recylrFilter.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recylrFilter.setHasFixedSize(true)
        binding.recylrFilter.adapter = adaptor_filter
        binding.recylrFilter.addOnItemTouchListener(RecyclerItemClickListener(
            this
        ) { view, position ->
            if (position != -1 && Constants.overlayImg.size != 0) {
                overlay_Name = Constants.overlayImg[position]
                binding.seek.visibility = View.VISIBLE
                setBitmapOverlay(
                    resources.getIdentifier(
                        overlay_Name,
                        "drawable",
                        packageName
                    )
                )
            }
        })
    }

    private fun drawBackgroundImage(
        ratio: String?,
        backgroundName: String?,
        profile: String?,
        crted: String
    ) {
        var resID: Int
        this.profile = profile
        binding.laySticker.visibility = View.GONE
        var btm: Bitmap? = null
        val bfo: BitmapFactory.Options
        val optsDownSample: BitmapFactory.Options

        if ((profile == "no")) {
            if ((frame_Name == "")) {
                Log.d(TAG, "profile:$profile if ")
                val f: Float
                bfo = BitmapFactory.Options()
                bfo.inJustDecodeBounds = true
                BitmapFactory.decodeResource(resources, R.drawable.shape_0, bfo)
                optsDownSample = BitmapFactory.Options()
                if (screenWidth < screenHeight) {
                    f = screenWidth
                } else {
                    f = screenHeight
                }
                optsDownSample.inSampleSize =
                    ImageUtils.getClosestResampleSize(bfo.outWidth, bfo.outHeight, f.toInt())
                bfo.inJustDecodeBounds = false
                optsDownSample.inScaled = false
                btm = BitmapFactory.decodeResource(resources, R.drawable.shape_0, optsDownSample)
            }
            else {
                Log.d(TAG, "profile:$profile else ")
                frame_Name = backgroundName
                if ((frame_Name!![0].toString() == "b")) {
                    Log.d(TAG, "profile:$profile else then if")
                    Log.d(TAG, frame_Name!![0].toString())
                    resID = resources.getIdentifier(backgroundName, "drawable", packageName)
                    bfo = BitmapFactory.Options()
                    bfo.inJustDecodeBounds = true
                    BitmapFactory.decodeResource(resources, resID, bfo)
                    optsDownSample = BitmapFactory.Options()
                    optsDownSample.inSampleSize = ImageUtils.getClosestResampleSize(
                        bfo.outWidth,
                        bfo.outHeight,
                        (if (screenWidth < screenHeight) screenWidth else screenHeight).toInt()
                    )
                    bfo.inJustDecodeBounds = false
                    optsDownSample.inScaled = false
                    btm = BitmapFactory.decodeResource(resources, resID, optsDownSample)
                } else {
                    Log.d(TAG, "profile:$profile else then else ")
                    showtailsSeek = true
                    btm = ImageUtils.getTiledBitmap(
                        this as Activity, frame_Name, binding.seekTailys,
                        typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).first,
                        typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).second
                    )
                }
            }
            temp_path = ""
        }
        else if ((profile == "Background")) {
            frame_Name = backgroundName
            temp_path = ""
            bfo = BitmapFactory.Options()
            bfo.inJustDecodeBounds = true
            val bytrArr = JniUtils.decryptResourceJNI(this as Context, backgroundName)
            BitmapFactory.decodeByteArray(bytrArr, 0, bytrArr.size, bfo)
            optsDownSample = BitmapFactory.Options()
            optsDownSample.inSampleSize = ImageUtils.getClosestResampleSize(
                bfo.outWidth,
                bfo.outHeight,
                (if (screenWidth < screenHeight) screenWidth else screenHeight).toInt()
            )
            bfo.inJustDecodeBounds = false
            optsDownSample.inScaled = false
            btm = BitmapFactory.decodeByteArray(bytrArr, 0, bytrArr.size, optsDownSample)
        }
        else if ((profile == "Texture")) {
            Log.d(TAG, "Texture")
            frame_Name = backgroundName
            temp_path = ""
            showtailsSeek = true
            btm = ImageUtils.getTiledBitmap(
                this as Activity, frame_Name, binding.seekTailys,
                screenWidth.toInt(),
                (screenWidth.toInt() - 0)
            )
        }
        else if ((profile == "Color")) {
            temp_path = ""
            binding.layHandletails.visibility = View.GONE
            val hex1 = hex
            val image = Bitmap.createBitmap(
                screenWidth.toInt(),
                screenWidth.toInt(), Bitmap.Config.ARGB_8888
            )
            image.eraseColor(Color.parseColor("#$hex1"))
            btm = image
        }
        else if ((profile == "Gradient")) {
            temp_path = ""
            binding.layHandletails.visibility = View.GONE
            val gradientDrawable = GradientDrawable(orient, colors)
            gradientDrawable.mutate()
            if ((typeGradient == "LINEAR")) {
                gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            } else {
                val bit = ImageUtils.resizeBitmap(
                    Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888),
                    screenWidth.toInt(),
                    screenWidth.toInt()
                )
                gradientDrawable.gradientType = GradientDrawable.RADIAL_GRADIENT
                if ((bit?.width ?: 0) > (bit?.height ?: 0)) {
                    gradientDrawable.gradientRadius =
                        ((bit.height * prog_radious) / 100).toFloat()
                } else {
                    if (bit.width < bit.height) {
                        gradientDrawable.gradientRadius =
                            ((bit.width * prog_radious) / 100).toFloat()
                    } else {
                        gradientDrawable.gradientRadius =
                            ((bit.width * prog_radious) / 100).toFloat()
                    }
                }
            }
            btm = Constants.drawableToBitmap(
                gradientDrawable,
                screenWidth.toInt(),
                screenWidth.toInt()
            )
        }
        else if ((profile == "Temp_Path")) {
            this.profile = "Temp_Path"
            if ((ratio == "")) {
                frame_Name = ""
            } else {
                frame_Name = backgroundName
            }
            val file2 = StorageConfiguration.getDesignPath()
            if (file2.exists()) {
                if ((crted == "nonCreated")) {
                    uriArry.clear()
                    for (absolutePath2: File in file2.listFiles()!!) {
                        uriArry.add(absolutePath2.absolutePath)
                    }
                }
                file1 = File(temp_path)
                if (file1!!.exists()) {
                    try {
                        bitmapRatio(
                            ratio, profile, ImageUtils.getResampleImageBitmap(
                                Uri.parse(
                                    temp_path
                                ),
                                this,
                                (if (screenWidth < screenHeight) screenWidth else screenHeight).toInt()
                            ), crted
                        )
                    } catch (e2: IOException) {
                        e2.printStackTrace()
                    }
                } else if (ratio != "") {
                    draName = file1!!.name.replace(".png", "")
                    SavebackgrundAsync().execute(*arrayOf(draName, ratio, profile, crted))
                } else if (OneShow) {
                    errorDialogTempInfo("View", ratio, profile, crted)
                    OneShow = false
                }
            } else {
                if ((crted == "nonCreated")) {
                    uriArry.clear()
                    for (absolutePath22: File in file2.listFiles()) {
                        uriArry.add(absolutePath22.absolutePath)
                    }
                }
                file1 = File(temp_path)
                if (file1!!.exists()) {
                    try {
                        bitmapRatio(
                            ratio, profile, ImageUtils.getResampleImageBitmap(
                                Uri.parse(
                                    temp_path
                                ),
                                this,
                                (if (screenWidth < screenHeight) screenWidth else screenHeight).toInt()
                            ), crted
                        )
                    } catch (e22: IOException) {
                        e22.printStackTrace()
                    }
                }
                else if (ratio != "") {
                    draName = file1!!.name.replace(".png", "")
                    SavebackgrundAsync().execute(*arrayOf(draName, ratio, profile, crted))
                } else if (OneShow) {
                    errorDialogTempInfo("View", ratio, profile, crted)
                    OneShow = false
                }
            }
        }
        else if ((profile == "Poster")) {
            binding.addPoster.startAnimation(animSlideUp)
            binding.addPoster.visibility = View.VISIBLE
            binding.addCard.startAnimation(animSlideDown)
            binding.addCard.visibility = View.GONE
            frame_Name = backgroundName
            btm = ImageUtils.getBitmapOfPosterCard(
                this as Activity, frame_Name
            )
            val newBitmap = ImageUtils.resizeBitmap(
                btm,
                typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).first,
                typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).second
            )
            viewModel.myBitmap = newBitmap

        }
        else if ((profile == "Card")) {
            binding.addCard.startAnimation(animSlideUp)
            binding.addCard.visibility = View.VISIBLE
            binding.addPoster.startAnimation(animSlideDown)
            binding.addPoster.visibility = View.GONE
            frame_Name = backgroundName
            btm = ImageUtils.getBitmapOfPosterCard(
                this as Activity, frame_Name
            )
            val newBitmap = ImageUtils.resizeBitmap(
                btm,
                typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).first,
                typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).second
            )
            viewModel.myBitmap = newBitmap
        } else return

        val iS = adaptor_shape!!.setSelected(shap_Name)
        if (iS == -1) {
            binding.imgOkHide.visibility = View.VISIBLE
        } else {
            binding.imgOkHide.visibility = View.GONE
        }
        binding.recylrShape.scrollToPosition(iS)

        resID = resources.getIdentifier(shap_Name, "drawable", packageName)
        binding.pipFrame.setMask(resID)
        binding.pipOverlay.setMask(resID)

        if (btm != null) {
            setImageBitmapAndResizeLayout(
                ImageUtils.resizeBitmap(
                    btm,
                    bitMapSize(profile = profile).first.toInt(),
                    bitMapSize(profile = profile).second.toInt()
                ), crted
            )
            btm.recycle()
        }
    }


    private fun typeOfDesign(w: Int, h: Int): Pair<Int, Int> {
        var pair: Pair<Int, Int>
        when (viewModel.typeOfDesign) {
            "poster" -> {
                Log.d(TAG, "typeOfDesign case: Poster")
                pair = Pair(w - 16, h - 16)
                return pair
            }

            "card" -> {
                Log.d(TAG, "typeOfDesign case: Card")
                val half = w / 2
                val one4th = half / 2
                val newH = one4th + half
                pair = Pair(w, newH)
                return pair
            }

            else -> {
                Log.d(TAG, "typeOfDesign case: else")
                pair = Pair(w, w)
                return pair
            }
        }
    }


    private fun bitMapSize(profile: String?): Pair<Float, Float> {
        val h = this.screenHeight
        val w = this.screenWidth
        var pair: Pair<Float, Float>
        when (profile) {
            "Poster" -> {
                Log.d(TAG, "bitMapSize case: Poster")
                pair = Pair(w,h)
                return pair
            }

            "Card" -> {
                Log.d(TAG, "bitMapSize case: Card")
                val half = w / 2
                val one4th = half / 2
                val newH = one4th + half
                pair = Pair(w, newH)
                return pair
            }

            else -> {
                Log.d(TAG, "bitMapSize case: else")
                if (viewModel.typeOfDesign == "poster"){
                    pair = Pair(w,w)
                    return pair
                } else if (viewModel.typeOfDesign == "card"){
                    val half = w / 2
                    val one4th = half / 2
                    val newH = one4th + half
                    pair = Pair(w, newH)
                    return pair
                } else {
                    pair = Pair(w, w)
                    return pair
                }
            }
        }
    }

    private fun setImageBitmapAndResizeLayout(bit: Bitmap, creted: String) {
        var bit = bit

        viewModel.myBitmap?.let {
            binding.imgCardPoster.layoutParams.height = it.height
            binding.imgCardPoster.layoutParams.width = it.width
            binding.imgCardPoster.postInvalidate()
            binding.imgCardPoster.requestLayout()
            binding.imgCardPoster.setImageBitmap(it)
            binding.imgCardPoster.visibility = View.VISIBLE
        }

        binding.mainRel.layoutParams.width = bit.width
        binding.mainRel.layoutParams.height = bit.height
        binding.mainRel.postInvalidate()
        binding.mainRel.requestLayout()
        binding.imgBackground.layoutParams.width = bit.width
        binding.imgBackground.layoutParams.height = bit.height
        binding.imgBackground.postInvalidate()
        binding.imgBackground.requestLayout()
        binding.backgroundImg.setImageBitmap(bit)
        binding.transImg.setImageBitmap(bit)
        imgBtmap = bit

        binding.mainRel.post {
            binding.layScroll.post {
                val los1 = IntArray(2)
                binding.layScroll.getLocationOnScreen(los1)
                parentY = los1[1].toFloat()
                yAtLayoutCenter = parentY

                removeScroll()

            }
        }
        try {
            val ow = bit.width.toFloat()
            val oh = bit.height.toFloat()
            bit = ImageUtils.resizeBitmap(
                bit,
                binding.centerRel.width,
                binding.centerRel.height
            )
            val nh = bit.height.toFloat()
            wr = (bit.width.toFloat()) / ow
            hr = nh / oh
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if ((creted == "created")) {
            binding.txtStkrRel.removeAllViews()
            LoadingStickers().execute(*arrayOf("" + template_id))
        }
    }

    private fun intilization() {
        when (viewModel.typeOfDesign) {
//            "poster" -> binding.by.setImageResource(R.drawable.businesscards_watermark_poster)
//            "card" -> binding.by.setImageResource(R.drawable.businesscards_watermark_card)
//            else -> binding.by.setImageResource(R.drawable.businesscards_watermark_logo)
        }

        binding.btnLayControls.setOnClickListener(this)
        ttf = Constants.getTextTypeface(this)
        binding.layTouchremove.setOnClickListener(this)

        bottomLayArr[0] = binding.addPoster
        bottomLayArr[1] = binding.addCard
        bottomLayArr[2] = binding.selectBackgnd
        bottomLayArr[3] = binding.layShape
        bottomLayArr[4] = binding.addSticker
        bottomLayArr[5] = binding.addFilter
        bottomLayArr[6] = binding.addText
        bottomLayArr[7] = binding.linearScale
        bottomLayArr[8] = binding.linearOpacity

        bottomImgArr[0] = binding.img1
        bottomImgArr[1] = binding.img2
        bottomImgArr[2] = binding.img3
        bottomImgArr[3] = binding.img4
        bottomImgArr[4] = binding.img5
        bottomImgArr[5] = binding.img9
        bottomImgArr[6] = binding.img6
        bottomImgArr[7] = binding.img7
        bottomImgArr[8] = binding.img8

//        binding.logoLl.setOnClickListener(this)
        binding.alphaSeekBar.setOnSeekBarChangeListener(this)
        binding.hueSeekBar.setOnSeekBarChangeListener(this)
        binding.XRoteSeekBar.setOnSeekBarChangeListener(this)
        binding.YRoteSeekBar.setOnSeekBarChangeListener(this)
        binding.ZRoteSeekBar.setOnSeekBarChangeListener(this)
        binding.ScaleSeekBar.setOnSeekBarChangeListener(this)
        binding.XTRoteSeekBar.setOnSeekBarChangeListener(this)
        binding.YTRoteSeekBar.setOnSeekBarChangeListener(this)
        binding.ZTRoteSeekBar.setOnSeekBarChangeListener(this)
        binding.CurveTRoteSeekBar.setOnSeekBarChangeListener(this)
        binding.seekTailys.setOnSeekBarChangeListener(this)
        binding.seekOpacity.setOnSeekBarChangeListener(this)
        binding.seekBar3.setOnSeekBarChangeListener(this)
        binding.o0.setOnClickListener(this)
        binding.btnUpdate.setOnClickListener(this)

        if (binding.layOpacity.visibility == View.GONE) {
            binding.layOpacity.startAnimation(animSlideUp)
            binding.layOpacity.visibility = View.VISIBLE
        } else if (binding.layTextMain.visibility == View.VISIBLE) {
            binding.layTextMain.startAnimation(animSlideDown)
            binding.layTextMain.visibility = View.GONE
        } else if (binding.linearBGs.visibility == View.GONE) {
            binding.linearBGs.startAnimation(animSlideUp)
            binding.linearBGs.visibility = View.VISIBLE
        } else if (binding.linearEffect.visibility == View.VISIBLE) {
            binding.linearEffect.startAnimation(animSlideDown)
            binding.linearEffect.visibility = View.GONE
        } else if (binding.layBgShapeContainer.visibility == View.VISIBLE) {
            binding.layBgShapeContainer.startAnimation(animSlideDown)
            binding.layBgShapeContainer.visibility = View.GONE
        } else if (binding.layHandletails.visibility == View.VISIBLE) {
            binding.layHandletails.startAnimation(animSlideDown)
            binding.layHandletails.visibility = View.GONE
        } else if (binding.layBgOpacity.visibility == View.VISIBLE) {
            binding.layBgOpacity.startAnimation(animSlideDown)
            binding.layBgOpacity.visibility = View.GONE
        } else if (binding.layArt.visibility == View.VISIBLE) {
            binding.layArt.startAnimation(animSlideDown)
            binding.layArt.visibility = View.GONE
        }

        binding.addPoster.setOnClickListener(this)
        binding.addCard.setOnClickListener(this)
        binding.selectBackgnd.setOnClickListener(this)
        binding.layShape.setOnClickListener(this)
        binding.addSticker.setOnClickListener(this)
        binding.addFilter.setOnClickListener(this)
        binding.addText.setOnClickListener(this)
        binding.linearScale.setOnClickListener(this)
        binding.linearOpacity.setOnClickListener(this)
        binding.layBg.setOnClickListener(this)
        binding.layTexture.setOnClickListener(this)
        binding.layGradient.setOnClickListener(this)
        binding.layUserImg.setOnClickListener(this)
        binding.layColrs.setOnClickListener(this)
        binding.seekBar3.progress = 0
        binding.seekBarShadow.setOnSeekBarChangeListener(this)
        binding.XRoteSeekBar.progress = 45
        binding.YRoteSeekBar.progress = 45
        binding.ZRoteSeekBar.progress = SubsamplingScaleImageView.ORIENTATION_180
        binding.ScaleSeekBar.progress = 10
        binding.XTRoteSeekBar.progress = 45
        binding.YTRoteSeekBar.progress = 45
        binding.ZTRoteSeekBar.progress = SubsamplingScaleImageView.ORIENTATION_180
        binding.CurveTRoteSeekBar.progress =
            ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION
        binding.seekBarShadow.progress = 0
        val bfo1 = BitmapFactory.Options()
        bfo1.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.o1, bfo1)
        val optsDownSample = BitmapFactory.Options()
        optsDownSample.inSampleSize = ImageUtils.getClosestResampleSize(
            bfo1.outWidth,
            bfo1.outHeight,
            (if (screenWidth < screenHeight) screenWidth else screenHeight).toInt()
        )
        bfo1.inJustDecodeBounds = false
        optsDownSample.inScaled = false
        try {
            binding.transImg.setImageBitmap(
                BitmapFactory.decodeResource(
                    resources, R.drawable.o1, optsDownSample
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.seek.max = 255
        binding.seek.progress = 80
        binding.transImg.imageAlpha = alpha
        binding.seek.setOnSeekBarChangeListener(this)
        binding.seekTailys.max = 290
        binding.seekTailys.progress = 90
        binding.seekOpacity.max = 255
        binding.seekOpacity.progress = 0
        binding.ScaleSeekBar.max = 10
        binding.backgroundImg.alpha = 0.0f

        binding.layDupliText.setOnClickListener(this)
        binding.layDupliStkr.setOnClickListener(this)
        binding.layEdit.setOnClickListener(this)

        binding.btnDone.setOnClickListener(this)
        setbottomLayerClick()
        binding.layTxtleft.setOnClickListener(this)
        binding.layTxtright.setOnClickListener(this)
        binding.layTxtcenter.setOnClickListener(this)
        binding.centerRel.setOnClickListener(this)
        animSlideUp = Constants.getAnimUp(this)
        animSlideDown = Constants.getAnimDown(this)

        binding.layTextEdit.setOnClickListener { }
        binding.seekbarContainer.setOnClickListener { }

        layArr[0] = binding.layControls
        layArr[1] = binding.layFonts
        layArr[2] = binding.layColors
        layArr[3] = binding.layShadow
        layArr[4] = binding.layBackgnd
        layArr[5] = binding.layTD
        setSelected(binding.layControls.id)

        binding.opctyTxtSeekbar.setOnSeekBarChangeListener(this)
        binding.opctyTxtSeekbar.progress = 100
        adapter = AssetsGridMain(this, resources.getStringArray(R.array.txtfont_array))
        binding.fontGridview.adapter = adapter
        binding.fontGridview.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                setTextFonts(adapter!!.getItem(position) as String)
                adapter!!.setSelected(position)
            }
        }
        adaptor_txtBg = RecyclerTextBgAdapter(this, imageId)

        binding.txtBgRecylr.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.txtBgRecylr.setHasFixedSize(true)
        binding.txtBgRecylr.adapter = adaptor_txtBg
        binding.txtBgRecylr.addOnItemTouchListener(
            RecyclerItemClickListener(
                this, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        setTextBgTexture(imageId!![position])
                    }
                })
        )

        binding.layColorOpacity.setOnClickListener(this)
        binding.layControlStkr.setOnClickListener(this)
        binding.layTDStkr.setOnClickListener(this)

        binding.txtBg.typeface = ttf
        binding.txtTexture.typeface = ttf
        binding.txtUserImg.typeface = ttf
        binding.txtGradient.typeface = ttf
        binding.txtColrs.typeface = ttf
        binding.txtCont.typeface = ttf
        binding.txtColOpacty.typeface = ttf
        binding.dub1.typeface = ttf


        if (viewModel.typeOfDesign == "poster") {

            binding.guidelinesTwo.setImageBitmap(
                ImageUtils.getTiledBitmap(
                    activity, R.drawable.gridtexture,
                    screenWidth.toInt(),
                     screenHeight.toInt(),
                    true
                )
            )
            binding.guidelinesTwo.layoutParams.height = screenHeight.toInt()
            binding.guidelinesTwo.layoutParams.width = screenWidth.toInt()
            binding.guidelinesTwo.invalidate()
            binding.guidelinesTwo.requestLayout()
        } else binding.guidelinesTwo.setImageBitmap(
            ImageUtils.getTiledBitmap(
                activity, R.drawable.gridtexture,
                typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).first,
                typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).second, true
            )
        )

        binding.toggleButton.setBackgroundResource(R.drawable.ic_layer_off)
        binding.toggleButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (binding.toggleButton.isChecked) {
                    binding.guidelinesTwo.visibility = View.VISIBLE
                    binding.toggleButton.setBackgroundResource(R.drawable.ic_lines)
                    return
                }
                binding.guidelinesTwo.visibility = View.GONE
                binding.toggleButton.setBackgroundResource(R.drawable.ic_layer_off)
            }
        })
        listFragment = ListFragment()
        listFragment!!.setRelativeLayout(
            binding.txtStkrRel,
            binding.btnLayControls,
            binding.layContainer
        )
        showFragment(listFragment)

        initArtViewPager()
    }


    fun setSelected(id: Int) {
        for (i in layArr.indices) {
            if (layArr[i]!!.id == id) {
                layArr[i]!!.setBackgroundResource(R.drawable.trans)
            } else {
                layArr[i]!!.setBackgroundResource(R.drawable.overlay)
            }
        }
    }

    fun setbottomLayerSelected(id: Int) {
        for (i in bottomLayArr.indices) {
        }
    }

    fun setbottomLayerClick() {
        for (onClickListener: LinearLayout? in bottomLayArr) {
            onClickListener!!.setOnClickListener(this)
        }
    }

    private fun showFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.lay_container, (fragment)!!, "fragment").commit()
    }

    // all clicks events
    override fun onClick(view: View) {
        val intent: Intent
        var i: Int
        var resizableStickerView: ResizableStickerView
        val id = view.id
//        if (id == R.id.logo_ll) {
//            startActivity(Intent(this@CreatePoster, SubscribePremium()::class.java))
//        }
//        else
        if (id == R.id.btn_bck) {
            binding.seekbarContainer.visibility = View.VISIBLE
            binding.layTextEdit.visibility = View.VISIBLE
            removeScroll()
            onBackPressed()
            return
        } else if (id == R.id.lay_touchremove) {
//            lay_effects.setVisibility(View.GONE);
            binding.guidelines.visibility = View.GONE
            onTouchApply()
            return
        } else if (id == binding.centerRel.id) {
            if (binding.layOpacity.visibility == View.VISIBLE) {
                binding.layOpacity.startAnimation(animSlideDown)
                binding.layOpacity.visibility = View.GONE
            }

            binding.layStkrMain.visibility = View.GONE
            binding.guidelines.visibility = View.GONE
            onTouchApply()
            return
        } else if (id == R.id.btn_layControls) {
            binding.seekbarContainer.visibility = View.VISIBLE
            binding.layTextEdit.visibility = View.VISIBLE
            removeScroll()
            removeImageViewControll()
            if (binding.layTextMain.visibility == View.VISIBLE) {
                binding.layTextMain.startAnimation(animSlideDown)
                binding.layTextMain.visibility = View.GONE
            }
            if (binding.layStkrMain.visibility == View.VISIBLE) {
                binding.layStkrMain.startAnimation(animSlideDown)
                binding.layStkrMain.visibility = View.GONE
            }
            if (binding.layContainer.visibility == View.GONE) {
                listFragment!!.getLayoutChild(true)
                binding.layContainer.visibility = View.VISIBLE
                binding.btnLayControls.visibility = View.GONE
                binding.layContainer.animate().translationX(binding.layContainer.left.toFloat())
                    .setDuration(200).setInterpolator(
                        DecelerateInterpolator()
                    ).start()
                return
            }
            binding.layContainer.animate().translationX((-binding.layContainer.right).toFloat())
                .setDuration(200).setInterpolator(
                    AccelerateInterpolator()
                ).start()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.layContainer.visibility = View.GONE
                binding.btnLayControls.visibility = View.VISIBLE
            }, 200)
            return
        } else if (id == binding.addPoster.id) {

            intent = Intent(this, SelectImageTwoActivity::class.java)
            intent.putExtra("tabposition", 0)
            intent.putExtra("hex", "no")
            startActivityForResult(intent, 4)
            return

        } else if (id == binding.addCard.id) {
            intent = Intent(this, SelectImageTwoActivity::class.java)
            intent.putExtra("tabposition", 1)
            intent.putExtra("hex", "no")
            startActivityForResult(intent, 5)
            return

        } else if (id == R.id.lay_bg) {

            intent = Intent(this, SelectImageTwoActivity::class.java)
            intent.putExtra("tabposition", 2)
            intent.putExtra("hex", "no")
            startActivityForResult(intent, 4)
            return
        } else if (id == R.id.lay_texture) {
            intent = Intent(this, SelectImageTwoActivity::class.java)
            intent.putExtra("tabposition", 3)
            intent.putExtra("hex", "no")
            startActivityForResult(intent, 4)
            return
        } else if (id == R.id.lay_UserImg) {
            intent = Intent(this, SelectImageTwoActivity::class.java)
            intent.putExtra("tabposition", 4)
            intent.putExtra("hex", "no")
            startActivityForResult(intent, 4)
            return
        } else if (id == R.id.lay_Gradient) {
            intent = Intent(this, SelectImageTwoActivity::class.java)
            intent.putExtra("tabposition", 5)
            intent.putExtra("hex", "no")
            intent.putExtra("typeGradient", typeGradient)
            intent.putExtra("colorArr", colors)
            intent.putExtra("orintation", orient)
            intent.putExtra("prog_radious", prog_radious)
            startActivityForResult(intent, 4)
            return
        } else if (id == R.id.lay_Colrs) {
            intent = Intent(this, SelectImageTwoActivity::class.java)
            intent.putExtra("tabposition", 6)
            intent.putExtra("hex", hex)
            startActivityForResult(intent, 4)
            return
        } else if (id == binding.selectBackgnd.id) {

            binding.layOpacity.visibility = View.VISIBLE
            binding.layOpacity.startAnimation(animSlideUp)

            binding.linearBGs.startAnimation(animSlideUp)
            binding.linearBGs.visibility = View.VISIBLE
            binding.linearEffect.startAnimation(animSlideDown)
            binding.linearEffect.visibility = View.GONE
            binding.layBgShapeContainer.startAnimation(animSlideDown)
            binding.layBgShapeContainer.visibility = View.GONE
            binding.layHandletails.startAnimation(animSlideDown)
            binding.layHandletails.visibility = View.GONE
            binding.layBgOpacity.startAnimation(animSlideDown)
            binding.layBgOpacity.visibility = View.GONE
            binding.layArt.startAnimation(animSlideDown)
            binding.layArt.visibility = View.GONE

            removeImageViewControll()
            removeScroll()
            binding.layStkrMain.visibility = View.GONE

            if (binding.layTextMain.visibility == View.VISIBLE) {
                binding.layTextMain.startAnimation(animSlideDown)
                binding.layTextMain.visibility = View.GONE
            }
            setbottomLayerSelected(R.id.select_backgnd)
            return

        } else if (id == binding.layShape.id) {
            removeScroll()
            removeImageViewControll()
            binding.btnLayControls.visibility = View.VISIBLE
            binding.layStkrMain.visibility = View.GONE

            binding.layOpacity.visibility = View.VISIBLE
            binding.layOpacity.startAnimation(animSlideUp)

            binding.layTextMain.startAnimation(animSlideDown)
            binding.layTextMain.visibility = View.GONE
            binding.linearBGs.startAnimation(animSlideDown)
            binding.linearBGs.visibility = View.GONE
            binding.linearEffect.startAnimation(animSlideDown)
            binding.linearEffect.visibility = View.GONE
            binding.layBgShapeContainer.startAnimation(animSlideUp)
            binding.layBgShapeContainer.visibility = View.VISIBLE
            binding.layHandletails.startAnimation(animSlideDown)
            binding.layHandletails.visibility = View.GONE
            binding.layBgOpacity.startAnimation(animSlideDown)
            binding.layBgOpacity.visibility = View.GONE
            binding.layArt.startAnimation(animSlideDown)
            binding.layArt.visibility = View.GONE

            setbottomLayerSelected(R.id.lay_shape)

        } else if (id == binding.addSticker.id) {
            initArtViewPager()
            removeScroll()
            removeImageViewControll()

            binding.layOpacity.visibility = View.VISIBLE
            binding.layOpacity.startAnimation(animSlideUp)

            binding.linearBGs.startAnimation(animSlideDown)
            binding.linearBGs.visibility = View.GONE
            binding.linearEffect.startAnimation(animSlideDown)
            binding.linearEffect.visibility = View.GONE
            binding.layBgShapeContainer.startAnimation(animSlideDown)
            binding.layBgShapeContainer.visibility = View.GONE
            binding.layHandletails.startAnimation(animSlideDown)
            binding.layHandletails.visibility = View.GONE
            binding.layBgOpacity.startAnimation(animSlideDown)
            binding.layBgOpacity.visibility = View.GONE
            binding.layArt.startAnimation(animSlideUp)
            binding.layArt.visibility = View.VISIBLE

            binding.btnLayControls.visibility = View.VISIBLE
            binding.layStkrMain.visibility = View.GONE
            binding.layTextMain.visibility = View.GONE
            setbottomLayerSelected(R.id.add_sticker)
        } else if (id == binding.addFilter.id) {

            setbottomLayerSelected(binding.addFilter.id)

            binding.layOpacity.visibility = View.VISIBLE
            binding.layOpacity.startAnimation(animSlideUp)

            binding.linearEffect.startAnimation(animSlideUp)
            binding.linearEffect.visibility = View.VISIBLE
            binding.linearBGs.startAnimation(animSlideDown)
            binding.linearBGs.visibility = View.GONE
            binding.layBgShapeContainer.startAnimation(animSlideDown)
            binding.layBgShapeContainer.visibility = View.GONE
            binding.layHandletails.startAnimation(animSlideDown)
            binding.layHandletails.visibility = View.GONE
            binding.layBgOpacity.startAnimation(animSlideDown)
            binding.layBgOpacity.visibility = View.GONE
            binding.layArt.startAnimation(animSlideDown)
            binding.layArt.visibility = View.GONE

//            if (binding.layOpacity.visibility == View.GONE) {
//                binding.layOpacity.visibility = View.VISIBLE
//                binding.layOpacity.startAnimation(animSlideUp)
//                setbottomLayerSelected(R.id.add_filter)
//            } else {
//                binding.layOpacity.startAnimation(animSlideDown)
//                binding.layOpacity.visibility = View.GONE
//                setbottomLayerSelected(0)
//            }
            return
        } else if (id == binding.addText.id) {

            binding.linearBGs.startAnimation(animSlideDown)
            binding.linearBGs.visibility = View.GONE
            binding.linearEffect.startAnimation(animSlideDown)
            binding.linearEffect.visibility = View.GONE
            binding.layBgShapeContainer.startAnimation(animSlideDown)
            binding.layBgShapeContainer.visibility = View.GONE
            binding.layHandletails.startAnimation(animSlideDown)
            binding.layHandletails.visibility = View.GONE
            binding.layBgOpacity.startAnimation(animSlideDown)
            binding.layBgOpacity.visibility = View.GONE
            binding.layArt.startAnimation(animSlideDown)
            binding.layArt.visibility = View.GONE

            binding.layTextMain.visibility = View.VISIBLE
            binding.layTextEdit.visibility = View.VISIBLE
            binding.layOpacity.visibility = View.GONE
            binding.layStkrMain.visibility = View.GONE

            removeScroll()
            removeImageViewControll()
            setbottomLayerSelected(R.id.add_text)
            openTextActivity()

            return
        } else if (id == binding.linearScale.id) {
            binding.linearBGs.startAnimation(animSlideDown)
            binding.linearBGs.visibility = View.GONE
            binding.linearEffect.startAnimation(animSlideDown)
            binding.linearEffect.visibility = View.GONE
            binding.layBgShapeContainer.startAnimation(animSlideDown)
            binding.layBgShapeContainer.visibility = View.GONE
            if (showtailsSeek) {
                binding.layHandletails.startAnimation(animSlideUp)
                binding.layHandletails.visibility = View.VISIBLE
            }

            binding.layBgOpacity.startAnimation(animSlideDown)
            binding.layBgOpacity.visibility = View.GONE
            binding.layArt.startAnimation(animSlideDown)
            binding.layArt.visibility = View.GONE

            binding.layOpacity.visibility = View.VISIBLE
            binding.layOpacity.startAnimation(animSlideUp)
            setbottomLayerSelected(R.id.linear_Scale)

            return
        } else if (id == binding.linearOpacity.id) {

            binding.layOpacity.visibility = View.VISIBLE
            binding.layOpacity.startAnimation(animSlideUp)
            binding.layBgOpacity.startAnimation(animSlideUp)
            binding.layBgOpacity.visibility = View.VISIBLE
            binding.linearBGs.startAnimation(animSlideDown)
            binding.linearBGs.visibility = View.GONE
            binding.linearEffect.startAnimation(animSlideDown)
            binding.linearEffect.visibility = View.GONE
            binding.layBgShapeContainer.startAnimation(animSlideDown)
            binding.layBgShapeContainer.visibility = View.GONE
            binding.layHandletails.startAnimation(animSlideDown)
            binding.layHandletails.visibility = View.GONE
            binding.layArt.startAnimation(animSlideDown)
            binding.layArt.visibility = View.GONE
            setbottomLayerSelected(R.id.linear_opacity)

            return
        } else if (id == R.id.s0) {
            if (checkTrans) {
                binding.seekOpacity.progress = 255
                checkTrans = false
            }
            shap_Name = "shape_0"
            temp_path = ""
            binding.pipFrame.setMask(R.drawable.squre)
            binding.pipOverlay.setMask(R.drawable.squre)
            adaptor_shape!!.setSelected("")
            binding.imgOkHide.visibility = View.VISIBLE
            return
        } else if (id == R.id.o0) {
            adaptor_filter!!.setSelected("")
//            if (binding.laySeekOpacty.visibility == View.VISIBLE) {
//                binding.laySeekOpacty.visibility = View.GONE
//            }
            overlay_Name = ""
            binding.seek.visibility = View.GONE
            binding.pipOverlay.visibility = View.GONE
            return
        } else if (id == R.id.btn_up_down) {
            focusedCopy = focusedView
            removeScroll()
            binding.layStkrMain.requestLayout()
            binding.layStkrMain.postInvalidate()
            if (binding.seekbarContainer.visibility == View.VISIBLE) {
                hideResContainer()
                return
            } else {
                showResContainer()
                return
            }
        } else if (id == R.id.lay_controlStkr) {
            binding.controlsShowStkr.visibility = View.VISIBLE
            binding.controlsShowStkr.startAnimation(animSlideUp)
            binding.layColorOacity.visibility = View.GONE
            binding.layColorOacity.startAnimation(animSlideDown)
            binding.tDShowStkr.visibility = View.GONE
            binding.tDShowStkr.startAnimation(animSlideDown)
            binding.layControlStkr.setBackgroundResource(R.drawable.trans)
            binding.layColorOpacity.setBackgroundResource(R.drawable.overlay)
            binding.layTDStkr.setBackgroundResource(R.drawable.overlay)
            return
        } else if (id == R.id.lay_colorOpacity) {

            binding.layHue.visibility = View.VISIBLE
            binding.controlsShowStkr.visibility = View.GONE
            binding.controlsShowStkr.startAnimation(animSlideDown)
            binding.layColorOacity.visibility = View.VISIBLE
            binding.layColorOacity.startAnimation(animSlideUp)
            binding.tDShowStkr.visibility = View.GONE
            binding.tDShowStkr.startAnimation(animSlideDown)
            binding.layColorOpacity.setBackgroundResource(R.drawable.trans)
            binding.layControlStkr.setBackgroundResource(R.drawable.overlay)
            binding.layTDStkr.setBackgroundResource(R.drawable.overlay)
            return
        } else if (id == R.id.lay_tDStkr) {
            binding.controlsShowStkr.visibility = View.GONE
            binding.controlsShowStkr.startAnimation(animSlideDown)
            binding.layColorOacity.visibility = View.GONE
            binding.layColorOacity.startAnimation(animSlideDown)
            binding.tDShowStkr.visibility = View.VISIBLE
            binding.tDShowStkr.startAnimation(animSlideUp)
            binding.layControlStkr.setBackgroundResource(R.drawable.overlay)
            binding.layColorOpacity.setBackgroundResource(R.drawable.overlay)
            binding.layTDStkr.setBackgroundResource(R.drawable.trans)
            return
        } else if (id == R.id.lay_dupliStkr) {
            val childCount = binding.txtStkrRel.childCount
            i = 0
            while (i < childCount) {
                val view1 = binding.txtStkrRel.getChildAt(i)
                if ((view1 is ResizableStickerView) && view1.borderVisbilty) {
                    resizableStickerView = ResizableStickerView(this)
                    resizableStickerView.setOnTouchCallbackListener(this)
                    resizableStickerView.componentInfo = view1.componentInfo
                    resizableStickerView.id = View.generateViewId()
                    resizableStickerView.setMainLayoutWH(
                        binding.mainRel.width.toFloat(),
                        binding.mainRel.height.toFloat()
                    )
                    binding.txtStkrRel.addView(resizableStickerView)
                    removeImageViewControll()
                    resizableStickerView.setBorderVisibility(true)
                }
                i++
            }
            return
        } else if (id == R.id.btnColor) {
            AmbilWarnaDialog(this, 0, object : OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    updateColor(color)
                }

                override fun onCancel(dialog: AmbilWarnaDialog) {
                }
            }).show()
            return
        } else if (id == R.id.btn_piclColorS) {
            val visiblePosition = removeBoderPosition
            removeImageViewControll()
            binding.imgBackground.visibility = View.VISIBLE
            bitmapNot = viewToBitmap(binding.mainRel)
            if (bitmapNot != null) {
                intent = Intent(this, PickColorImageActivity::class.java)
                intent.putExtra("way", "stkr")
                intent.putExtra("visiPosition", visiblePosition)
                intent.putExtra("color", stkrColorSet)
                startActivity(intent)
                return
            }
            return
        } else if (id == R.id.btn_txtColor1) {
            AmbilWarnaDialog(this, stkrColorSet, object : OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    updateColor(color)
                }

                override fun onCancel(dialog: AmbilWarnaDialog) {
                }
            }).show()
            return
        } else if (id == R.id.btn_up_down1) {
            focusedCopy = focusedView
            removeScroll()
            binding.layTextMain.requestLayout()
            binding.layTextMain.postInvalidate()
            if (binding.layTextEdit.visibility == View.VISIBLE) {
                hideTextResContainer()
                return
            } else {
                showTextResContainer()
                return
            }
        } else if (id == R.id.lay_controls) {
            binding.fontsShow.visibility = View.GONE
            binding.colorShow.visibility = View.GONE
            binding.sadowShow.visibility = View.GONE
            binding.bgShow.visibility = View.GONE
            binding.controlsShow.visibility = View.VISIBLE
            binding.tDShowText.visibility = View.GONE
            setSelected(R.id.lay_controls)
            return
        } else if (id == R.id.lay_fonts) {
            binding.fontsShow.visibility = View.VISIBLE
            binding.colorShow.visibility = View.GONE
            binding.sadowShow.visibility = View.GONE
            binding.bgShow.visibility = View.GONE
            binding.controlsShow.visibility = View.GONE
            binding.tDShowText.visibility = View.GONE
            setSelected(R.id.lay_fonts)
            return
        } else if (id == R.id.lay_colors) {
            binding.fontsShow.visibility = View.GONE
            binding.colorShow.visibility = View.VISIBLE
            binding.sadowShow.visibility = View.GONE
            binding.bgShow.visibility = View.GONE
            binding.controlsShow.visibility = View.GONE
            binding.tDShowText.visibility = View.GONE
            setSelected(R.id.lay_colors)
            return
        } else if (id == R.id.lay_shadow) {
            binding.fontsShow.visibility = View.GONE
            binding.colorShow.visibility = View.GONE
            binding.sadowShow.visibility = View.VISIBLE
            binding.bgShow.visibility = View.GONE
            binding.controlsShow.visibility = View.GONE
            binding.tDShowText.visibility = View.GONE
            setSelected(R.id.lay_shadow)
            return
        } else if (id == R.id.lay_backgnd) {
            binding.fontsShow.visibility = View.GONE
            binding.colorShow.visibility = View.GONE
            binding.sadowShow.visibility = View.GONE
            binding.bgShow.visibility = View.VISIBLE
            binding.controlsShow.visibility = View.GONE
            binding.tDShowText.visibility = View.GONE
            setSelected(R.id.lay_backgnd)
            return
        } else if (id == R.id.lay_tD) {
            binding.fontsShow.visibility = View.GONE
            binding.colorShow.visibility = View.GONE
            binding.sadowShow.visibility = View.GONE
            binding.bgShow.visibility = View.GONE
            binding.controlsShow.visibility = View.GONE
            binding.tDShowText.visibility = View.VISIBLE
            setSelected(R.id.lay_tD)
            return
        } else if (id == R.id.lay_txtleft) {
            binding.layTxtleft.setBackgroundResource(R.drawable.txtleft_c)
            binding.layTxtcenter.setBackgroundResource(R.drawable.txtcenter)
            binding.layTxtright.setBackgroundResource(R.drawable.txtright)
            setGravityText("L")
            return
        } else if (id == R.id.lay_txtcenter) {
            binding.layTxtleft.setBackgroundResource(R.drawable.txtleft)
            binding.layTxtcenter.setBackgroundResource(R.drawable.txtcenter_c)
            binding.layTxtright.setBackgroundResource(R.drawable.txtright)
            setGravityText("C")
            return
        } else if (id == R.id.lay_txtright) {
            binding.layTxtleft.setBackgroundResource(R.drawable.txtleft)
            binding.layTxtcenter.setBackgroundResource(R.drawable.txtcenter)
            binding.layTxtright.setBackgroundResource(R.drawable.txtright_c)
            setGravityText("R")
            return
        } else if (id == R.id.lay_dupliText) {
            val childCount1 = binding.txtStkrRel.childCount
            i = 0
            while (i < childCount1) {
                val view2 = binding.txtStkrRel.getChildAt(i)
                if ((view2 is AutofitTextRel) && view2.borderVisibility) {
                    val resizableStickerViewx = AutofitTextRel(this)
                    binding.txtStkrRel.addView(resizableStickerViewx)
                    resizableStickerViewx.setMainLayoutWH(
                        binding.mainRel.width.toFloat(),
                        binding.mainRel.height.toFloat()
                    )
                    removeImageViewControll()
                    resizableStickerViewx.setTextInfo(view2.textInfo, false)
                    resizableStickerViewx.id = View.generateViewId()
                    resizableStickerViewx.setOnTouchCallbackListener(this)
                    resizableStickerViewx.borderVisibility = true
                }
                i++
            }
            return
        } else if (id == R.id.lay_edit) {
            try {
                doubleTap()
                return
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }
        } else if (id == R.id.btn_piclColor) {
            val visiblePosition1 = removeBoderPosition
            removeImageViewControll()
            binding.imgBackground.visibility = View.VISIBLE
            bitmapNot = viewToBitmap(binding.mainRel)
            if (bitmapNot != null) {
                intent = Intent(this, PickColorImageActivity::class.java)
                intent.putExtra("way", "txtColor")
                intent.putExtra("visiPosition", visiblePosition1)
                intent.putExtra("color", textColorSet)
                startActivity(intent)
                return
            }
            return
        } else if (id == R.id.btn_txtColor) {
            AmbilWarnaDialog(this, textColorSet, object : OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    updateColor(color)
                }

                override fun onCancel(dialog: AmbilWarnaDialog) {
                }
            }).show()
            return
        } else if (id == R.id.btn_decShadow) {
            binding.seekBarShadow.progress -= 2
            return
        } else if (id == R.id.btn_incShadow) {
            binding.seekBarShadow.progress += 2
            return
        } else if (id == R.id.btn_piclColor2) {
            val visiblePosition2 = removeBoderPosition
            removeImageViewControll()
            binding.imgBackground.visibility = View.VISIBLE
            bitmapNot = viewToBitmap(binding.mainRel)
            if (bitmapNot != null) {
                intent = Intent(this, PickColorImageActivity::class.java)
                intent.putExtra("way", "txtShadow")
                intent.putExtra("visiPosition", visiblePosition2)
                intent.putExtra("color", shadowColor)
                startActivity(intent)
                return
            }
            return
        } else if (id == R.id.btn_txtColor2) {
            AmbilWarnaDialog(this, shadowColor, object : OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    updateShadow(color)
                }

                override fun onCancel(dialog: AmbilWarnaDialog) {
                }
            }).show()
            return
        } else if (id == R.id.txt_bg_none) {
            binding.seekBar3.progress = 0
            adaptor_txtBg!!.setSelected(500)
            val childCount6 = binding.txtStkrRel.childCount
            i = 0
            while (i < childCount6) {
                val view6 = binding.txtStkrRel.getChildAt(i)
                if ((view6 is AutofitTextRel) && view6.borderVisibility) {
                    view6.bgAlpha = 0
                }
                i++
            }
            return
        } else if (id == R.id.btn_piclColor3) {
            val visiblePosition3 = removeBoderPosition
            removeImageViewControll()
            binding.imgBackground.visibility = View.VISIBLE
            bitmapNot = viewToBitmap(binding.mainRel)
            if (bitmapNot != null) {
                intent = Intent(this, PickColorImageActivity::class.java)
                intent.putExtra("way", "txtBg")
                intent.putExtra("visiPosition", visiblePosition3)
                intent.putExtra("color", bgColor)
                startActivity(intent)
                return
            }
            return
        } else if (id == R.id.btn_txtColor3) {
            AmbilWarnaDialog(this, bgColor, object : OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                    updateBgColor(color)
                }

                override fun onCancel(dialog: AmbilWarnaDialog) {
                }
            }).show()
            return
        } else if (id == binding.btnBck1.id) {
            binding.layScroll.smoothScrollTo(0, distanceScroll)
            return
        } else if (id == binding.btnUpdate.id) {
            var primaryColor = 0
            var secondryColor = 0
            var StkrNameIs = ""
            val childCountt = binding.txtStkrRel.childCount
            var template_IDInt = 0
            var primaryTextId = ViewCompat.MEASURED_STATE_MASK
            var secondaryTextId = ViewCompat.MEASURED_STATE_MASK
            val dh = DatabaseHandler.getDbHandler(this)
            i = 0
            while (i < childCountt) {
                val child = binding.txtStkrRel.getChildAt(i)
                if (child is AutofitTextRel) {
                    val textInfo = child.textInfo
                    if ((textInfo.text == prefs!!.getString("companyName", null))) {
                        primaryColor = textInfo.texT_COLOR
                        primaryTextId = textInfo.texT_ID
                        template_IDInt = textInfo.templatE_ID
                    }
                    if ((textInfo.text == prefs!!.getString("tagLine", null))) {
                        secondryColor = textInfo.texT_COLOR
                        secondaryTextId = textInfo.texT_ID
                        template_IDInt = textInfo.templatE_ID
                    }
                } else {
                    StkrNameIs =
                        (binding.txtStkrRel.getChildAt(i) as ResizableStickerView).componentInfo.reS_ID
                }
                i++
            }
            Toast.makeText(this, "$StkrNameIs ,$primaryColor ,$secondryColor", Toast.LENGTH_LONG)
                .show()
            val success = dh.updateTemplateColor(
                StkrNameIs,
                primaryColor,
                secondryColor,
                template_IDInt,
                primaryTextId,
                secondaryTextId
            )
            return
        } else if (id == binding.btnDone.id) {
            binding.seekbarContainer.visibility = View.VISIBLE
            binding.layTextEdit.visibility = View.VISIBLE
            removeScroll()
            setbottomLayerSelected(0)
            removeImageViewControll()
            if (binding.layOpacity.visibility == View.VISIBLE) {
                binding.layOpacity.startAnimation(animSlideDown)
                binding.layOpacity.visibility = View.GONE
            }
            if (binding.layTextMain.visibility == View.VISIBLE) {
                binding.layTextMain.startAnimation(animSlideDown)
                binding.layTextMain.visibility = View.GONE
            }
            if (binding.layStkrMain.visibility == View.VISIBLE) {
                binding.layStkrMain.startAnimation(animSlideDown)
                binding.layStkrMain.visibility = View.GONE
            }
            if (binding.layContainer.visibility == View.VISIBLE) {
                binding.layContainer.animate().translationX((-binding.layContainer.right).toFloat())
                    .setDuration(200).setInterpolator(
                        AccelerateInterpolator()
                    ).start()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.layContainer.visibility = View.GONE
                    binding.btnLayControls.visibility = View.VISIBLE
                }, 200)
            }
            binding.guidelines.visibility = View.GONE
            showDialogSave()
            return
        }
        return
    }

    private fun setGravityText(gravity: String) {
        val childCount = binding.txtStkrRel.childCount
        for (i in 0 until childCount) {
            val view = binding.txtStkrRel.getChildAt(i)
            if ((view is AutofitTextRel) && view.borderVisibility) {
                view.textGravity = gravity
                txtGravity = gravity
            }
        }
    }

    private fun showResContainer() {
        binding.btnUpDown.setBackgroundResource(R.drawable.textlib_decdwn)
        binding.seekbarContainer.visibility = View.VISIBLE
        binding.layStkrMain.startAnimation(animSlideUp)
        binding.layStkrMain.requestLayout()
        binding.layStkrMain.postInvalidate()
        binding.layStkrMain.post { stickerScrollView(focusedView) }
    }

    private fun hideResContainer() {
        binding.btnUpDown.setBackgroundResource(R.drawable.textlib_incup)
        binding.layStkrMain.startAnimation(animSlideDown)
        binding.seekbarContainer.visibility = View.GONE
        binding.layStkrMain.requestLayout()
        binding.layStkrMain.postInvalidate()
        binding.layStkrMain.post { stickerScrollView(focusedView) }
    }

    private fun showTextResContainer() {
        binding.btnUpDown1.setBackgroundResource(R.drawable.textlib_decdwn)
        binding.layTextEdit.visibility = View.VISIBLE
        binding.layTextMain.startAnimation(animSlideUp)
        binding.layTextMain.requestLayout()
        binding.layTextMain.postInvalidate()
        binding.layTextMain.post { stickerScrollView(focusedView) }
    }

    private fun hideTextResContainer() {
        binding.btnUpDown1.setBackgroundResource(R.drawable.textlib_incup)
        binding.layTextMain.startAnimation(animSlideDown)
        binding.layTextEdit.visibility = View.GONE
        binding.layTextMain.requestLayout()
        binding.layTextMain.postInvalidate()
        binding.layTextMain.post { stickerScrollView(focusedView) }
    }

    private fun setTextBgTexture(mDrawableName: String) {
        val resID = resources.getIdentifier(mDrawableName, "drawable", packageName)
        val childCount = binding.txtStkrRel.childCount
        for (i in 0 until childCount) {
            val view = binding.txtStkrRel.getChildAt(i)
            if ((view is AutofitTextRel) && view.borderVisibility) {
                if (binding.seekBar3.progress == 0) {
                    binding.seekBar3.progress = 127
                }
                view.bgDrawable = mDrawableName
                view.bgAlpha = binding.seekBar3.progress
                bgColor = 0
                (binding.txtStkrRel.getChildAt(i) as AutofitTextRel).textInfo.bG_DRAWABLE =
                    mDrawableName
                bgDrawable = view.bgDrawable
                bgAlpha = binding.seekBar3.progress
            }
        }
    }

    private fun setTextFonts(fontName1: String) {
        fontName = fontName1
        val childCount = binding.txtStkrRel.childCount
        for (i in 0 until childCount) {
            val view = binding.txtStkrRel.getChildAt(i)
            if ((view is AutofitTextRel) && view.borderVisibility) {
                view.setTextFont(fontName1)
            }
        }
    }

    private fun setBitmapOverlay(resId: Int) {
        var oi: Bitmap?
        try {
            if (binding.laySeekOpacty.visibility == View.GONE) {
                binding.laySeekOpacty.visibility = View.VISIBLE
            }
            oi = BitmapFactory.decodeResource(resources, resId)
            binding.pipOverlay.visibility = View.VISIBLE
            binding.pipFrame.startAnimation(animSlideDown)
            binding.pipFrame.visibility = View.GONE

            binding.transImg.setImageBitmap(oi)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inSampleSize = 3
            options.inJustDecodeBounds = false
            oi = BitmapFactory.decodeResource(resources, resId, options)
            binding.pipOverlay.visibility = View.VISIBLE
            binding.transImg.setImageBitmap(oi)
            if (binding.laySeekOpacty.visibility == View.GONE) {
                binding.laySeekOpacty.visibility = View.VISIBLE
            }
        }
    }

    private fun initArtViewPager() {
        binding.artRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.artRv.adapter = ArtAdapter(this) { itemClicked ->
            Log.d(TAG, "itemClicked at ArtAdapter:$itemClicked")
            if (initViewPager) {
                initViewPager(itemClicked)
//                initViewPager = false
            }

        }
    }

    private fun initViewPager(position: Int) {
        _adapter = StickerViewPagerAdapter(this@CreatePoster, supportFragmentManager)
        binding.imageviewPager.adapter = _adapter
        binding.tabs.setViewPager(binding.imageviewPager)
        binding.tabs.setTypeface(ttf, 1)
        binding.imageviewPager.currentItem = position
        binding.layOpacity.visibility = View.GONE
        binding.laySticker.visibility = View.VISIBLE

        binding.tabs.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun updateColor(color: Int) {
        val childCount = binding.txtStkrRel.childCount
        for (i in 0 until childCount) {
            val view = binding.txtStkrRel.getChildAt(i)
            if ((view is AutofitTextRel) && view.borderVisibility) {
                view.textColor = color
                tColor = color
                textColorSet = color
                binding.horizontalPicker.setSelectedColor(color)
            }
            if ((view is ResizableStickerView) && view.borderVisbilty) {
                view.color = color
                stkrColorSet = color
                binding.horizontalPickerColor.setSelectedColor(color)
            }
        }
    }

    private fun updateShadow(color: Int) {
        val childCount = binding.txtStkrRel!!.childCount
        for (i in 0 until childCount) {
            val view = binding.txtStkrRel!!.getChildAt(i)
            if ((view is AutofitTextRel) && view.borderVisibility) {
                if (binding.seekBarShadow.progress == 0) {
                    binding.seekBarShadow.progress = 5
                }
                view.textShadowColor = color
                shadowColor = color
            }
        }
    }

    private fun updateBgColor(color: Int) {
        val childCount = binding.txtStkrRel!!.childCount
        for (i in 0 until childCount) {
            val view = binding.txtStkrRel!!.getChildAt(i)
            if ((view is AutofitTextRel) && view.borderVisibility) {
                if (binding.seekBar3.progress == 0) {
                    binding.seekBar3.progress = 127
                }
                view.bgAlpha = binding.seekBar3.progress
                view.bgColor = color
                bgColor = color
                bgDrawable = "0"
            }
        }
    }

    private fun updatePositionSticker(incr: String) {
        val childCount = binding.txtStkrRel!!.childCount
        for (i in 0 until childCount) {
            val view = binding.txtStkrRel!!.getChildAt(i)
            if ((view is AutofitTextRel) && view.borderVisibility) {
                if ((incr == "incrX")) {
                    view.incrX()
                }
                if ((incr == "decX")) {
                    view.decX()
                }
                if ((incr == "incrY")) {
                    view.incrY()
                }
                if ((incr == "decY")) {
                    view.decY()
                }
            }
            if ((view is ResizableStickerView) && view.borderVisbilty) {
                if ((incr == "incrX")) {
                    view.incrX()
                }
                if ((incr == "decX")) {
                    view.decX()
                }
                if ((incr == "incrY")) {
                    view.incrY()
                }
                if ((incr == "decY")) {
                    view.decY()
                }
            }
            val pWidth = binding.mainRel!!.width.toFloat()
            val pHeight = binding.mainRel!!.height.toFloat()
            val vw = view.width
            var isInCenterX = false
            var isInCenterY = false
            val cx = (view.x + ((vw / 2).toFloat())).toInt()
            val cy = (view.y + ((view.height / 2).toFloat())).toInt()
            if ((cx.toFloat()) > (pWidth / 2.0f) - (1f) && (cx.toFloat()) < (pWidth / 2.0f) + (1f)) {
                isInCenterX = true
            }
            if ((cy.toFloat()) > (pHeight / 2.0f) - (1f) && (cy.toFloat()) < (pHeight / 2.0f) + (1f)) {
                isInCenterY = true
            }
            if (isInCenterX && isInCenterY) {
                binding.guidelines.setCenterValues(true, true)
            } else if (isInCenterX) {
                binding.guidelines.setCenterValues(true, false)
            } else if (isInCenterY) {
                binding.guidelines.setCenterValues(false, true)
            } else {
                binding.guidelines.setCenterValues(false, false)
            }
        }
    }

    private fun openTextActivity() {
        val i = Intent(this, TextActivity::class.java)
        val bundle = Bundle()
        bundle.putFloat(
            "X", ((binding.txtStkrRel.width / 2) - ImageUtils.dpToPx(
                this, 100
            )).toFloat()
        )
        bundle.putFloat(
            "Y", ((binding.txtStkrRel.height / 2) - ImageUtils.dpToPx(
                this, 100
            )).toFloat()
        )
        bundle.putInt(
            "wi",
            ImageUtils.dpToPx(this, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION)
        )
        bundle.putInt(
            "he",
            ImageUtils.dpToPx(this, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION)
        )
        bundle.putString("text", "")
        Log.e(TAG, "openTextActivity: fontName: $fontName")
        Log.e(TAG, "openTextActivity: tColor: $tColor")
        Log.e(TAG, "openTextActivity: tAlpha: $tAlpha")
        Log.e(TAG, "openTextActivity: shadowColor: $shadowColor")
        Log.e(TAG, "openTextActivity: shadowProg: $shadowProg")
        Log.e(TAG, "openTextActivity: bgDrawable: $bgDrawable")
        Log.e(TAG, "openTextActivity: bgColor: $bgColor")
        Log.e(TAG, "openTextActivity: bgAlpha: $bgAlpha")
        Log.e(TAG, "openTextActivity: txtGravity: $txtGravity")
        bundle.putString("fontName", fontName)
        bundle.putInt("tColor", tColor)
        bundle.putInt("tAlpha", tAlpha)
        bundle.putInt("shadowColor", shadowColor)
        bundle.putInt("shadowProg", shadowProg)
        bundle.putString("bgDrawable", bgDrawable)
        bundle.putInt("bgColor", bgColor)
        bundle.putInt("bgAlpha", bgAlpha)
        bundle.putFloat("rotation", 0.0f)
        bundle.putString("view", "mosaic")
        bundle.putString("gravity", txtGravity)
        i.putExtras(bundle)
        startActivityForResult(i, TEXT_ACTIVITY)
    }

    private fun onTouchApply() {
        removeScroll()
        setbottomLayerSelected(0)
        if (binding.layTextMain.visibility == View.VISIBLE) {
            binding.layTextMain.startAnimation(animSlideDown)
            binding.layTextMain.visibility = View.GONE
        }
        removeImageViewControll()
    }

    override fun onSnapFilter(stkrName: String, colorType: String, stkr_path: String) {
        Log.e("stkrName", "poster$stkrName")
        Log.e("colorType", "poster$colorType")
        Log.e("stkr_path", "poster$stkr_path")

        binding.XRoteSeekBar.progress = 45
        binding.YRoteSeekBar.progress = 45
        binding.ZRoteSeekBar.progress = SubsamplingScaleImageView.ORIENTATION_180
        binding.ScaleSeekBar.progress = 10
        binding.btnLayControls.visibility = View.VISIBLE
        binding.laySticker.visibility = View.GONE
        setbottomLayerSelected(R.id.add_sticker)
        if (binding.layTextMain.visibility == View.VISIBLE) {
            binding.layTextMain.startAnimation(animSlideDown)
            binding.layTextMain.visibility = View.GONE
        }
        if ((stkr_path == "")) {
            mDrawableName = stkrName
            setDrawable(colorType)
            return
        }
        color_Type = "colored"
        addSticker("", stkr_path)
    }

    override fun onSnapFilterPosition(
        stkrName: Array<String>,
        stkrPosition: Int,
        colorType: String
    ) {
        this.stkrName = stkrName
        pos = stkrPosition
        colotType = colorType
        if (/*!remove_ad_pref!!.getBoolean("isAdsDisabled", false)*/ !subscriptionViewModel.isPremiumActive.value!!) {
//            if (mRewardedVideoAd.isLoaded()) {
//                mRewardedVideoAd.show();
//                return;
//            }
            loadRewardedVideoAd()
            val dialog1 = Dialog(this /*, 16974126*/)
            dialog1.requestWindowFeature(1)
            dialog1.setContentView(R.layout.interner_connection_dialog)
            dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog1.setCancelable(true)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog1.window!!.attributes)
            lp.width = -1
            lp.height = -2
            (dialog1.findViewById<View>(R.id.heater) as TextView).typeface = ttfHeader
            (dialog1.findViewById<View>(R.id.txt_free) as TextView).typeface = ttf
            val btn_ok = dialog1.findViewById<View>(R.id.btn_ok) as Button
            btn_ok.typeface = ttf
            btn_ok.setOnClickListener { dialog1.dismiss() }
            lp.dimAmount = 0.7f
            dialog1.window!!.attributes.windowAnimations = R.style.DialogAnimation_
            dialog1.show()
            dialog1.window!!.attributes = lp
            dialog1.window!!.addFlags(2)
        }
    }

    private fun setDrawable(sticker_color: String) {
        color_Type = sticker_color
        if ((sticker_color == "white")) {
            binding.layColor.visibility = View.VISIBLE
            binding.layHue.visibility = View.GONE
        } else {
            binding.layColor.visibility = View.GONE
            //lay_hue.setVisibility(View.VISIBLE);
        }
        binding.layOpacity.visibility = View.GONE
        addSticker(mDrawableName, "")
    }

    private fun viewToBitmap(frameLayout: View?): Bitmap {
        var b: Bitmap? = null
        try {
            b = Bitmap.createBitmap(
                frameLayout!!.width,
                frameLayout.height,
                Bitmap.Config.ARGB_8888
            )
            frameLayout.draw(Canvas(b))
            frameLayout.destroyDrawingCache()
        } catch (e2: Error) {
            val error = e2
            b = Bitmap.createBitmap(
                frameLayout!!.width,
                frameLayout.height,
                Bitmap.Config.ARGB_4444
            )
            frameLayout.draw(Canvas(b))
            return b
        } catch (e: Throwable) {
            try {
                b = Bitmap.createBitmap(
                    frameLayout!!.width,
                    frameLayout.height,
                    Bitmap.Config.ARGB_4444
                )
                frameLayout.draw(Canvas(b))
                return b
            } finally {
                frameLayout!!.destroyDrawingCache()
            }
        }
        return b
    }

    private fun showDialogSave() {
        val dialog = Dialog(this /*, 16974126*/)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.save_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = -1
        lp.height = -2
        (dialog.findViewById<View>(R.id.txtapp) as TextView).typeface = ttfHeader
        (dialog.findViewById<View>(R.id.txt) as TextView).typeface = ttf
        (dialog.findViewById<View>(R.id.txt1) as TextView).typeface = ttf
        val dialogButton = dialog.findViewById<View>(R.id.btn_template) as Button
        dialogButton.typeface = ttf
        dialogButton.setOnClickListener {
            createFrame()
            dialog.dismiss()
        }
        val btn_yes = dialog.findViewById<View>(R.id.btn_image) as Button
        btn_yes.typeface = ttf
        btn_yes.setOnClickListener {
            if (((frame_Name == "") && (shap_Name == "shape_0")
                        && (binding.seekOpacity.progress >
                        ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION)
                        && (hex == "no")
                        && (profile == "no")
                        && (overlay_Name == ""))
                || ((frame_Name == "")
                        && (profile == "Color")
                        && (hex == "ffffffff")
                        && (shap_Name == "shape_0")
                        && (binding.seekOpacity.progress >
                        ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION)
                        && (overlay_Name == ""))) {
                Log.d(TAG, "on image save if part executed")
                showOptionSaveDialog()
            } else {
                saveUserLogoPicture("white", true)
                Log.d(TAG, "on image save else part executed")
            }
            dialog.dismiss()
        }
        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.show()
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(2)
    }

    private fun saveUserLogoPicture(transparent: String, sizeOptions: Boolean) {
        imageType = transparent
        if (sizeOptions) {
            val dh = DatabaseHandler.getDbHandler(this)
            val templateList = dh.getTemplateListDes("SAVEPICTURE", "DESC")
            for (i in templateList.indices) {
                dh.deleteTemplateInfo((templateList[i] as TemplateInfo).templatE_ID)
            }
            dh.close()
            saveImageSize = 0.0f
            countSize = 0
        }
        saveTemplateAsync = SaveTemplateAsync(transparent, sizeOptions)
        saveTemplateAsync!!.execute(*arrayOfNulls<String>(0))
    }

    private fun showOptionSaveDialog() {
        val dialog1 = Dialog(this /*, 16974126*/)
        dialog1.requestWindowFeature(1)
        dialog1.setContentView(R.layout.save_picture_options)
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog1.window!!.attributes)
        lp.width = -1
        lp.height = -2
        val btn_trans = dialog1.findViewById<View>(R.id.btn_trans) as Button
        btn_trans.typeface = ttf
        btn_trans.setOnClickListener {
            saveUserLogoPicture("transparent", true)
            dialog1.dismiss()
        }
        val btn_outTrans = dialog1.findViewById<View>(R.id.btn_outTrans) as Button
        btn_outTrans.typeface = ttf
        btn_outTrans.setOnClickListener {
            saveUserLogoPicture("white", true)
            dialog1.dismiss()
        }
        lp.dimAmount = 0.7f
        dialog1.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog1.show()
        dialog1.window!!.attributes = lp
        dialog1.window!!.addFlags(2)
    }

    private fun createFrame() {
        val ringProgressDialog = ProgressDialog.show(
            this, "", ImageUtils.getSpannableString(
                this, ttf, R.string.plzwait
            ), true
        )
        ringProgressDialog.setCancelable(false)
        Thread {
            val templateId = insertDataTemplate("USER", "white")
            ringProgressDialog.dismiss()
        }.start()
        ringProgressDialog.setOnDismissListener {
            val dialog1 = Dialog(this@CreatePoster  /*, 16974126*/)
            dialog1.requestWindowFeature(1)
            dialog1.setContentView(R.layout.save_success_dialog)
            dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog1.setCancelable(true)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog1.window!!.attributes)
            lp.width = -1
            lp.height = -2
            (dialog1.findViewById<View>(R.id.heater) as TextView).typeface =
                ttfHeader
            (dialog1.findViewById<View>(R.id.txt_free) as TextView).typeface = ttf
            val btn_ok = dialog1.findViewById<View>(R.id.btn_ok) as Button
            btn_ok.typeface = ttf
            btn_ok.setOnClickListener {
                if (temp_Type != "") {
                    editor!!.putBoolean("isChanged", true)
                    editor!!.commit()
                }
                dialog1.dismiss()
            }
            lp.dimAmount = 0.7f
            dialog1.window!!.attributes.windowAnimations = R.style.DialogAnimation_
            dialog1.show()
            dialog1.window!!.attributes = lp
            dialog1.window!!.addFlags(2)
        }
    }

    private fun insertDataTemplate(templateType: String, transp_Check: String): Long {
        var dh: DatabaseHandler? = null
        var templateId: Long = -1
        try {
            if ((ratio == "")) {
                temp_path = Constants.saveBitmapObject1(imgBtmap?:Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888))
            }
            val ti = TemplateInfo()
            ti.art_Type = viewModel.typeOfDesign
            ti.art_BG = viewModel.myBitmap?:Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888)
            ti.thumB_URI = ""
            ti.framE_NAME = frame_Name
            ti.ratio = ratio
            ti.profilE_TYPE = profile
            ti.seeK_VALUE = seekValue.toString()
            ti.type = templateType
            ti.temP_PATH = temp_path
            if ((profile == "Gradient")) {
                val jsonObj = JSONObject()
                jsonObj.put("Type", typeGradient)
                jsonObj.put("Orient", orient)
                jsonObj.put("Color1", colors!![0])
                jsonObj.put("Color2", colors!![1])
                jsonObj.put("Prog_radius", prog_radious)
                ti.tempcolor = jsonObj.toString()
            } else {
                ti.tempcolor = hex
            }
            ti.overlaY_NAME = overlay_Name
            ti.overlaY_OPACITY = binding.seek.progress
            if ((transp_Check == "transparent")) {
                ti.overlaY_BLUR = 0
            } else {
                ti.overlaY_BLUR = binding.seekOpacity.progress
            }
            ti.shaP_NAME = shap_Name
            dh = DatabaseHandler.getDbHandler(this)
            templateId = dh.insertTemplateRow(ti)
            saveComponent1(templateId, dh)
            dh?.close()
        } catch (e: Exception) {
            Log.i("testing", "Exception " + e.message)
            e.printStackTrace()
            dh?.close()
        } catch (th: Throwable) {
            dh?.close()
        }
        return templateId
    }

    private fun saveComponent1(templateId: Long, dh: DatabaseHandler?) {
        val childCount = binding.txtStkrRel.childCount
        for (i in 0 until childCount) {
            val child = binding.txtStkrRel.getChildAt(i)
            if (child is AutofitTextRel) {
                val textInfo = child.textInfo
                textInfo.templatE_ID = templateId.toInt()
                textInfo.order = i
                textInfo.type = "TEXT"
                dh!!.insertTextRow(textInfo)
            } else {
                saveShapeAndSticker(templateId, i, TYPE_STICKER, dh)
            }
        }
    }

    fun saveShapeAndSticker(templateId: Long, i: Int, type: Int, dh: DatabaseHandler?) {
        val ci = (binding.txtStkrRel.getChildAt(i) as ResizableStickerView).componentInfo
        ci.templatE_ID = templateId.toInt()
        ci.type = "STICKER"
        ci.order = i
        dh!!.insertComponentInfoRow(ci)
    }

    private fun showDialogSaveImage(options: String) {
        val dialog1 = Dialog(this /*, 16974126*/)
        dialog1.requestWindowFeature(1)
        dialog1.setContentView(R.layout.save_error_dialog)
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog1.window!!.attributes)
        lp.width = -1
        lp.height = -2
        val btn_card = dialog1.findViewById<Button>(R.id.btn_card)
        val btn_poster = dialog1.findViewById<Button>(R.id.btn_poster)
        val txtHeader = dialog1.findViewById<View>(R.id.heater) as TextView
        val textDes = dialog1.findViewById<View>(R.id.txt_free) as TextView
        val txt_or = dialog1.findViewById<View>(R.id.txt_or) as TextView
        val btn_1 = dialog1.findViewById<View>(R.id.btn_1) as RelativeLayout
        val btn_2 = dialog1.findViewById<View>(R.id.btn_2) as RelativeLayout
        val btn_icon = dialog1.findViewById<View>(R.id.btn_icon) as Button
        val btn_thumb = dialog1.findViewById<View>(R.id.btn_thumb) as Button
        val btn_3 = dialog1.findViewById<View>(R.id.btn_3) as Button
        val btn_bestSize = dialog1.findViewById<View>(R.id.btn_bestSize) as Button
        val btn_ok = dialog1.findViewById<View>(R.id.btn_ok) as Button
        val edit_size1 = dialog1.findViewById<View>(R.id.txt_size1) as EditText
        val txt_size2 = dialog1.findViewById<View>(R.id.txt_size2) as TextView
        val btn_size = dialog1.findViewById<View>(R.id.btn_size) as Button
        (dialog1.findViewById<View>(R.id.btn_lay) as Button).setOnClickListener(
            {
                showPremiumDialog()
                dialog1.dismiss()
            })
        btn_card.typeface = ttf
        btn_poster.typeface = ttf
        (dialog1.findViewById<View>(R.id.txt1) as TextView).typeface = ttf
        (dialog1.findViewById<View>(R.id.txt2) as TextView).typeface = ttf
        btn_icon.typeface = ttf
        btn_thumb.typeface = ttf
        btn_3.typeface = ttf
        btn_bestSize.typeface = ttf
        btn_ok.typeface = ttf
        txtHeader.typeface = ttfHeader
        textDes.typeface = ttf
        txt_or.typeface = ttf
        edit_size1.typeface = ttf
        txt_size2.typeface = ttf
        btn_size.typeface = ttf
        (dialog1.findViewById<View>(R.id.txtCustom) as TextView).typeface = ttf
        if ((options == "Option")) {
            txtHeader.text = resources.getString(R.string.save_Error_title1)
        } else if ((options == "BestSize")) {
            txtHeader.text = resources.getString(R.string.save_Error_title2)
            textDes.text = resources.getString(R.string.des_save_image)
            (dialog1.findViewById<View>(R.id.lay_Size) as LinearLayout).visibility = View.GONE
            textDes.visibility = View.VISIBLE
            txt_or.visibility = View.VISIBLE
            btn_bestSize.visibility = View.VISIBLE
        } else {
            txtHeader.text = resources.getString(R.string.save_Error_title2)
            textDes.text = resources.getString(R.string.check_memory)
            (dialog1.findViewById<View>(R.id.lay_Size) as LinearLayout).visibility = View.GONE
            btn_bestSize.visibility = View.GONE
            btn_ok.visibility = View.VISIBLE
            textDes.visibility = View.VISIBLE
        }
        edit_size1.setTextSize(2, 12.0f)
        val textView = txt_size2
        edit_size1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 0) {
                    edit_size1.setTextSize(2, 12.0f)
                } else {
                    edit_size1.setTextSize(2, 18.0f)
                }
                textView.text = s
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        btn_1.setOnClickListener {
            saveImageSize = 2048.0f
            saveUserLogoPicture(imageType!!, false)
            dialog1.dismiss()
        }
        btn_2.setOnClickListener {
            saveImageSize = 1024.0f
            imageType?.let { it1 ->
                saveUserLogoPicture(
                    it1,
                    false
                )
            }
            dialog1.dismiss()
        }
        btn_icon.visibility = View.GONE
        btn_icon.setOnClickListener {
            dialog1.dismiss()
            saveImageSize = 128.0f
            imageType?.let { it1 ->
                saveUserLogoPicture(
                    it1,
                    false
                )
            }
        }
        btn_thumb.visibility = View.GONE
        btn_thumb.setOnClickListener {
            dialog1.dismiss()
            saveImageSize = 256.0f
            imageType?.let { it1 ->
                saveUserLogoPicture(
                    it1,
                    false
                )
            }
        }
        btn_3.setOnClickListener {
            dialog1.dismiss()
            saveImageSize = 512.0f
                saveUserLogoPicture(
                    imageType?:"",
                    false
                )
        }
        btn_bestSize.setOnClickListener {
            dialog1.dismiss()
            countSize++
            saveImageSize = (saveImageSize * 80.0f) / 100.0f
            imageType?.let { it1 ->
                saveUserLogoPicture(
                    it1,
                    false
                )
            }
        }
        btn_ok.setOnClickListener {
            saveImageSize = 0.0f
            countSize = 0
            dialog1.dismiss()
        }
        btn_size.setOnClickListener {
            val text = edit_size1.text.toString()
            try {
                if (text != "") {
                    val num = text.toInt()
                    if (num <= 2048 && num >= 10) {
                        edit_size1.error = null
                        dialog1.dismiss()
                        saveImageSize = num.toFloat()
                        imageType?.let { it1 ->
                            saveUserLogoPicture(
                                it1,
                                false
                            )
                        }
                    } else if (num > 2048) {
                        edit_size1.error =
                            resources.getString(R.string.txtUnvalit)
                    } else {
                        edit_size1.error =
                            resources.getString(R.string.txtUnvalit1)
                    }
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                Toast.makeText(
                    this@CreatePoster,
                    resources.getString(R.string.txtUnvalit),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e2: Resources.NotFoundException) {
                e2.printStackTrace()
                Toast.makeText(
                    this@CreatePoster,
                    resources.getString(R.string.txtUnvalit),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        lp.dimAmount = 0.7f
        dialog1.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog1.show()
        dialog1.window!!.attributes = lp
        dialog1.window!!.addFlags(2)
    }

    private fun saveImage(activity: Activity, bitmap: Bitmap?, inPNG: Boolean): Boolean {
        Log.d(TAG," bitmap size in saveImage method ${bitmap?.height}")
        try {
            val pictureFileDir = StorageConfiguration.getSavedImagesPath()

            var photoFile = "IMG_" + System.currentTimeMillis()
            if (inPNG) {
                photoFile = "$photoFile.png"
            } else {
                photoFile = "$photoFile.jpg"
            }
            imagePath = pictureFileDir.path + File.separator + photoFile
            val pictureFile = File(imagePath)
            try {
                val saved: Boolean
                if (!pictureFile.exists()) {
                    pictureFile.createNewFile()
                }
                val ostream = FileOutputStream(pictureFile)
                if (inPNG) {
                    saved = bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, ostream)
                } else {
                    val newBitmap = Bitmap.createBitmap(
                        bitmap!!.width, bitmap.height, (bitmap.config)!!
                    )
                    val canvas = Canvas(newBitmap)
                    canvas.drawColor(-1)
                    canvas.drawBitmap((bitmap), 0.0f, 0.0f, null)
                    saved = newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
                    newBitmap.recycle()
                }
                ostream.flush()
                ostream.close()
//                activity.sendBroadcast(
//                    Intent(
//                        "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
//                        Uri.fromFile(pictureFile)
//                    )
//                )
                MediaScannerConnection.scanFile(
                    activity,
                    arrayOf(pictureFile.absolutePath),
                    null,
                    null
                )
                return saved
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        } catch (e2: Exception) {
            e2.printStackTrace()
            Log.i("testing", "Exception" + e2.message)
            return false
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        var i: Int
        val childCount2: Int
        var view2: View?
        val id = seekBar.id
        if (id == binding.seekTailys.id) {
            seekValue = progress
            addTilesBG(frame_Name)
            return
        } else if (id == binding.seekOpacity.id) {
            binding.backgroundImg.alpha = (progress.toFloat()) / 255.0f
            return
        } else if (id == R.id.seek) {
            alpha = progress
            binding.transImg.imageAlpha = alpha
            return
        } else if (id == R.id.alpha_seekBar) {
            val childCount = binding.txtStkrRel.childCount
            i = 0
            while (i < childCount) {
                val view = binding.txtStkrRel.getChildAt(i)
                if ((view is ResizableStickerView) && view.borderVisbilty) {
                    view.alphaProg = progress
                }
                i++
            }
            return
        } else if (id == R.id.hue_seekBar) {
            val childCount1 = binding.txtStkrRel.childCount
            i = 0
            while (i < childCount1) {
                val view1 = binding.txtStkrRel.getChildAt(i)
                if ((view1 is ResizableStickerView) && view1.borderVisbilty) {
                    view1.hueProg = progress
                }
                i++
            }
            return
        } else if (id == R.id.XRote_seekBar) {
            if (progress != 0) {
                childCount2 = binding.txtStkrRel.childCount
                i = 0
                while (i < childCount2) {
                    view2 = binding.txtStkrRel.getChildAt(i)
                    if ((view2 is ResizableStickerView) && view2.borderVisbilty) {
                        if (progress < 42 || progress > 48) {
                            view2.setStickerRotateProg(
                                45 - progress,
                                45 - binding.YRoteSeekBar.progress,
                                180 - binding.ZRoteSeekBar.progress,
                                binding.XRoteSeekBar.progress,
                                binding.YRoteSeekBar.progress,
                                binding.ZRoteSeekBar.progress
                            )
                        } else {
                            binding.XRoteSeekBar.progress = 45
                            view2.setStickerRotateProg(
                                0,
                                45 - binding.YRoteSeekBar.progress,
                                180 - binding.ZRoteSeekBar.progress,
                                binding.XRoteSeekBar.progress,
                                binding.YRoteSeekBar.progress,
                                binding.ZRoteSeekBar.progress
                            )
                        }
                    }
                    i++
                }
                return
            }
            return
        } else if (id == R.id.YRote_seekBar) {
            if (progress != 0) {
                childCount2 = binding.txtStkrRel.childCount
                i = 0
                while (i < childCount2) {
                    view2 = binding.txtStkrRel.getChildAt(i)
                    if ((view2 is ResizableStickerView) && view2.borderVisbilty) {
                        if (progress < 42 || progress > 48) {
                            view2.setStickerRotateProg(
                                45 - binding.XRoteSeekBar.progress,
                                45 - progress,
                                180 - binding.ZRoteSeekBar.progress,
                                binding.XRoteSeekBar.progress,
                                binding.YRoteSeekBar.progress,
                                binding.ZRoteSeekBar.progress
                            )
                        } else {
                            binding.YRoteSeekBar.progress = 45
                            view2.setStickerRotateProg(
                                45 - binding.XRoteSeekBar.progress,
                                0,
                                180 - binding.ZRoteSeekBar.progress,
                                binding.XRoteSeekBar.progress,
                                binding.YRoteSeekBar.progress,
                                binding.ZRoteSeekBar.progress
                            )
                        }
                    }
                    i++
                }
                return
            }
            return
        } else if (id == R.id.ZRote_seekBar) {
            if (progress != 0) {
                childCount2 = binding.txtStkrRel.childCount
                i = 0
                while (i < childCount2) {
                    view2 = binding.txtStkrRel!!.getChildAt(i)
                    if ((view2 is ResizableStickerView) && view2.borderVisbilty) {
                        if (progress < 175 || progress > 185) {
                            view2.setStickerRotateProg(
                                45 - binding.XRoteSeekBar.progress,
                                45 - binding.YRoteSeekBar.progress,
                                180 - progress,
                                binding.XRoteSeekBar.progress,
                                binding.YRoteSeekBar.progress,
                                binding.ZRoteSeekBar.progress
                            )
                        } else {
                            binding.ZRoteSeekBar.progress =
                                SubsamplingScaleImageView.ORIENTATION_180
                            view2.setStickerRotateProg(
                                45 - binding.XRoteSeekBar.progress,
                                45 - binding.YRoteSeekBar.progress,
                                0,
                                binding.XRoteSeekBar.progress,
                                binding.YRoteSeekBar.progress,
                                binding.ZRoteSeekBar.progress
                            )
                        }
                    }
                    i++
                }
                return
            }
            return
        } else if (id == R.id.Scale_seekBar) {
            if (progress != 0) {
                childCount2 = binding.txtStkrRel.childCount
                i = 0
                while (i < childCount2) {
                    view2 = binding.txtStkrRel.getChildAt(i)
                    if ((view2 is ResizableStickerView) && view2.borderVisbilty) {
                        view2.setScaleViewProg(progress)
                    }
                    i++
                }
                return
            }
            return
        } else if (id == R.id.seekBar2) {
            processs = progress
            val childCount3 = binding.txtStkrRel.childCount
            i = 0
            while (i < childCount3) {
                val view3 = binding.txtStkrRel.getChildAt(i)
                if ((view3 is AutofitTextRel) && view3.borderVisibility) {
                    view3.textAlpha = progress
                }
                i++
            }
            return
        } else if (id == R.id.seekBar_shadow) {
            val childCount4 = binding.txtStkrRel.childCount
            i = 0
            while (i < childCount4) {
                val view4 = binding.txtStkrRel.getChildAt(i)
                if ((view4 is AutofitTextRel) && view4.borderVisibility) {
                    view4.textShadowProg = progress
                    shadowProg = progress
                }
                i++
            }
            return
        } else if (id == R.id.seekBar3) {
            val childCount5 = binding.txtStkrRel.childCount
            i = 0
            while (i < childCount5) {
                val view5 = binding.txtStkrRel.getChildAt(i)
                if ((view5 is AutofitTextRel) && view5.borderVisibility) {
                    if (view5.bgColor == 0 && (view5.bgDrawable == "0")) {
                        view5.bgColor = Color.parseColor("#000000")
                        view5.textInfo.bG_COLOR = Color.parseColor("#000000")
                        bgColor = Color.parseColor("#000000")
                    }
                    view5.bgAlpha = progress
                    bgAlpha = progress
                }
                i++
            }
            return
        } else if (id == R.id.XTRote_seekBar) {
            if (progress != 0) {
                childCount2 = binding.txtStkrRel.childCount
                i = 0
                while (i < childCount2) {
                    view2 = binding.txtStkrRel.getChildAt(i)
                    if ((view2 is AutofitTextRel) && view2.borderVisibility) {
                        if (progress < 42 || progress > 48) {
                            view2.setTextRotateProg(
                                45 - progress,
                                45 - binding.YTRoteSeekBar.progress,
                                180 - binding.ZTRoteSeekBar.progress,
                                binding.XTRoteSeekBar.progress,
                                binding.YTRoteSeekBar.progress,
                                binding.ZTRoteSeekBar.progress,
                                250 - binding.CurveTRoteSeekBar.progress
                            )
                        } else {
                            binding.XTRoteSeekBar.progress = 45
                            view2.setTextRotateProg(
                                0,
                                45 - binding.YTRoteSeekBar.progress,
                                180 - binding.ZTRoteSeekBar.progress,
                                binding.XTRoteSeekBar.progress,
                                binding.YTRoteSeekBar.progress,
                                binding.ZTRoteSeekBar.progress,
                                250 - binding.CurveTRoteSeekBar.progress
                            )
                        }
                    }
                    i++
                }
                return
            }
            return
        } else if (id == R.id.YTRote_seekBar) {
            if (progress != 0) {
                childCount2 = binding.txtStkrRel.childCount
                i = 0
                while (i < childCount2) {
                    view2 = binding.txtStkrRel.getChildAt(i)
                    if ((view2 is AutofitTextRel) && view2.borderVisibility) {
                        if (progress < 42 || progress > 48) {
                            view2.setTextRotateProg(
                                45 - binding.XTRoteSeekBar.progress,
                                45 - progress,
                                180 - binding.ZTRoteSeekBar.progress,
                                binding.XTRoteSeekBar.progress,
                                binding.YTRoteSeekBar.progress,
                                binding.ZTRoteSeekBar.progress,
                                250 - binding.CurveTRoteSeekBar.progress
                            )
                        } else {
                            binding.YTRoteSeekBar.progress = 45
                            view2.setTextRotateProg(
                                45 - binding.XTRoteSeekBar.progress,
                                0,
                                180 - binding.ZTRoteSeekBar.progress,
                                binding.XTRoteSeekBar.progress,
                                binding.YTRoteSeekBar.progress,
                                binding.ZTRoteSeekBar.progress,
                                250 - binding.CurveTRoteSeekBar.progress
                            )
                        }
                    }
                    i++
                }
                return
            }
            return
        } else if (id == R.id.ZTRote_seekBar) {
            if (progress != 0) {
                childCount2 = binding.txtStkrRel.childCount
                i = 0
                while (i < childCount2) {
                    view2 = binding.txtStkrRel.getChildAt(i)
                    if ((view2 is AutofitTextRel) && view2.borderVisibility) {
                        if (progress < 175 || progress > 185) {
                            view2.setTextRotateProg(
                                45 - binding.XTRoteSeekBar.progress,
                                45 - binding.YTRoteSeekBar.progress,
                                180 - progress,
                                binding.XTRoteSeekBar.progress,
                                binding.YTRoteSeekBar.progress,
                                binding.ZTRoteSeekBar.progress,
                                250 - binding.CurveTRoteSeekBar.progress
                            )
                        } else {
                            binding.ZTRoteSeekBar.progress =
                                SubsamplingScaleImageView.ORIENTATION_180
                            view2.setTextRotateProg(
                                45 - binding.XTRoteSeekBar.progress,
                                45 - binding.YTRoteSeekBar.progress,
                                0,
                                binding.XTRoteSeekBar.progress,
                                binding.YTRoteSeekBar.progress,
                                binding.ZTRoteSeekBar.progress,
                                250 - binding.CurveTRoteSeekBar.progress
                            )
                        }
                    }
                    i++
                }
                return
            }
            return
        } else if (id == R.id.CurveTRote_seekBar) {
            if (progress != 0) {
                childCount2 = binding.txtStkrRel.childCount
                i = 0
                while (i < childCount2) {
                    view2 = binding.txtStkrRel.getChildAt(i)
                    if ((view2 is AutofitTextRel) && view2.borderVisibility) {
                        if (progress < 245 || progress > 255) {
                            view2.setTextCurveRotateProg(250 - progress)
                        } else {
                            binding.CurveTRoteSeekBar.progress =
                                ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION
                            view2.setTextCurveRotateProg(0)
                        }
                    }
                    i++
                }
                return
            }
            return
        }
        return
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

    private fun addTilesBG(resId: String?) {
        if (resId != "" && imgBtmap != null) {
            setImageBitmapAndResizeLayout1(
                Constants.getTiledBitmap(
                    this,
                    resId,
                    imgBtmap?:Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888),
                    binding.seekTailys
                )
            )
        }
    }

    private fun setImageBitmapAndResizeLayout1(bit: Bitmap) {
        binding.mainRel.layoutParams.width = bit.width
        binding.mainRel.layoutParams.height = bit.height
        binding.mainRel.postInvalidate()
        binding.mainRel.requestLayout()
        binding.backgroundImg.setImageBitmap(bit)
        binding.transImg.setImageBitmap(bit)
        imgBtmap = bit
    }

    override fun onTouchDown(v: View) {
        touchDown(v, "hideboder")
    }

    override fun onTouchUp(v: View) {
        touchUp(v)
    }

    override fun onTouchMove(v: View) {
        touchMove(v)
    }

    private fun touchDown(v: View, visibl: String) {
        focusedView = v
        if ((visibl == "hideboder")) {
            removeImageViewControll()
        }
        if (v is ResizableStickerView) {
            binding.layTextMain.visibility = View.GONE
            binding.layStkrMain.visibility = View.GONE
            stkrColorSet = v.color
            binding.horizontalPickerColor.setSelectedColor(stkrColorSet)
            binding.alphaSeekBar.progress = v.alphaProg
            binding.hueSeekBar.progress = v.hueProg
            binding.XRoteSeekBar.progress = v.xRotateProg
            binding.YRoteSeekBar.progress = v.yRotateProg
            binding.ZRoteSeekBar.progress = v.zRotateProg
            binding.ScaleSeekBar.progress = v.geScaleProg()
        }
        if (v is AutofitTextRel) {
            binding.layStkrMain.visibility = View.GONE
            binding.layTextMain.visibility = View.GONE
            textColorSet = v.textColor
            binding.horizontalPicker.setSelectedColor(textColorSet)
            fontName = v.fontName
            tColor = v.textColor
            shadowColor = v.textShadowColor
            shadowProg = v.textShadowProg
            tAlpha = v.textAlpha
            bgDrawable = v.bgDrawable
            bgAlpha = v.bgAlpha
            bgColor = v.bgColor
            txtGravity = v.textGravity
            val font_Arr = resources.getStringArray(R.array.txtfont_array)
            adapter!!.setSelected(-1)
            for (i in font_Arr.indices) {
                if ((font_Arr[i] == fontName)) {
                    adapter!!.setSelected(i)
                }
            }
            if ((bgDrawable == "0") || bgAlpha == 0) {
                adaptor_txtBg!!.setSelected(500)
            } else {
                adaptor_txtBg!!.setSelected(bgDrawable.replace("btxt", "").toInt())
            }
            binding.opctyTxtSeekbar.progress = tAlpha
            binding.seekBarShadow.progress = shadowProg
            binding.seekBar3.progress = bgAlpha
            binding.XTRoteSeekBar.progress = v.xRotateProg
            binding.YTRoteSeekBar.progress = v.yRotateProg
            binding.ZTRoteSeekBar.progress = v.zRotateProg
            if (v.curveRotateProg == ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) {
                binding.CurveTRoteSeekBar.progress =
                    ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION
            } else {
                binding.CurveTRoteSeekBar.progress = 250 - v.curveRotateProg
            }
        }
        if (binding.guidelines.visibility == View.GONE) {
            binding.guidelines.visibility = View.VISIBLE
        }
        if (binding.layOpacity.visibility == View.VISIBLE) {
            binding.layOpacity.startAnimation(animSlideDown)
            binding.layOpacity.visibility = View.GONE
        }
    }

    private fun touchMove(v: View) {
        if (v is ResizableStickerView) {
            binding.layTextMain.visibility = View.GONE
            binding.layStkrMain.visibility = View.GONE
            removeScroll()
        }
        if (v is AutofitTextRel) {
            binding.layTextMain.visibility = View.GONE
            binding.layStkrMain.visibility = View.GONE
            removeScroll()
        }
    }

    private fun touchUp(v: View) {
        if (focusedCopy !== focusedView) {
            binding.seekbarContainer.visibility = View.VISIBLE
            binding.layTextEdit.visibility = View.VISIBLE
        }
        if (v is AutofitTextRel) {
            if (binding.layTextMain.visibility == View.GONE) {
                setbottomLayerSelected(R.id.add_text)
                binding.layTextMain.visibility = View.VISIBLE
                binding.layTextMain.startAnimation(animSlideUp)
                binding.layTextMain.post { stickerScrollView(v) }
            }
            if (processs != 0) {
                binding.opctyTxtSeekbar.progress = processs
            }
        }
        if (v is ResizableStickerView) {
            if ((("" + v.colorType) == "white")) {
                binding.layColor.visibility = View.VISIBLE
                binding.layHue.visibility = View.GONE
            } else {
                binding.layColor.visibility = View.GONE
                // lay_hue.setVisibility(View.VISIBLE);
            }
            if (binding.layStkrMain.visibility == View.GONE) {
                setbottomLayerSelected(R.id.add_sticker)
                binding.layStkrMain.visibility = View.VISIBLE
                binding.layStkrMain.startAnimation(animSlideUp)
                binding.layStkrMain.post { stickerScrollView(v) }
            }
        }
        if (binding.guidelines.visibility == View.VISIBLE) {
            binding.guidelines.visibility = View.GONE
        }
    }

    override fun onDelete() {
        binding.seekbarContainer.visibility = View.VISIBLE
        binding.layTextEdit.visibility = View.VISIBLE
        removeScroll()
        if (binding.layStkrMain.visibility == View.VISIBLE) {
            binding.layStkrMain.startAnimation(animSlideDown)
            binding.layStkrMain.visibility = View.GONE
        }
        if (binding.layTextMain.visibility == View.VISIBLE) {
            binding.layTextMain.startAnimation(animSlideDown)
            binding.layTextMain.visibility = View.GONE
        }
        setbottomLayerSelected(0)
        binding.guidelines.visibility = View.GONE
    }

    override fun onRotateDown(v: View) {
        touchDown(v, "viewboder")
    }

    override fun onRotateMove(v: View) {
        touchMove(v)
    }

    override fun onRotateUp(v: View) {
        touchUp(v)
    }

    override fun onScaleDown(v: View) {
        touchDown(v, "viewboder")
    }

    override fun onScaleMove(v: View) {
        touchMove(v)
    }

    override fun onScaleUp(v: View) {
        touchUp(v)
    }

    override fun onEdit(v: View, uri: Uri) {
    }

    override fun onDoubleTap() {
        Log.e(TAG, "onDoubleTap: ")
        doubleTap()
    }

    fun stickerScrollView(v: View?) {
        if (v != null) {
            val rotation: Float
            val los = IntArray(2)
            v.getLocationOnScreen(los)
            val vW = v.width.toFloat()
            val vH = v.height.toFloat()
            if (v is ResizableStickerView) {
                rotation = v.rotation
            } else {
                rotation = (v as AutofitTextRel).rotation
            }
            val los1 = IntArray(2)
            binding.layScroll.getLocationOnScreen(los1)
            parentY = los1[1].toFloat()
            val x = v.x
            val y = v.y + parentY
            distance = parentY - (ImageUtils.dpToPx(this, 50).toFloat())
            val matrix = Matrix()
            val rectF = RectF(x, y, x + vW, y + vH)
            matrix.postRotate(rotation, (vW / 2.0f) + x, (vH / 2.0f) + y)
            matrix.mapRect(rectF)
            val bYonScreen = (los[1].toFloat()) + vH
            var bY = max(rectF.top.toDouble(), rectF.bottom.toDouble()).toFloat()
            val currentScrolledDistance = binding.layScroll.scrollY.toFloat()
            if (currentScrolledDistance > 0.0f) {
                bY -= currentScrolledDistance
            }
            val los_edittext_lay = IntArray(2)
            if (v is ResizableStickerView) {
                binding.seekbarContainer.getLocationOnScreen(los_edittext_lay)
            } else {
                binding.layTextEdit.getLocationOnScreen(los_edittext_lay)
            }
            val pY = los_edittext_lay[1].toFloat()
            if (parentY + (binding.layScroll.height.toFloat()) < bY) {
                bY = parentY + (binding.layScroll.height.toFloat())
            }
            if (bY > pY) {
                distanceScroll = (bY - pY).toInt()
                dsfc = distanceScroll
                if ((distanceScroll.toFloat()) < distance) {
                    binding.layScroll.y = (parentY - (ImageUtils.dpToPx(
                        this, 50
                    ).toFloat())) - (distanceScroll.toFloat())
                } else {
                    val currentDistanceToScroll = binding.layScroll.scrollY
                    binding.layScroll.layoutParams = RelativeLayout.LayoutParams(-1, -2)
                    binding.layScroll.postInvalidate()
                    binding.layScroll.requestLayout()
                    val scrollBy = ((bY - distance) - pY).toInt()
                    val nH = binding.layScroll.height - scrollBy
                    distanceScroll = currentDistanceToScroll + scrollBy
                    binding.layScroll.layoutParams.height = nH
                    binding.layScroll.postInvalidate()
                    binding.layScroll.requestLayout()
                }
                binding.layScroll.post { binding.btnBck1.performClick() }
            }
        }
    }

    private fun removeScroll() {

        // Get the bitmap's height from the ImageView
        val bitmapHeight = typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).second
        // Calculate the y offset to center the image vertically
        val yOffset = (screenHeight - bitmapHeight) / 2
        // Adjust the ScrollView's position
        binding.layScroll.y = yOffset.toFloat()
        // Update layout parameters if needed
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        binding.layScroll.layoutParams = params
        // Force a layout refresh
        binding.layScroll.postInvalidate()
        binding.layScroll.requestLayout()
        Log.d(TAG, "yOffset calculated: $yOffset")

//        val los1 = IntArray(2)
//        binding.layScroll.getLocationOnScreen(los1)
//        if ((los1[1].toFloat()) != (ImageUtils.dpToPx(this, 50).toFloat())) {
//            binding.layScroll.y =
//                yAtLayoutCenter - (ImageUtils.dpToPx(
//                    this,
//                    50
//                ).toFloat())
//        }
//        val params = RelativeLayout.LayoutParams(-1, -2)
//        params.addRule(18)
//        binding.layScroll.layoutParams = params
//        binding.layScroll.postInvalidate()
//        binding.layScroll.requestLayout()
    }

    private fun doubleTap() {
        removeImageViewControll()
        editMode = true
        val t = (binding.txtStkrRel.getChildAt(
            binding.txtStkrRel.childCount - 1
        ) as AutofitTextRel).textInfo
        val i = Intent(this, TextActivity::class.java)
        val bundle = Bundle()
        bundle.putFloat("X", t.poS_X)
        bundle.putFloat("Y", t.poS_Y)
        bundle.putInt("wi", t.width)
        bundle.putInt("he", t.height)
        bundle.putString("text", t.text)
        bundle.putString("fontName", t.fonT_NAME)
        bundle.putInt("tColor", t.texT_COLOR)
        bundle.putInt("tAlpha", t.texT_ALPHA)
        bundle.putInt("shadowColor", t.shadoW_COLOR)
        bundle.putInt("shadowProg", t.shadoW_PROG)
        bundle.putString("bgDrawable", t.bG_DRAWABLE)
        bundle.putInt("bgColor", t.bG_COLOR)
        bundle.putInt("bgAlpha", t.bG_ALPHA)
        bundle.putFloat("rotation", t.rotation)
        bundle.putString("gravity", t.texT_GRAVITY)
        i.putExtras(bundle)
        startActivityForResult(i, TEXT_ACTIVITY)
    }

    private fun addSticker(resId: String, str_path: String) {
        if (binding.layStkrMain.visibility == View.GONE) {
            binding.layStkrMain.visibility = View.VISIBLE
            binding.layStkrMain.startAnimation(animSlideUp)
        }
        binding.alphaSeekBar.progress = 100
        binding.hueSeekBar.progress = 1
        val ci = ComponentInfo()
        ci.poS_X = ((binding.mainRel.width / 2) - ImageUtils.dpToPx(this, 100)).toFloat()
        ci.poS_Y = ((binding.mainRel.height / 2) - ImageUtils.dpToPx(this, 100)).toFloat()
        ci.width = ImageUtils.dpToPx(this, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION)
        ci.height =
            ImageUtils.dpToPx(this, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION)
        ci.rotation = 0.0f
        ci.reS_ID = resId
        ci.bitmap = null
        ci.colortype = color_Type
        ci.type = "STICKER"
        ci.stC_OPACITY = 100
        ci.stC_COLOR = 0
        ci.stkR_PATH = str_path
        ci.stC_HUE = binding.hueSeekBar.progress
        ci.fielD_TWO = "0,0"
        ci.xRotateProg = 45
        ci.yRotateProg = 45
        ci.zRotateProg = SubsamplingScaleImageView.ORIENTATION_180
        ci.scaleProg = 10
        val riv = ResizableStickerView(this)
        riv.setOnTouchCallbackListener(this)
        riv.optimizeScreen(screenWidth, screenHeight)
        riv.componentInfo = ci
        riv.id = View.generateViewId()
        riv.setMainLayoutWH(
            binding.mainRel.width.toFloat(),
            binding.mainRel.height.toFloat()
        )
        binding.txtStkrRel.addView(riv)
        riv.setBorderVisibility(true)
    }

    override fun onCenterX(v: View) {
        binding.guidelines.setCenterValues(true, false)
    }

    override fun onCenterY(v: View) {
        binding.guidelines.setCenterValues(false, true)
    }

    override fun onCenterXY(v: View) {
        binding.guidelines.setCenterValues(true, true)
    }

    override fun onOtherXY(v: View) {
        binding.guidelines.setCenterValues(false, false)
    }

    override fun getResBytes(ctx: Context, resName: String): ByteArray {
        return JniUtils.decryptResourceJNI(ctx, resName)
    }

    fun removeImageViewControll() {
        setbottomLayerSelected(0)
        if (binding.txtStkrRel != null) {
            val childCount = binding.txtStkrRel.childCount
            for (i in 0 until childCount) {
                val view = binding.txtStkrRel.getChildAt(i)
                if (view is AutofitTextRel) {
                    view.borderVisibility = false
                }
                if (view is ResizableStickerView) {
                    view.setBorderVisibility(false)
                }
            }
        }
    }

    val removeBoderPosition: Int
        get() {
            val childCount = binding.txtStkrRel.childCount
            var k = 0
            for (i in 0 until childCount) {
                val view = binding.txtStkrRel.getChildAt(i)
                if ((view is AutofitTextRel) && view.borderVisibility) {
                    k = i
                }
                if ((view is ResizableStickerView) && view.borderVisbilty) {
                    k = i
                }
            }
            return k
        }

    private fun getBitmapDataObject(path: String): BitmapDataObject? {
        try {
            return ObjectInputStream(FileInputStream(File(path))).readObject() as BitmapDataObject
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e2: IOException) {
            e2.printStackTrace()
        } catch (e3: ClassNotFoundException) {
            e3.printStackTrace()
        }
        return null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val e: Throwable
        super.onActivityResult(requestCode, resultCode, data)

        when (viewModel.typeOfDesign) {
//            "poster" -> binding.by.setImageResource(R.drawable.businesscards_watermark_poster)
//            "card" -> binding.by.setImageResource(R.drawable.businesscards_watermark_card)
//            else -> binding.by.setImageResource(R.drawable.businesscards_watermark_logo)
        }

        if (!((binding.imageviewPager.childCount == 0) || (_adapter!!.currentFragment(
                binding.imageviewPager.currentItem
            ) == null))
        ) {
            _adapter!!.currentFragment(binding.imageviewPager.currentItem)
                .onActivityResult(requestCode, resultCode, data)
        }

        if (resultCode == -1) {
            binding.layStkrMain.visibility = View.GONE
            if ((data != null) || (requestCode == SELECT_PICTURE_FROM_CAMERA) || (requestCode == TEXT_ACTIVITY)) {
                var bundle: Bundle?
                var selectedImageUri: Uri?
                var _main: Intent
                if (requestCode == TEXT_ACTIVITY) {
                    bundle = data!!.extras
                    val textInfo = TextInfo()
                    textInfo.poS_X = bundle!!.getFloat("X", 0.0f)
                    textInfo.poS_Y = bundle.getFloat("Y", 0.0f)
                    textInfo.width = bundle.getInt(
                        "wi", ImageUtils.dpToPx(
                            this, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION
                        )
                    )
                    textInfo.height = bundle.getInt(
                        "he", ImageUtils.dpToPx(
                            this, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION
                        )
                    )
                    textInfo.text = bundle.getString("text", "")
                    textInfo.fonT_NAME = bundle.getString("fontName", "")
                    textInfo.texT_COLOR = bundle.getInt("tColor", Color.parseColor("#000000"))
                    textInfo.texT_ALPHA = bundle.getInt("tAlpha", 100)
                    textInfo.shadoW_COLOR =
                        bundle.getInt("shadowColor", Color.parseColor("#000000"))
                    textInfo.shadoW_PROG = bundle.getInt("shadowProg", 5)
                    textInfo.bG_COLOR = bundle.getInt("bgColor", 0)
                    textInfo.bG_DRAWABLE = bundle.getString("bgDrawable", "0")
                    textInfo.bG_ALPHA = bundle.getInt("bgAlpha", 255)
                    textInfo.rotation = bundle.getFloat("rotation", 0.0f)
                    textInfo.texT_GRAVITY = bundle.getString("gravity", "")
                    fontName = bundle.getString("fontName", "")
                    tColor = bundle.getInt("tColor", Color.parseColor("#000000"))
                    shadowColor = bundle.getInt("shadowColor", Color.parseColor("#000000"))
                    shadowProg = bundle.getInt("shadowProg", 0)
                    tAlpha = bundle.getInt("tAlpha", 100)
                    bgDrawable = bundle.getString("bgDrawable", "0")
                    bgAlpha = bundle.getInt("bgAlpha", 255)
                    bgColor = bundle.getInt("bgColor", Color.parseColor("#000000"))
                    txtGravity = bundle.getString("gravity", "")
                    val childCount2: Int
                    val view2: View
                    if (editMode) {
                        touchChange = false
                        textInfo.xRotateProg = (binding.txtStkrRel.getChildAt(
                            binding.txtStkrRel.childCount - 1
                        ) as AutofitTextRel).xRotateProg
                        textInfo.yRotateProg = (binding.txtStkrRel.getChildAt(
                            binding.txtStkrRel.childCount - 1
                        ) as AutofitTextRel).yRotateProg
                        textInfo.zRotateProg = (binding.txtStkrRel.getChildAt(
                            binding.txtStkrRel.childCount - 1
                        ) as AutofitTextRel).zRotateProg
                        textInfo.curveRotateProg = (binding.txtStkrRel.getChildAt(
                            binding.txtStkrRel.childCount - 1
                        ) as AutofitTextRel).curveRotateProg
                        binding.XTRoteSeekBar.progress = (binding.txtStkrRel.getChildAt(
                            binding.txtStkrRel.childCount - 1
                        ) as AutofitTextRel).xRotateProg
                        binding.YTRoteSeekBar.progress = (binding.txtStkrRel.getChildAt(
                            binding.txtStkrRel.childCount - 1
                        ) as AutofitTextRel).yRotateProg
                        binding.ZTRoteSeekBar.progress = (binding.txtStkrRel.getChildAt(
                            binding.txtStkrRel.childCount - 1
                        ) as AutofitTextRel).zRotateProg
                        try {
                            if ((focusedView as AutofitTextRel?)!!.curveRotateProg == 250) {
                                binding.CurveTRoteSeekBar.progress =
                                    ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION
                            } else {
                                binding.CurveTRoteSeekBar.progress =
                                    250 - (focusedView as AutofitTextRel?)!!.curveRotateProg
                            }
                        } catch (e2: Exception) {
                            e2.printStackTrace()
                        }
                        (binding.txtStkrRel.getChildAt(
                            binding.txtStkrRel.childCount - 1
                        ) as AutofitTextRel).setTextInfo(textInfo, false)
                        childCount2 = binding.txtStkrRel.childCount
                        if (childCount2 != 0) {
                            view2 = binding.txtStkrRel.getChildAt(childCount2 - 1)
                            if (view2 is AutofitTextRel) {
                                view2.borderVisibility = false
                                if (!view2.borderVisibility) {
                                    view2.borderVisibility = true
                                }
                            }
                        }
                        editMode = false
                    }
                    else {
                        touchChange = true
                        textInfo.xRotateProg = 45
                        textInfo.yRotateProg = 45
                        textInfo.zRotateProg = SubsamplingScaleImageView.ORIENTATION_180
                        textInfo.curveRotateProg = 0
                        binding.XTRoteSeekBar.progress = 45
                        binding.YTRoteSeekBar.progress = 45
                        binding.ZTRoteSeekBar.progress = SubsamplingScaleImageView.ORIENTATION_180
                        binding.CurveTRoteSeekBar.progress =
                            ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION
                        val rl = AutofitTextRel(this)
                        binding.txtStkrRel.addView(rl)
                        rl.setTextInfo(textInfo, false)
                        rl.id = View.generateViewId()
                        rl.setMainLayoutWH(
                            binding.mainRel.width.toFloat(),
                            binding.mainRel.height.toFloat()
                        )
                        rl.setOnTouchCallbackListener(this)
                        rl.borderVisibility = false
                        childCount2 = binding.txtStkrRel.childCount
                        if (childCount2 != 0) {
                            view2 = binding.txtStkrRel.getChildAt(childCount2 - 1)
                            if ((view2 is AutofitTextRel) && !view2.borderVisibility) {
                                view2.borderVisibility = true
                            }
                        }
                        binding.layOpacity.visibility = View.GONE
                    }
                    if (binding.layTextMain.visibility == View.GONE) {
                        setbottomLayerSelected(R.id.add_text)
                        binding.layOpacity.visibility = View.GONE
                        binding.layTextMain.visibility = View.VISIBLE
                        binding.layTextMain.startAnimation(animSlideUp)
                    }
                }
                if (requestCode == SELECT_PICTURE_FROM_GALLERY) {
                    selectedImageUri = data!!.data
                    try {
                        val bitmap = ImageUtils.getBitmapFromUri(
                            this,
                            selectedImageUri,
                            screenWidth,
                            screenHeight
                        )
                        if (screenWidth <= (bitmap.width.toFloat()) || screenHeight <= (bitmap.height.toFloat())) {
                            btmSticker = bitmap
                        } else {
                            btmSticker = ImageUtils.resizeBitmap(
                                bitmap,
                                screenWidth.toInt(),
                                screenHeight.toInt()
                            )
                        }
                        _main = Intent(this, CropActivityTwo::class.java)
                        _main.putExtra("value", "sticker")
                        startActivity(_main)
                    } catch (e22: Exception) {
                        e22.printStackTrace()
                    }
                }
                if (requestCode == SELECT_PICTURE_FROM_CAMERA) {
                    selectedImageUri = Uri.fromFile(f)
                    try {
                        btmSticker = ImageUtils.getBitmapFromUri(
                            this, selectedImageUri, screenWidth, screenHeight
                        )
                        _main = Intent(this, CropActivityTwo::class.java)
                        _main.putExtra("value", "sticker")
                        startActivity(_main)
                    } catch (e222: Exception) {
                        e222.printStackTrace()
                    }
                }
                if (requestCode == 4) {
                    if (checkTrans) {
                        binding.seekOpacity.progress = 255
                        checkTrans = false
                    }
                    bundle = data!!.extras
                    profile = bundle!!.getString("profile")
                    if ((profile == "no")) {
                        showtailsSeek = false
                        profile = "Temp_Path"
                        ratio = ""
                        try {
                            val btmm = CropActivityTwo.bitmapImage
                            if (btmm != null) {
                                setImageBitmapAndResizeLayout(
                                    ImageUtils.resizeBitmap(
                                        btmm,
                                        typeOfDesign(
                                            screenWidth.toInt(),
                                            screenHeight.toInt()
                                        ).first,
                                        typeOfDesign(
                                            screenWidth.toInt(),
                                            screenHeight.toInt()
                                        ).second
                                    ), "nonCreated"
                                )
                                return
                            }
                            return
                        } catch (e3: OutOfMemoryError) {
                            e = e3
                            e.printStackTrace()
                            Toast.makeText(
                                applicationContext,
                                resources.getString(R.string.picUpImg),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                            return
                        } catch (e4: NullPointerException) {
                            e = e4
                            e.printStackTrace()
                            Toast.makeText(
                                applicationContext,
                                resources.getString(R.string.picUpImg),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                            return
                        }
                    }
                    if (binding.seek.progress == 255) {
                        binding.seek.progress = 127
                        binding.transImg.imageAlpha = 127
                    }

                    if ((profile == "Texture")) {
                        showtailsSeek = true
                    } else {
                        showtailsSeek = false
                    }
                    val ratio = bundle.getString("ratio")
                    val backgroundName = bundle.getString("backgroundName")
                    if ((profile == "Color")) {
                        hex = bundle.getString("color")
                    }
                    if ((profile == "Gradient")) {
                        colors = bundle.getIntArray("colorArr")
                        typeGradient = bundle.getString("typeGradient")
                        orient = bundle["orintation"] as GradientDrawable.Orientation?
                        prog_radious = bundle.getInt("prog_radious")
                    }
                    if (bundle.getBoolean("updateSticker")) {
                        _adapter = StickerViewPagerAdapter(this, supportFragmentManager)
                        binding.imageviewPager.adapter = _adapter
                    }
                    try {
                        drawBackgroundImage(ratio, backgroundName, profile, "nonCreated")
                        return
                    } catch (e5: OutOfMemoryError) {
                        e = e5
                    } catch (e6: NullPointerException) {
                        e = e6
                    }
                }
                else return

            }
            val alertDialog = AlertDialog.Builder(this, 16974126).setMessage(
                ImageUtils.getSpannableString(
                    this, Typeface.DEFAULT, R.string.picUpImg
                )
            ).setPositiveButton(ImageUtils.getSpannableString(this, Typeface.DEFAULT, R.string.ok)) {
                                                                                                    dialog, which -> dialog.cancel()
            }.create()
            alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
            alertDialog.show()
            return
        } else if (requestCode == TEXT_ACTIVITY) {
            editMode = false
            return
        } else {
            return
        }
        //        e.printStackTrace();
//        Toast.makeText(getApplicationContext(), getResources().getString(R.string.picUpImg), 1).show();
//        finish();
    }

    private fun showDialogPicker() {
        val dialog = Dialog(this /*, 16974126*/)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = -1
        lp.height = -2
        (dialog.findViewById<View>(R.id.txt_title) as TextView).typeface = ttfHeader
        (dialog.findViewById<View>(R.id.txtCam) as TextView).typeface = ttf
        (dialog.findViewById<View>(R.id.txtGal) as TextView).typeface = ttf
        (dialog.findViewById<View>(R.id.img_camera) as ImageButton).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View) {
                onCameraButtonClick()
                dialog.dismiss()
            }
        })
        (dialog.findViewById<View>(R.id.img_gallery) as ImageButton).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View) {
                onGalleryButtonClick()
                dialog.dismiss()
            }
        })
        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.show()
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(2)
    }

    fun onCameraButtonClick() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        f = File(Environment.getExternalStorageDirectory(), ".temp.jpg")
        intent.putExtra("output", Uri.fromFile(f))
        startActivityForResult(intent, SELECT_PICTURE_FROM_CAMERA)
    }

    fun onGalleryButtonClick() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction("android.intent.action.PICK")
        startActivityForResult(
            Intent.createChooser(
                intent,
                resources.getString(R.string.select_picture)
            ), SELECT_PICTURE_FROM_GALLERY
        )
    }


    override fun onBackPressed() {

        if (binding.layTextMain.visibility == View.VISIBLE) {
            if (binding.layTextEdit.visibility == View.VISIBLE) {
                removeImageViewControll()
                removeScroll()
                binding.layTextMain.startAnimation(animSlideDown)
                binding.layTextMain.visibility = View.GONE
                return
            }
            backShowDialog()
        } else if (binding.layStkrMain.visibility == View.VISIBLE) {
            if (binding.seekbarContainer.visibility == View.VISIBLE) {
                removeImageViewControll()
                removeScroll()
                binding.layStkrMain.startAnimation(animSlideDown)
                binding.layStkrMain.visibility = View.GONE
                return
            }
            backShowDialog()
        } else if (binding.laySticker.visibility == View.VISIBLE) {
            binding.laySticker.visibility = View.GONE
            binding.btnLayControls.visibility = View.VISIBLE
            setbottomLayerSelected(0)
        } else if (binding.layOpacity.visibility == View.VISIBLE) {
            binding.layOpacity.startAnimation(animSlideDown)
            binding.layOpacity.visibility = View.GONE
        } else if (binding.layContainer.childCount != 0) {
            backShowDialog()
        } else if (binding.layContainer.visibility == View.VISIBLE) {
            binding.layContainer.animate().translationX((-binding.layContainer.right).toFloat())
                .setDuration(200).setInterpolator(
                    AccelerateInterpolator()
                ).start()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.layContainer.visibility = View.GONE
                binding.btnLayControls.visibility = View.VISIBLE
            }, 200)
        } else {
            backShowDialog()
        }
    }

    fun backShowDialog() {
        val dialog = Dialog(this /*, 16974126*/)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.back_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = -1
        lp.height = -2
        (dialog.findViewById<View>(R.id.txtapp) as TextView).typeface = ttfHeader
        (dialog.findViewById<View>(R.id.txt2) as TextView).typeface = ttf
        val dialogButton = dialog.findViewById<View>(R.id.btn_no) as Button
        dialogButton.typeface = ttf
        dialogButton.setOnClickListener { dialog.dismiss() }
        val btn_yes = dialog.findViewById<View>(R.id.btn_yes) as Button
        btn_yes.typeface = ttf
        btn_yes.setOnClickListener {
            editor?.putString("typeOfDesign", "logo")?.commit()
            finish()
            dialog.dismiss()
        }
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        if (/*!remove_ad_pref!!.getBoolean("isAdsDisabled", false)*/ !subscriptionViewModel.isPremiumActive.value!!) {
            dialog.setOnDismissListener { }
        }
        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(2)
        dialog.show()
    }

    override fun onDestroy() {
//        if (!remove_ad_pref!!.getBoolean("isAdsDisabled", false)) {
//            mRewardedVideoAd.destroy(this);
//        }
        super.onDestroy()

        try {
            Thread(object : Runnable {
                override fun run() {
                    try {
                        Glide.get(this@CreatePoster).clearDiskCache()
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }).start()
            Glide.get(this).clearMemory()
            _binding = null
            _adapter = null

            animSlideUp = null
            animSlideDown = null
            if (imgBtmap != null) {
                imgBtmap!!.recycle()
                imgBtmap = null
            }
            if (bitmapNot != null) {
                bitmapNot!!.recycle()
                bitmapNot = null
            }

            activity = null
            if (saveTemplateAsync != null) {
                if (saveTemplateAsync!!.status == AsyncTask.Status.PENDING) {
//                    saveTemplateAsync!!.cancel(true)
                    saveTemplateAsync!!.cancel(true)
                }
                if (saveTemplateAsync!!.status == AsyncTask.Status.RUNNING) {
//                    saveTemplateAsync!!.cancel(true)
                    saveTemplateAsync!!.cancel(true)
                }
            }
            ttf = null
            ttfHeader = null
            focusedView = null
            focusedCopy = null
            pallete = null
            imageId = null
            uriArry.clear()
            if (txtShapeList != null) {
                txtShapeList!!.clear()
            }

            adaptor_txtBg = null
            adapter = null
            listFragment = null
        } catch (e4: OutOfMemoryError) {
            e4.printStackTrace()
        } catch (e32: Exception) {
            e32.printStackTrace()
        }
        Constants.freeMemory()
    }

    override fun ongetSticker() {
        binding.XRoteSeekBar.progress = 45
        binding.YRoteSeekBar.progress = 45
        binding.ZRoteSeekBar.progress = SubsamplingScaleImageView.ORIENTATION_180
        binding.ScaleSeekBar.progress = 10
        try {
            color_Type = "colored"
            if ((color_Type == "white")) {
                binding.layColor.visibility = View.VISIBLE
                binding.layHue.visibility = View.GONE
            } else {
                binding.layColor.visibility = View.GONE
                // lay_hue.setVisibility(View.VISIBLE);
            }
            binding.layOpacity.visibility = View.GONE
            addSticker("0", Constants.saveBitmapObjectSticker(CropActivityTwo.bitmapImage))
        } catch (e: Exception) {
        } catch (e2: Error) {
        }
    }

    override fun onColor(position: Int, way: String, visiPosition: Int) {
        if (position != 0) {
            val childCount = binding.txtStkrRel!!.childCount
            var i: Int
            var view: View?
            if ((way == "txtShadow")) {
                i = 0
                while (i < childCount) {
                    view = binding.txtStkrRel!!.getChildAt(i)
                    if (view is AutofitTextRel) {
                        (binding.txtStkrRel!!.getChildAt(visiPosition) as AutofitTextRel).borderVisibility =
                            true
                        if (view.borderVisibility) {
                            if (binding.seekBarShadow.progress == 0) {
                                binding.seekBarShadow.progress = 5
                            }
                            shadowColor = position
                            view.textShadowColor = position
                        }
                    }
                    i++
                }
                return
            } else if ((way == "txtBg")) {
                i = 0
                while (i < childCount) {
                    view = binding.txtStkrRel!!.getChildAt(i)
                    if (view is AutofitTextRel) {
                        (binding.txtStkrRel!!.getChildAt(visiPosition) as AutofitTextRel).borderVisibility =
                            true
                        if (view.borderVisibility) {
                            if (binding.seekBar3.progress == 0) {
                                binding.seekBar3.progress = 127
                            }
                            bgColor = position
                            bgDrawable = "0"
                            view.bgColor = position
                            view.bgAlpha = binding.seekBar3.progress
                        }
                    }
                    i++
                }
                return
            } else {
                view = binding.txtStkrRel!!.getChildAt(visiPosition)
                if (view is AutofitTextRel) {
                    (binding.txtStkrRel!!.getChildAt(visiPosition) as AutofitTextRel).borderVisibility =
                        true
                    if (view.borderVisibility) {
                        tColor = position
                        textColorSet = position
                        view.textColor = position
                    }
                }
                if (view is ResizableStickerView) {
                    (binding.txtStkrRel!!.getChildAt(visiPosition) as ResizableStickerView).setBorderVisibility(
                        true
                    )
                    if (view.borderVisbilty) {
                        stkrColorSet = position
                        view.color = position
                        return
                    }
                    return
                }
                return
            }
        }
        removeScroll()
        if (binding.layTextMain.visibility == View.VISIBLE) {
            binding.layTextMain.startAnimation(animSlideDown)
            binding.layTextMain.visibility = View.GONE
        }
        if (binding.layStkrMain.visibility == View.VISIBLE) {
            binding.layStkrMain.startAnimation(animSlideDown)
            binding.layStkrMain.visibility = View.GONE
        }
    }

    override fun onDrag(layoutview: View, dragevent: DragEvent): Boolean {
        if (dragevent.action == 3) {
            val view = dragevent.localState as View
            val owner = view.parent as ViewGroup
            val container: ViewGroup = layoutview as MaskableFrameLayout
            if (owner !== container) {
                owner.removeView(view)
                val view1 = container.getChildAt(0)
                val r = view.rotation
                val r1 = view1.rotation
                container.removeAllViews()
                view.scaleX = 1.0f
                view.scaleY = 1.0f
                view1.scaleX = 1.0f
                view1.scaleY = 1.0f
                view.translationX = 0.0f
                view.translationY = 0.0f
                view1.translationX = 0.0f
                view1.translationY = 0.0f
                view.rotation = r1
                view1.rotation = r
                val iv_lp = FrameLayout.LayoutParams(-2, -2)
                iv_lp.gravity = 17
                val iv_lp1 = FrameLayout.LayoutParams(-2, -2)
                iv_lp1.gravity = 17
                view.layoutParams = iv_lp
                view1.layoutParams = iv_lp1
                container.addView(view)
                owner.addView(view1)
            }
        }
        return true
    }

    private fun errorDialogTempInfo(viewIs: String, ratio: String, profile: String, crted: String) {
        val dialog = Dialog(this /*, 16974126*/)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.error_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = -1
        lp.height = -2
        (dialog.findViewById<View>(R.id.txtapp) as TextView).typeface = ttfHeader
        (dialog.findViewById<View>(R.id.txt) as TextView).typeface = ttf
        val btn_ok = dialog.findViewById<View>(R.id.btn_ok) as Button
        btn_ok.typeface = ttf
        btn_ok.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })
        val btn_conti = dialog.findViewById<View>(R.id.btn_conti) as Button
        btn_conti.typeface = ttf
        val str = viewIs
        val str2 = profile
        val str3 = crted
        btn_conti.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dialog.dismiss()
                if ((str == "View")) {
                    val bfo = BitmapFactory.Options()
                    bfo.inJustDecodeBounds = true
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.shape_0,
                        bfo
                    )
                    val optsDownSample = BitmapFactory.Options()
                    optsDownSample.inSampleSize = ImageUtils.getClosestResampleSize(
                        bfo.outWidth,
                        bfo.outHeight,
                        (if (screenWidth < screenHeight) screenWidth else screenHeight).toInt()
                    )
                    bfo.inJustDecodeBounds = false
                    optsDownSample.inScaled = false
                    bitmapRatio(
                        "1:1", str2, BitmapFactory.decodeResource(
                            resources, R.drawable.shape_0, optsDownSample
                        ), str3
                    )
                }
            }
        })
        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.show()
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(2)
    }

    private fun bitmapRatio(ratio: String?, profile: String?, btm: Bitmap, crted: String?) {
        val bit = ImageUtils.resizeBitmap(
            btm,
            typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).first,
            typeOfDesign(screenWidth.toInt(), screenHeight.toInt()).second
        )
        if (crted != "created") {
            setImageBitmapAndResizeLayout(bit, "nonCreated")
        } else if ((profile == "Texture")) {
            setImageBitmapAndResizeLayout(
                Constants.getTiledBitmap(
                    this,
                    frame_Name,
                    bit,
                    binding.seekTailys
                ), "created"
            )
        } else if ((profile == "Card")) {
            setImageBitmapAndResizeLayout(
                Constants.getTiledBitmap(
                    this,
                    frame_Name,
                    bit,
                    binding.seekTailys
                ), "created"
            )
        } else if ((profile == "Poster")) {
            setImageBitmapAndResizeLayout(
                Constants.getTiledBitmap(
                    this,
                    frame_Name,
                    bit,
                    binding.seekTailys
                ), "created"
            )
        } else {
            setImageBitmapAndResizeLayout(bit, "created")
        }
    }

    fun onRewardedVideoAdLoaded() {
    }

    fun onRewardedVideoAdOpened() {
    }

    fun onRewardedVideoStarted() {
    }

    fun onRewardedVideoAdClosed() {
        loadRewardedVideoAd()
    }

    fun onRewarded() {
        onSnapFilter(
            stkrName?.get(pos) ?: "",
            (colotType)!!, ""
        )
    }

    fun onRewardedVideoAdLeftApplication() {
    }

    fun onRewardedVideoAdFailedToLoad(i: Int) {
    }

    fun onRewardedVideoCompleted() {
        onSnapFilter(
            stkrName?.get(pos) ?: "",
            (colotType)!!, ""
        )
    }

    private fun loadRewardedVideoAd() {
//        mRewardedVideoAd.loadAd(getString(R.string.video_admob), new AdRequest.Builder().build());
    }

    override fun onResume() {
        when (viewModel.typeOfDesign) {
//            "poster" -> binding.by.setImageResource(R.drawable.businesscards_watermark_poster)
//            "card" -> binding.by.setImageResource(R.drawable.businesscards_watermark_card)
//            else -> binding.by.setImageResource(R.drawable.businesscards_watermark_logo)
        }

//        if (!remove_ad_pref!!.getBoolean("isAdsDisabled", false)) {
//            mRewardedVideoAd.resume(this);
//        }
        super.onResume()
    }

    override fun onPause() {
//        if (!remove_ad_pref!!.getBoolean("isAdsDisabled", false)) {
//            mRewardedVideoAd.pause(this);
//        }
        super.onPause()
    }

    private fun showPremiumDialog() {
        val dialog = Dialog(this /*, 16974126*/)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.premium_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = -1
        lp.height = -2

        (dialog.findViewById<View>(R.id.txtHeadet) as TextView).typeface =
            Constants.getHeaderTypeface(
                this
            )

        (dialog.findViewById<View>(R.id.headingtxt4) as TextView).setTypeface(
            Constants.getHeaderTypeface(
                this
            ), Typeface.BOLD
        )
        (dialog.findViewById<View>(R.id.txt4) as TextView).typeface = Constants.getHeaderTypeface(
            this
        )

        (dialog.findViewById<View>(R.id.no_thanks) as Button).typeface =
            Constants.getHeaderTypeface(
                this
            )
        (dialog.findViewById<View>(R.id.btn_PremiumMonthly) as RelativeLayout).setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View) {
                    dialog.dismiss()
                    val pakagename = packageName

                    try {
                        startActivity(
                            Intent(
                                "android.intent.action.VIEW", Uri.parse(
                                    "market://details?id=$pakagename"
                                )
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                "android.intent.action.VIEW", Uri.parse(
                                    "https://play.google.com/store/apps/details?id=$pakagename"
                                )
                            )
                        )
                    }
                }
            })

        dialog.findViewById<View>(R.id.no_thanks).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dialog.dismiss()
            }
        })
        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.show()
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(2)
    }


    companion object {
        private val OPEN_CUSTOM_ACITIVITY = 4
        private val SELECT_PICTURE_FROM_CAMERA = 905
        private val SELECT_PICTURE_FROM_GALLERY = 907
        private val TEXT_ACTIVITY = 908
        private val TYPE_STICKER = 9072
        var activity: CreatePoster? = null

        @JvmStatic
        var bitmapNot: Bitmap? = null

        @JvmStatic
        var btmSticker: Bitmap? = null

        @JvmStatic
        var listFragment: ListFragment? = null
    }
}
