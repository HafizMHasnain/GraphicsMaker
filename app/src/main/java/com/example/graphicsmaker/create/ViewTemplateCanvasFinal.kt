package com.example.graphicsmaker.create

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.AsyncTask
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.graphicsmaker.JniUtils
import com.example.graphicsmaker.R
import com.example.graphicsmaker.adapter.RecyclerTemplateAdapter
import com.example.graphicsmaker.create.DatabaseHandler.Companion.getDbHandler
import com.example.graphicsmaker.msl.demo.view.ColorFilterGenerator
import com.example.graphicsmaker.msl.demo.view.ComponentInfo
import com.example.graphicsmaker.msl.textmodule.TextInfo
import com.example.graphicsmaker.utility.ImageUtils
import com.example.graphicsmaker.utility.StorageConfiguration
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class ViewTemplateCanvasFinal () {
    private var context: Context? = null
    private val curTileId: Int = 0
    var job: AsyncTask<*, *, *>? = null
    var jobWork: Boolean = false
    var outputBitmap: Bitmap? = null
    var outputCanvas: Canvas? = null
    var screenH: Float = 0f
    var screenToImageRatio: Float = 1.0f
    var screenW: Float = 0f
    var targetDensity: Int = 640
    var viewHeight: Float = 0f
    var viewWidth: Float = 0f

    constructor (
        context: Context?,
        screenW_half1: Float,
        screenH_half1: Float,
        imageWidth: Float,
        imageHeight: Float,
        job: AsyncTask<*, *, *>?,
        jobWork: Boolean,
        targetDensity: Int
    ) : this() {
        this.context = context
        this.job = job
        this.jobWork = jobWork
        this.screenW = screenW_half1
        this.screenH = screenH_half1
        this.viewWidth = imageWidth
        this.viewHeight = imageHeight
        this.targetDensity = targetDensity
        this.screenToImageRatio = screenW_half1 / imageWidth
    }


    fun getTemplateBitmap(templateId: Int): Bitmap? {
        var option: BitmapFactory.Options
        val templateInfo = getDbHandler(
            context!!
        ).getTemplateByID(templateId)
        if (this.jobWork) {
            if (job!!.isCancelled()) {
                return null
            }
            if (RecyclerTemplateAdapter.isTemplateLoaded) {
                job!!.cancel(true)
                return null
            }
        }
        var tiledScaleValue = 90
        try {
            tiledScaleValue =
                ((templateInfo.seeK_VALUE.toInt().toFloat()) / this.screenToImageRatio).toInt()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        this.outputBitmap = Bitmap.createBitmap(
            viewWidth.toInt(),
            viewHeight.toInt(), Bitmap.Config.ARGB_8888
        )
        this.outputCanvas = Canvas(outputBitmap!!)
        outputCanvas!!.density = this.targetDensity
        if (this.jobWork) {
            if (job!!.isCancelled()) {
                return null
            }
            if (RecyclerTemplateAdapter.isTemplateLoaded) {
                job!!.cancel(true)
                return null
            }
        }
        val bytrArr: ByteArray
        val backgroundBitmap: Bitmap
        val paint: Paint
        Log.d("ViewTemplateCanvasFinal","profile: ${templateInfo.profilE_TYPE}")
        Log.d("ViewTemplateCanvasFinal","frame name: ${templateInfo.framE_NAME}")
        Log.d("ViewTemplateCanvasFinal","art_bg: ${templateInfo.art_BG.height}")
        if (templateInfo.profilE_TYPE == "no") {
            if (templateInfo.art_BG != null){
                outputCanvas?.drawBitmap(templateInfo.art_BG,0f,0f,Paint())
            }

            if (templateInfo.framE_NAME == "") {
                outputCanvas!!.drawColor(Color.argb(templateInfo.overlaY_BLUR, 255, 255, 255))
            } else {
                if (templateInfo.framE_NAME[0].toString() == "b") {
                    option = BitmapFactory.Options()
                    option.inTargetDensity = this.targetDensity
                    bytrArr = JniUtils.decryptResourceJNI(this.context, templateInfo.framE_NAME)
                    backgroundBitmap = ImageUtils.resizeBitmap(
                        BitmapFactory.decodeByteArray(bytrArr, 0, bytrArr.size, option),
                        viewWidth.toInt(), viewHeight.toInt()
                    )
                    backgroundBitmap.density = targetDensity
                    paint = Paint(1)
                    paint.isAntiAlias = true
                    paint.isFilterBitmap = true
                    paint.alpha = templateInfo.overlaY_BLUR
                    outputCanvas!!.drawBitmap(backgroundBitmap, 0.0f, 0.0f, paint)
                }
                else {
                    drawOnCanvas(
                        templateInfo.framE_NAME,
                        this.outputCanvas!!, tiledScaleValue, templateInfo.overlaY_BLUR
                    )
                }
            }
        }
        else if (templateInfo.profilE_TYPE == "Background") {


            option = BitmapFactory.Options()
            option.inTargetDensity = this.targetDensity
            bytrArr = JniUtils.decryptResourceJNI(this.context, templateInfo.framE_NAME)
            backgroundBitmap = ImageUtils.resizeBitmap(
                BitmapFactory.decodeByteArray(bytrArr, 0, bytrArr.size, option),
                viewWidth.toInt(), viewHeight.toInt()
            )
            backgroundBitmap.density = targetDensity
            paint = Paint(1)
            paint.alpha = templateInfo.overlaY_BLUR
            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            outputCanvas!!.drawBitmap(backgroundBitmap, 0.0f, 0.0f, paint)
        }
        else if (templateInfo.profilE_TYPE == "Poster") {

            option = BitmapFactory.Options()
            option.inTargetDensity = this.targetDensity
            bytrArr = JniUtils.decryptResourceJNI(this.context, templateInfo.framE_NAME)
            backgroundBitmap = ImageUtils.resizeBitmap(
                BitmapFactory.decodeByteArray(bytrArr, 0, bytrArr.size, option),
                viewWidth.toInt(), viewHeight.toInt()
            )
            backgroundBitmap.density = targetDensity
            paint = Paint(1)
            paint.alpha = templateInfo.overlaY_BLUR
            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            outputCanvas!!.drawBitmap(backgroundBitmap, 0.0f, 0.0f, paint)
        }
        else if (templateInfo.profilE_TYPE == "Card") {

            option = BitmapFactory.Options()
            option.inTargetDensity = this.targetDensity
            bytrArr = JniUtils.decryptResourceJNI(this.context, templateInfo.framE_NAME)
            backgroundBitmap = ImageUtils.resizeBitmap(
                BitmapFactory.decodeByteArray(bytrArr, 0, bytrArr.size, option),
                viewWidth.toInt(), viewHeight.toInt()
            )
            backgroundBitmap.density = targetDensity
            paint = Paint(1)
            paint.alpha = templateInfo.overlaY_BLUR
            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            outputCanvas!!.drawBitmap(backgroundBitmap, 0.0f, 0.0f, paint)
        }

        else if (templateInfo.profilE_TYPE == "Logo") {
Log.d("CreatePoster", "logo case executed")
            option = BitmapFactory.Options()
            option.inTargetDensity = this.targetDensity
            bytrArr = JniUtils.decryptResourceJNI(this.context, templateInfo.framE_NAME)
            backgroundBitmap = ImageUtils.resizeBitmap(
                BitmapFactory.decodeByteArray(bytrArr, 0, bytrArr.size, option),
                viewWidth.toInt(), viewHeight.toInt()
            )
            backgroundBitmap.density = targetDensity
            paint = Paint(1)
            paint.alpha = templateInfo.overlaY_BLUR
            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            outputCanvas!!.drawBitmap(backgroundBitmap, 0.0f, 0.0f, paint)
        }

        else if (templateInfo.profilE_TYPE == "Texture") {
            if (templateInfo.art_BG != null){
                outputCanvas?.drawBitmap(templateInfo.art_BG,0f,0f,Paint())
            }
            drawOnCanvas(
                templateInfo.framE_NAME,
                this.outputCanvas!!,
                tiledScaleValue,
                templateInfo.overlaY_BLUR
            )
        } else if (templateInfo.profilE_TYPE == "Color") {
            if (templateInfo.art_BG != null){
                outputCanvas?.drawBitmap(templateInfo.art_BG,0f,0f,Paint())
            }
            var colorInt = -1
            try {
                colorInt = Color.parseColor("#" + templateInfo.tempcolor)
            } catch (e2: NumberFormatException) {
            }
            val red = Color.red(colorInt)
            val green = Color.green(colorInt)
            val blue = Color.blue(colorInt)
            val alpha = Color.alpha(colorInt)
            outputCanvas!!.drawColor(Color.argb(templateInfo.overlaY_BLUR, red, green, blue))
        } else if (templateInfo.profilE_TYPE == "Gradient") {
            if (templateInfo.art_BG != null){
                outputCanvas?.drawBitmap(templateInfo.art_BG,0f,0f,Paint())
            }
            try {
                val jSONObject = JSONObject(templateInfo.tempcolor)
                val oorient = jSONObject.getString("Orient")
                val Color1 = jSONObject.getInt("Color1")
                val Color2 = jSONObject.getInt("Color2")
                val gradientDrawable = GradientDrawable(
                    GradientDrawable.Orientation.valueOf(oorient),
                    intArrayOf(Color1, Color2)
                )
                gradientDrawable.mutate()
                if (jSONObject.getString("Type") == "LINEAR") {
                    gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
                } else {
                    gradientDrawable.gradientType = GradientDrawable.RADIAL_GRADIENT
                    gradientDrawable.gradientRadius =
                        viewWidth * ((jSONObject.getInt("Prog_radius")
                            .toFloat()) / this.screenToImageRatio) / (100.0f / this.screenToImageRatio)
                }
                gradientDrawable.setBounds(
                    0, 0, viewWidth.toInt(),
                    viewHeight.toInt()
                )
                gradientDrawable.alpha = templateInfo.overlaY_BLUR
                gradientDrawable.draw(outputCanvas!!)
            }
            catch (e3: JSONException) {
                e3.printStackTrace()
            }
        }
        else if (templateInfo.profilE_TYPE == "Temp_Path") {
            val file: File

            if (StorageConfiguration.getDesignPath().exists()) {
                file = File(templateInfo.temP_PATH)
                if (file.exists()) {
                    try {
                        option = BitmapFactory.Options()
                        option.inTargetDensity = this.targetDensity
                        var backgroundBitmap1 = BitmapFactory.decodeFile(file.absolutePath, option)
                        backgroundBitmap1.density = targetDensity
                        backgroundBitmap1 = ImageUtils.resizeBitmap(
                            backgroundBitmap1,
                            viewWidth.toInt(), viewHeight.toInt()
                        )
                        paint = Paint(1)
                        paint.isAntiAlias = true
                        paint.isFilterBitmap = true
                        paint.alpha = templateInfo.overlaY_BLUR
                        outputCanvas!!.drawBitmap(backgroundBitmap1, 0.0f, 0.0f, paint)
                    } catch (e4: Exception) {
                        e4.printStackTrace()
                    } catch (e5: OutOfMemoryError) {
                        e5.printStackTrace()
                    }
                }
            }
        }
        if (this.jobWork) {
            if (job!!.isCancelled()) {
                return null
            }
            if (RecyclerTemplateAdapter.isTemplateLoaded) {
                job!!.cancel(true)
                return null
            }
        }
        if (templateInfo.overlaY_NAME != "") {
            val resID1 = context!!.resources.getIdentifier(
                templateInfo.overlaY_NAME, "drawable",
                context!!.packageName
            )
            option = BitmapFactory.Options()
            option.inTargetDensity = this.targetDensity
            val overlayBitmap = ImageUtils.resizeBitmap(
                BitmapFactory.decodeResource(
                    context!!.resources, resID1, option
                ),
                viewWidth.toInt(), viewHeight.toInt()
            )
            val transparentpaintoverlay = Paint()
            transparentpaintoverlay.isAntiAlias = true
            transparentpaintoverlay.isFilterBitmap = true
            transparentpaintoverlay.alpha = templateInfo.overlaY_OPACITY
            outputCanvas!!.drawBitmap(overlayBitmap, 0.0f, 0.0f, transparentpaintoverlay)
            overlayBitmap.recycle()
        }
        if (this.jobWork) {
            if (job!!.isCancelled()) {
                return null
            }
            if (RecyclerTemplateAdapter.isTemplateLoaded) {
                job!!.cancel(true)
                return null
            }
        }
        val resID = context!!.resources.getIdentifier(
            templateInfo.shaP_NAME, "drawable",
            context!!.packageName
        )
        option = BitmapFactory.Options()
        option.inTargetDensity = this.targetDensity
        val maskBitmap = ImageUtils.resizeBitmap(
            BitmapFactory.decodeResource(
                context!!.resources, resID, option
            ), viewWidth.toInt(),
            viewHeight.toInt()
        )
        val paint2 = Paint()
        paint2.isAntiAlias = true
        paint2.isFilterBitmap = true
        paint2.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        outputCanvas!!.drawBitmap(maskBitmap, 0.0f, 0.0f, paint2)
        maskBitmap.recycle()
        if (this.jobWork) {
            if (job!!.isCancelled()) {
                return null
            }
            if (RecyclerTemplateAdapter.isTemplateLoaded) {
                job!!.cancel(true)
                return null
            }
        }
        addStickerAndTextToCanvas(this.outputCanvas!!, templateInfo.templatE_ID)
        return this.outputBitmap
    }

    fun getWaterMarkedBitmap(logo: Bitmap): Bitmap? {
        var logo = logo
        logo.density = targetDensity
        val wr = outputCanvas!!.width.toFloat()
        val hr = outputCanvas!!.height.toFloat()
        val wdl = logo.width.toFloat()
        val hel = logo.height.toFloat()
        val rat1 = wdl / hel
        val rat2 = hel / wdl
        var wd = wr / 3.0f
        var he = wd * rat2
        if (wd > wr) {
            wd = wr
            logo = Bitmap.createScaledBitmap(
                logo, wd.toInt(),
                (wd * rat2).toInt(), false
            )
        } else if (he > hr) {
            he = hr
            logo = Bitmap.createScaledBitmap(
                logo,
                (he * rat1).toInt(), he.toInt(), false
            )
        } else {
            logo = Bitmap.createScaledBitmap(logo, wd.toInt(), he.toInt(), false)
            Log.i("testing", "In else condition")
        }
        outputCanvas!!.drawBitmap(
            logo,
            (outputBitmap!!.width - logo.width).toFloat(),
            (outputBitmap!!.height - logo.height).toFloat(), null
        )
        logo.recycle()
        return this.outputBitmap
    }

    private fun addStickerAndTextToCanvas(outputCanvas: Canvas, templateId: Int): Boolean {
        val dh = getDbHandler(
            context!!
        )
        val stickerInfoList = dh.getComponentInfoList(templateId, "STICKER")
        val TextInfoList = dh.getTextInfoList(templateId)
        val txtShapeList: HashMap<Int, Any> = HashMap()
        var it: Iterator<*> = stickerInfoList.iterator()
        while (it.hasNext()) {
            val ci = it.next() as ComponentInfo
            txtShapeList[ci.order] = ci
        }
        it = TextInfoList.iterator()
        while (it.hasNext()) {
            val textInfo = it.next() as TextInfo
            txtShapeList[textInfo.order] = textInfo
        }
        if (txtShapeList.size == 0) {
            return false
        }
        val arrayList: List<*> = ArrayList<Any>(txtShapeList.keys)
        val len = arrayList.size
        for (j in 0 until len) {
            val obj = txtShapeList[arrayList[j]]!!
            if (obj is ComponentInfo) {
                var totalImageViewWidth = obj.width.toFloat()
                var totalImageViewHeight = obj.height.toFloat()
                val field_two = obj.fielD_TWO
                var lm = 0
                var tm = 0
                if ("" != field_two) {
                    val marginArr = field_two.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    lm = marginArr[0].toInt()
                    tm = marginArr[1].toInt()
                }
                var maxW = this.screenW
                var maxH = this.screenH
                if (lm < 0) {
                    maxW -= lm.toFloat()
                }
                if (tm < 0) {
                    maxH -= tm.toFloat()
                }
                if (totalImageViewWidth > maxW) {
                    totalImageViewWidth = maxW
                }
                if (totalImageViewHeight > maxH) {
                    totalImageViewHeight = maxH
                }
                val newY = obj.poS_Y / this.screenToImageRatio
                val newWidth = totalImageViewWidth / this.screenToImageRatio
                val newHeight = totalImageViewHeight / this.screenToImageRatio
                (obj as ComponentInfo).poS_X =
                    (obj as ComponentInfo).poS_X / this.screenToImageRatio
                (obj as ComponentInfo).poS_Y = newY
                (obj as ComponentInfo).width =
                    newWidth.toInt()
                (obj as ComponentInfo).height =
                    newHeight.toInt()
                if ((obj as ComponentInfo).fielD_TWO != "") {
                    (obj as ComponentInfo).fielD_TWO =
                        getMargin((obj as ComponentInfo).fielD_TWO)
                }
                if (this.jobWork) {
                    if (job!!.isCancelled()) {
                        return false
                    }
                    if (RecyclerTemplateAdapter.isTemplateLoaded) {
                        job!!.cancel(true)
                        return false
                    }
                }
                setStickerDrawable(
                    outputCanvas,
                    obj.reS_ID,
                    obj.stkR_PATH, obj as ComponentInfo
                )
            } else {
                if ((obj as TextInfo?)!!.fielD_TWO != "") {
                    (obj as TextInfo?)!!.fielD_TWO =
                        getMargin(
                            (obj as TextInfo?)!!.fielD_TWO
                        )
                }
                if (this.jobWork) {
                    if (job!!.isCancelled()) {
                        return false
                    }
                    if (RecyclerTemplateAdapter.isTemplateLoaded) {
                        job!!.cancel(true)
                        return false
                    }
                }
                setTextDrawableOnCanvas(outputCanvas, obj)
            }
        }
        return false
    }

    private fun setStickerDrawable(
        outputCanvas: Canvas,
        res_id: String,
        stkr_path: String,
        obj: ComponentInfo
    ) {
        val XofImageView = obj.poS_X
        val YofImageView = obj.poS_Y
        val totalImageViewWidth = obj.width.toFloat()
        val totalImageViewHeight = obj.height.toFloat()
        var stkrBitmap: Bitmap? = null
        val option: BitmapFactory.Options
        if (stkr_path == "") {
            option = BitmapFactory.Options()
            option.inTargetDensity = this.targetDensity
            val bytrArr = JniUtils.decryptResourceJNI(this.context, res_id)
            stkrBitmap = BitmapFactory.decodeByteArray(bytrArr, 0, bytrArr.size, option)
        } else {
            var file = StorageConfiguration.getDesignPath()
            if (file.exists() || file.mkdirs()) {
                if (StorageConfiguration.getDesignPath().exists()) {
                    file = File(stkr_path)
                    if (file.exists()) {
                        try {
                            option = BitmapFactory.Options()
                            option.inTargetDensity = this.targetDensity
                            stkrBitmap = BitmapFactory.decodeFile(file.absolutePath, option)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        } catch (e2: OutOfMemoryError) {
                            e2.printStackTrace()
                        }
                    }
                }
            } else {
                Log.d("", "Can't create directory to save image.")
                Toast.makeText(
                    this.context,
                    context!!.resources.getString(R.string.create_dir_err), Toast.LENGTH_LONG
                ).show()
                return
            }
        }
        if (stkrBitmap != null) {
            val ratio = (stkrBitmap.width.toFloat()) / (stkrBitmap.height.toFloat())
            val buttonMargin = (dpToPx(this.context, 25).toFloat()) / this.screenToImageRatio
            val WidthAfterDeductingMargin = totalImageViewWidth - (2.0f * buttonMargin)
            val HeightAfterDeductingMargin = totalImageViewHeight - (2.0f * buttonMargin)
            val WidthAfterMarginAndScaling =
                ((obj.scaleProg.toFloat()) * WidthAfterDeductingMargin) / 10.0f
            val HeightAfterMarginAndScaling =
                ((obj.scaleProg.toFloat()) * HeightAfterDeductingMargin) / 10.0f
            var drawableWidth = WidthAfterMarginAndScaling
            var drawableHeight = HeightAfterMarginAndScaling
            var drawableXPosition =
                (XofImageView + buttonMargin) + ((WidthAfterDeductingMargin - WidthAfterMarginAndScaling) / 2.0f)
            var drawableYPosition =
                (YofImageView + buttonMargin) + ((HeightAfterDeductingMargin - HeightAfterMarginAndScaling) / 2.0f)
            val nh1 = drawableWidth / ratio
            val nw2 = drawableHeight * ratio
            val nh2 = drawableHeight
            if (drawableWidth <= drawableWidth && nh1 <= drawableHeight) {
                drawableYPosition += (drawableHeight - nh1) / 2.0f
                drawableHeight = nh1
            } else if (nw2 <= drawableWidth && nh2 <= drawableHeight) {
                drawableXPosition += (drawableWidth - nw2) / 2.0f
                drawableWidth = nw2
            }
            if (drawableWidth <= 0.0f || drawableHeight <= 0.0f) {
                Log.e("Less then 0", "$drawableWidth $drawableHeight")
                return
            }
            var filter: ColorFilter? = null
            stkrBitmap = Bitmap.createScaledBitmap(
                stkrBitmap,
                drawableWidth.toInt(),
                drawableHeight.toInt(),
                true
            )
            val camera = Camera()
            val cx = (drawableWidth.toInt()) / 2
            val cy = (drawableHeight.toInt()) / 2
            camera.save()
            val yRotation = obj.yRotateProg.toFloat()
            val zRotation = obj.zRotateProg.toFloat()
            camera.rotateX(45.0f - (obj.xRotateProg.toFloat()))
            camera.rotateY(45.0f - yRotation)
            camera.rotateZ(180.0f + zRotation)
            camera.setLocation(0.0f, 0.0f, -8.0f / this.screenToImageRatio)
            val mtx = Matrix()
            camera.getMatrix(mtx)
            mtx.preTranslate((-cx).toFloat(), (-cy).toFloat())
            mtx.postRotate(obj.rotation)
            camera.restore()
            val transparentpaintStkr = Paint()
            transparentpaintStkr.alpha = Math.round(((obj.stC_OPACITY.toFloat()) / 100.0f) * 255.0f)
            val lightingColorFilter: ColorFilter
            if (obj.colortype == "white") {
                lightingColorFilter = if (obj.stC_COLOR == 0) {
                    LightingColorFilter(0, -1)
                } else {
                    LightingColorFilter(0, obj.stC_COLOR)
                }
            } else if (obj.stC_HUE == 0) {
                lightingColorFilter = LightingColorFilter(0, -1)
            } else if (obj.stC_HUE == 360) {
                lightingColorFilter = LightingColorFilter(0, ViewCompat.MEASURED_STATE_MASK)
            } else if (obj.stC_HUE < 1 || obj.stC_HUE > 5) {
                filter = ColorFilterGenerator.adjustHue(obj.stC_HUE.toFloat())
            } else {
                filter = null
            }
            transparentpaintStkr.setColorFilter(filter)
            transparentpaintStkr.isAntiAlias = true
            transparentpaintStkr.isFilterBitmap = true
            if (obj.y_ROTATION != 0.0f) {
                mtx.preScale(-1.0f, 1.0f, cx.toFloat(), cy.toFloat())
            }
            mtx.postTranslate(
                (cx.toFloat()) + drawableXPosition,
                (cy.toFloat()) + drawableYPosition
            )
            outputCanvas.drawBitmap(stkrBitmap, mtx, transparentpaintStkr)
            stkrBitmap.recycle()
        }
    }

    private fun setTextDrawableOnCanvas(outputCanvas: Canvas, obj: TextInfo) {
        val XofImageView = obj.poS_X / this.screenToImageRatio
        val YofImageView = obj.poS_Y / this.screenToImageRatio
        val totalImageViewWidth = (obj.width.toFloat()) / this.screenToImageRatio
        val totalImageViewHeight = (obj.height.toFloat()) / this.screenToImageRatio
        val dpToPixelMarginX =
            (dpToPx(this.context, 25.0f.toInt()).toFloat()) / this.screenToImageRatio
        val dpToPixelMarginY =
            (dpToPx(this.context, 25.0f.toInt()).toFloat()) / this.screenToImageRatio
        val dpToPixelMarginRelX =
            (dpToPx(this.context, 25.0f.toInt()).toFloat()) / this.screenToImageRatio
        val dpToPixelMarginRelY =
            (dpToPx(this.context, 25.0f.toInt()).toFloat()) / this.screenToImageRatio
        val XafterShiftingMargin = XofImageView + dpToPixelMarginX
        val YafterShiftingMargin = YofImageView + dpToPixelMarginY
        var WidthAfterDeductingMargin = totalImageViewWidth - (2.0f * dpToPixelMarginX)
        var HeightAfterDeductingMargin = totalImageViewHeight - (2.0f * dpToPixelMarginY)
        if (WidthAfterDeductingMargin <= 0.0f) {
            WidthAfterDeductingMargin = 1.0f
        }
        if (HeightAfterDeductingMargin <= 0.0f) {
            HeightAfterDeductingMargin = 1.0f
        }
        val drawableXPosition = XafterShiftingMargin
        val drawableYPosition = YafterShiftingMargin
        val bitmapText1 = getTextBitmap2(
            obj,
            Rect(0, 0, WidthAfterDeductingMargin.toInt(), HeightAfterDeductingMargin.toInt()),
            drawableXPosition,
            drawableYPosition
        )
        val curveRelWidth = totalImageViewWidth - (2.0f * dpToPixelMarginRelX)
        val curveRelHeight = totalImageViewHeight - (2.0f * dpToPixelMarginRelY)
        val curveRelX = XofImageView + dpToPixelMarginRelX
        val curveRelY = YafterShiftingMargin + dpToPixelMarginRelX
        val bitmapText = Bitmap.createBitmap(
            curveRelWidth.toInt(),
            curveRelHeight.toInt(),
            Bitmap.Config.ARGB_8888
        )
        var canvas = Canvas(bitmapText)
        canvas.density = targetDensity
        val shiftX = (curveRelWidth - (bitmapText1.width.toFloat())) / 2.0f
        val shiftY = (curveRelHeight - (bitmapText1.height.toFloat())) / 2.0f
        val mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isFilterBitmap = true
        canvas.drawBitmap(bitmapText1, shiftX, shiftY, mPaint)
        val bitmapForCurves =
            Bitmap.createBitmap(bitmapText.width, bitmapText.height, Bitmap.Config.ARGB_8888)
        val canvasForCurves = Canvas(bitmapForCurves)
        canvasForCurves.density = targetDensity
        val curveroatationValue =
            if (obj.curveRotateProg == ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION) {
                0
            } else {
                obj.curveRotateProg
            }
        val stockArr = getCurvingArray(
            bitmapText.width, bitmapText.height,
            ((curveroatationValue.toFloat()) / this.screenToImageRatio).toInt()
        )
        canvasForCurves.translate(0.0f, 0.0f)
        canvasForCurves.drawBitmapMesh(bitmapText, 8, 8, stockArr, 0, null, 0, mPaint)
        bitmapText.recycle()
        val bitmapForRotation =
            Bitmap.createBitmap(bitmapText.width, bitmapText.height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmapForRotation)
        canvas.density = targetDensity
        val camera = Camera()
        val cx = bitmapForCurves.width / 2
        val cy = bitmapForCurves.height / 2
        camera.save()
        val yRotation = obj.yRotateProg.toFloat()
        val zRotation = obj.zRotateProg.toFloat()
        val camera2 = camera
        camera2.rotateX(45.0f - (obj.xRotateProg.toFloat()))
        camera.rotateY(45.0f - yRotation)
        camera.rotateZ(180.0f + zRotation)
        camera.setLocation(0.0f, 0.0f, -8.0f / this.screenToImageRatio)
        val mtx = Matrix()
        camera.getMatrix(mtx)
        mtx.preTranslate((-cx).toFloat(), (-cy).toFloat())
        camera.restore()
        mtx.postTranslate(cx.toFloat(), cy.toFloat())
        canvas.drawBitmap(bitmapForCurves, mtx, mPaint)
        bitmapForCurves.recycle()
        System.gc()
        Runtime.getRuntime().gc()
        val finalTextBitmap = Bitmap.createBitmap(
            totalImageViewWidth.toInt(),
            totalImageViewHeight.toInt(),
            Bitmap.Config.ARGB_8888
        )
        canvas = Canvas(finalTextBitmap)
        canvas.density = targetDensity
        val paint = Paint()
        if (obj.bG_COLOR != 0) {
            paint.color = obj.bG_COLOR
        } else {
            paint.color = 0
        }
        canvas.translate(0.0f, 0.0f)
        paint.alpha = obj.bG_ALPHA
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isSubpixelText = true
        if (obj.bG_DRAWABLE == "0") {
            canvas.drawPaint(paint)
        } else {
            canvas.drawBitmap(
                getTiledBitmap(
                    this.context,
                    context!!.resources.getIdentifier(
                        obj.bG_DRAWABLE, "drawable",
                        context!!.packageName
                    ),
                    ((obj.width.toFloat()) / this.screenToImageRatio).toInt(),
                    ((obj.height.toFloat()) / this.screenToImageRatio).toInt(), 0.5f
                ), 0.0f, 0.0f, paint
            )
        }
        canvas.drawBitmap(
            bitmapForRotation,
            (totalImageViewWidth - (bitmapForRotation.width.toFloat())) / 2.0f,
            (totalImageViewHeight - (bitmapForRotation.height.toFloat())) / 2.0f, null
        )
        val rotateMatrix = Matrix()
        val centerX = (totalImageViewWidth.toInt()) / 2
        val centerY = (totalImageViewHeight.toInt()) / 2
        rotateMatrix.preTranslate((-centerX).toFloat(), (-centerY).toFloat())
        rotateMatrix.postRotate(obj.rotation)
        rotateMatrix.postTranslate(
            (centerX.toFloat()) + XofImageView,
            (centerY.toFloat()) + YofImageView
        )
        val paintForOutput = Paint()
        paintForOutput.isAntiAlias = true
        paintForOutput.isFilterBitmap = true
        paintForOutput.isSubpixelText = true
        outputCanvas.drawBitmap(finalTextBitmap, rotateMatrix, paintForOutput)
        bitmapForRotation.recycle()
        finalTextBitmap.recycle()
    }

    private fun getCurvingArray(width: Int, height: Int, progress: Int): FloatArray {
        val w = width
        val h = height
        val wd = w / 8
        val hd = h / 8
        val y10 = 0 + hd
        val x18 = 0 + w
        val y18 = 0 + hd
        val y19 = 0 + (hd * 2)
        val x27 = 0 + w
        val y27 = 0 + (hd * 2)
        val y28 = 0 + (hd * 3)
        val x36 = 0 + w
        val y36 = 0 + (hd * 3)
        val y37 = 0 + (hd * 4)
        val x45 = 0 + w
        val y45 = 0 + (hd * 4)
        val y46 = 0 + (hd * 5)
        val x54 = 0 + w
        val y54 = 0 + (hd * 5)
        val y55 = 0 + (hd * 6)
        val x63 = 0 + w
        val y63 = 0 + (hd * 6)
        val y64 = 0 + (hd * 7)
        val x72 = 0 + w
        val y72 = 0 + (hd * 7)
        val y73 = 0 + h
        val x81 = 0 + w
        val y81 = 0 + h
        val path1 = mPath(0, 0, 0 + w, 0, progress)
        val path2 = mPath(0, y10, x18, y18, progress)
        val path3 = mPath(0, y19, x27, y27, progress)
        val path4 = mPath(0, y28, x36, y36, progress)
        val path5 = mPath(0, y37, x45, y45, progress)
        val path6 = mPath(0, y46, x54, y54, progress)
        val path7 = mPath(0, y55, x63, y63, progress)
        val path8 = mPath(0, y64, x72, y72, progress)
        val path9 = mPath(0, y73, x81, y81, progress)
        val arrayPath = arrayOf(path1, path2, path3, path4, path5, path6, path7, path8, path9)
        val mVerts1: ArrayList<Float?> = arrayListOf()
        for (pathMeasure in arrayPath) {
            val pathMeasure2 = PathMeasure(pathMeasure, false)
            var fArr = FloatArray(2)
            fArr = floatArrayOf(0.0f, 0.0f)
            var i = 0.0f
            while (i <= 1.0f) {
                pathMeasure2.getPosTan(pathMeasure2.length * i, fArr, null)
                mVerts1.add(fArr[0])
                mVerts1.add(fArr[1])
                i += 0.125f
            }
        }
        val stockArr = FloatArray(mVerts1.size)
        for (k in stockArr.indices) {
            stockArr[k] = (mVerts1[k]!!)
        }
        return stockArr
    }

    fun mPath(x1: Int, y1: Int, x2: Int, y2: Int, radius: Int): Path {
        val path = Path()
        val midX = x1 + ((x2 - x1) / 2)
        val midY = y1 + ((y2 - y1) / 2)
        val angleRadians = Math.toRadians(
            (atan2(
                (midY - y1).toFloat().toDouble(), (midX - x1).toFloat().toDouble()
            ) * 57.29577951308232) - 90.0
        )
        val pointX = ((midX.toDouble()) + ((radius.toDouble()) * cos(angleRadians))).toFloat()
        val pointY = ((midY.toDouble()) + ((radius.toDouble()) * sin(angleRadians))).toFloat()
        path.moveTo(x1.toFloat(), y1.toFloat())
        path.cubicTo(x1.toFloat(), y1.toFloat(), pointX, pointY, x2.toFloat(), y2.toFloat())
        return path
    }

    private fun getTextBitmap2(
        textInfo: TextInfo,
        bounds: Rect,
        drawableXPosition: Float,
        drawableYPosition: Float
    ): Bitmap {
        val sl: StaticLayout
        val textOnCanvas = textInfo.text
        val textPaint = TextPaint(1)
        textPaint.density = context!!.resources.displayMetrics.density
        try {
            val ttf = if (textInfo.fonT_NAME == "default" || textInfo.fonT_NAME == "") {
                Typeface.DEFAULT
            } else {
                Typeface.createFromAsset(context!!.assets, textInfo.fonT_NAME)
            }
            textPaint.setTypeface(ttf)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        textPaint.textSize = getOptimumTextSize(
            5,
            1000,
            textOnCanvas,
            RectF(
                bounds.left.toFloat(),
                bounds.top.toFloat(),
                bounds.width().toFloat(),
                bounds.height().toFloat()
            ),
            textPaint
        ).toFloat()
        textPaint.color = textInfo.texT_COLOR
        textPaint.isAntiAlias = true
        textPaint.isFilterBitmap = true
        val textAlpha = Math.round(((textInfo.texT_ALPHA.toFloat()) / 100.0f) * 255.0f)
        textPaint.alpha = textAlpha
        textPaint.setShadowLayer(
            (textInfo.shadoW_PROG.toFloat()) / this.screenToImageRatio,
            0.0f,
            0.0f,
            ColorUtils.setAlphaComponent(textInfo.shadoW_COLOR, textAlpha)
        )
        sl = if (textInfo.texT_GRAVITY == null) {
            StaticLayout(
                textOnCanvas,
                textPaint,
                bounds.width(),
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0.0f,
                true
            )
        } else if (textInfo.texT_GRAVITY == "L") {
            StaticLayout(
                textOnCanvas,
                textPaint,
                bounds.width(),
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0.0f,
                true
            )
        } else if (textInfo.texT_GRAVITY == "R") {
            StaticLayout(
                textOnCanvas,
                textPaint,
                bounds.width(),
                Layout.Alignment.ALIGN_OPPOSITE,
                1.0f,
                0.0f,
                true
            )
        } else {
            StaticLayout(
                textOnCanvas,
                textPaint,
                bounds.width(),
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0.0f,
                true
            )
        }
        val bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.density = targetDensity
        canvas.save()
        val textHeight = getTextHeight(textOnCanvas, textPaint)
        val numberOfTextLines = sl.lineCount
        val textXCoordinate = bounds.left.toFloat()
        canvas.translate(textXCoordinate, ((bounds.height() - sl.height) / 2).toFloat())
        sl.draw(canvas)
        canvas.restore()
        return bitmap
    }

    private fun getOptimumTextSize(
        start: Int,
        end: Int,
        text: String,
        availableSpace: RectF,
        textPaint: TextPaint
    ): Int {
        return binarySearch(start, end, text, availableSpace, textPaint)
    }

    private fun binarySearch(
        start: Int,
        end: Int,
        text: String,
        availableSpace: RectF,
        textPaint: TextPaint
    ): Int {
        var lastBest = start
        var lo = start
        var hi = end - 1
        while (lo <= hi) {
            val i = (lo + hi) ushr 1
            val midValCmp = onTestSize(i, text, availableSpace, textPaint)
            if (midValCmp < 0) {
                lastBest = lo
                lo = i + 1
            } else if (midValCmp <= 0) {
                return i
            } else {
                hi = i - 1
                lastBest = hi
            }
        }
        return lastBest
    }

    fun onTestSize(
        suggestedSize: Int,
        text: String,
        availableSpace: RectF,
        textPaint: TextPaint
    ): Int {
        textPaint.textSize = suggestedSize.toFloat()
        val textRect = RectF()
        val layout = StaticLayout(
            text,
            textPaint,
            availableSpace.width().toInt(),
            Layout.Alignment.ALIGN_NORMAL,
            1f,
            0f,
            true
        )
        textRect.bottom = layout.height.toFloat()
        var maxWidth = -1
        val lineCount = layout.lineCount
        for (i in 0 until lineCount) {
            val end = layout.getLineEnd(i)
            if (i < lineCount - 1 && end > 0) {
                if (!isValidWordWrap(text[end - 1], text[end])) {
                    return 1
                }
            }
            if ((maxWidth.toFloat()) < layout.getLineRight(i) - layout.getLineLeft(i)) {
                maxWidth = (layout.getLineRight(i).toInt()) - (layout.getLineLeft(i).toInt())
            }
        }
        textRect.right = maxWidth.toFloat()
        textRect.offsetTo(0.0f, 0.0f)
        if (availableSpace.contains(textRect)) {
            return -1
        }
        return 1
    }

    fun isValidWordWrap(before: Char, after: Char): Boolean {
        return before == ' ' || before == '-'
    }

    private fun getTextHeight(text: String, paint: Paint): Float {
        val rect = Rect()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.getTextBounds(text, 0, text.length, rect)
        return rect.height().toFloat()
    }

    private fun drawOnCanvas(
        resId: String,
        canvas: Canvas,
        tileScale: Int,
        overlayBlur: Int
    ): Boolean {
        val rect = Rect(0, 0, canvas.width, canvas.height)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        val tileHeightAndWidth = ((tileScale.toFloat()) + (10.0f / this.screenToImageRatio)).toInt()
        val option = BitmapFactory.Options()
        option.inTargetDensity = this.targetDensity
        val bytes = JniUtils.decryptResourceJNI(this.context, resId)
        val tiledBitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, option),
            tileHeightAndWidth,
            tileHeightAndWidth,
            true
        )
        tiledBitmap.density = targetDensity
        paint.setShader(BitmapShader(tiledBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT))
        paint.alpha = overlayBlur
        canvas.drawRect(rect, paint)
        tiledBitmap.recycle()
        return true
    }

    fun getMargin(margin: String): String {
        val parts = margin.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val LeftMargin = parts[0].toInt()
        return ((LeftMargin.toFloat()) / this.screenToImageRatio).toInt()
            .toString() + "," + ((parts[1].toInt()
            .toFloat()) / this.screenToImageRatio).toInt().toString()
    }

    private fun getTiledBitmap(
        ctx: Context?,
        resId: Int,
        width: Int,
        height: Int,
        ratioOfView: Float
    ): Bitmap {
        var ratioOfView = ratioOfView
        if (ratioOfView <= 0.0f) {
            ratioOfView = 1.0f
        }
        val rect = Rect(0, 0, width, height)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        val options = BitmapFactory.Options()
        options.inTargetDensity = this.targetDensity
        val t = BitmapFactory.decodeResource(ctx!!.resources, resId, options)
        paint.setShader(
            BitmapShader(
                Bitmap.createScaledBitmap(
                    t,
                    ((t.width.toFloat()) * ratioOfView).toInt(),
                    ((t.height.toFloat()) * ratioOfView).toInt(), false
                ), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT
            )
        )
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(b)
        canvas.density = targetDensity
        canvas.drawRect(rect, paint)
        return b
    }

    fun dpToPx(c: Context?, dp: Int): Int {
        val f = dp.toFloat()
        c!!.resources
        return (f * Resources.getSystem().displayMetrics.density).toInt()
    }


}