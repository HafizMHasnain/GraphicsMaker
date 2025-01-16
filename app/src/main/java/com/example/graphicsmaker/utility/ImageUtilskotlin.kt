package com.example.graphicsmaker.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.WindowManager
import android.widget.SeekBar
import com.example.graphicsmaker.JniUtils
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.max

object ImageUtilskotlin {
    @JvmStatic
    @Throws(IOException::class)
    fun getBitmapFromUri(
        context: Context,
        uri: Uri,
        screenWidth: Float,
        screenHeight: Float
    ): Bitmap? {
        var screenWidth = screenWidth
        try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val bfo = BitmapFactory.Options()
            bfo.inJustDecodeBounds = true
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, bfo)
            val optsDownSample = BitmapFactory.Options()
            if (screenWidth <= screenHeight) {
                screenWidth = screenHeight
            }
            val maxDim = screenWidth.toInt()
            optsDownSample.inSampleSize =
                getClosestResampleSize(bfo.outWidth, bfo.outHeight, maxDim)
            var image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, optsDownSample)
            val m = Matrix()
            if (image.width > maxDim || image.height > maxDim) {
                val optsScale = getResampling(image.width, image.height, maxDim)
                m.postScale(
                    (optsScale.outWidth.toFloat()) / (image.width.toFloat()),
                    (optsScale.outHeight.toFloat()) / (image.height.toFloat())
                )
            }
            val pathInput = getRealPathFromURI(uri, context)
            if (Build.VERSION.SDK > 4.toString()) {
                val rotation = ExifUtils.getExifRotation(pathInput)
                if (rotation != 0) {
                    m.postRotate(rotation.toFloat())
                }
            }
            image = Bitmap.createBitmap(image, 0, 0, image.width, image.height, m, true)
            parcelFileDescriptor.close()
            return image
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            return null
        }
    }

    @Throws(IOException::class)
    fun getResampleImageBitmap(uri: Uri, context: Context, maxDim: Int): Bitmap? {
        var bmp: Bitmap? = null
        try {
            bmp = resampleImage(getRealPathFromURI(uri, context), maxDim)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bmp
    }

    @Throws(IOException::class)
    fun getResampleImageBitmap(uri: Uri, context: Context): Bitmap {
        val pathInput = getRealPathFromURI(uri, context)
        try {
            val display =
                (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            val height = size.y
            return resampleImage(pathInput, width)
        } catch (e: Exception) {
            e.printStackTrace()
            return MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(pathInput))
        }
    }

    @SuppressLint("UseValueOf")
    @Throws(Exception::class)
    fun resampleImage(path: String?, maxDim: Int): Bitmap {
        val bfo = BitmapFactory.Options()
        bfo.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, bfo)
        val optsDownSample = BitmapFactory.Options()
        optsDownSample.inSampleSize = getClosestResampleSize(bfo.outWidth, bfo.outHeight, maxDim)
        val bmpt = BitmapFactory.decodeFile(path, optsDownSample)
        val m = Matrix()
        if (bmpt.width > maxDim || bmpt.height > maxDim) {
            val optsScale = getResampling(bmpt.width, bmpt.height, maxDim)
            m.postScale(
                (optsScale.outWidth.toFloat()) / (bmpt.width.toFloat()),
                (optsScale.outHeight.toFloat()) / (bmpt.height.toFloat())
            )
        }
        if (Build.VERSION.SDK > 4.toString()) {
            val rotation = ExifUtils.getExifRotation(path)
            if (rotation != 0) {
                m.postRotate(rotation.toFloat())
            }
        }
        return Bitmap.createBitmap(bmpt, 0, 0, bmpt.width, bmpt.height, m, true)
    }

    fun getResampling(cx: Int, cy: Int, max: Int): BitmapFactory.Options {
        val bfo = BitmapFactory.Options()
        val scaleVal = if (cx > cy) {
            max.toFloat() / (cx.toFloat())
        } else if (cy > cx) {
            max.toFloat() / (cy.toFloat())
        } else {
            max.toFloat() / (cx.toFloat())
        }
        bfo.outWidth = (((cx.toFloat()) * scaleVal) + 0.5f).toInt()
        bfo.outHeight = (((cy.toFloat()) * scaleVal) + 0.5f).toInt()
        return bfo
    }

    fun getClosestResampleSize(cx: Int, cy: Int, maxDim: Int): Int {
        val max = max(cx.toDouble(), cy.toDouble()).toInt()
        var resample = 1
        while (resample < Int.MAX_VALUE) {
            if (resample * maxDim > max) {
                resample--
                break
            }
            resample++
        }
        return if (resample > 0) resample else 1
    }

    @Throws(Exception::class)
    fun getBitmapDims(path: String?): BitmapFactory.Options {
        val bfo = BitmapFactory.Options()
        bfo.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, bfo)
        return bfo
    }

//    fun getRealPathFromURI(contentURI: Uri, context: Context): String? {
//        try {
//            val cursor = context.contentResolver.query(contentURI, null, null, null, null)
//                ?: return contentURI.path
//            cursor.moveToFirst()
//            val result = cursor.getString(cursor.getColumnIndex("_data"))
//            cursor.close()
//            return result
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return contentURI.toString()
//        }
//    }

    fun getRealPathFromURI(contentURI: Uri, context: Context): String? {
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(contentURI, projection, null, null, null)

            if (cursor != null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    val path = cursor.getString(columnIndex)
                    cursor.close()
                    return path
                }
                cursor.close()
            }

            return if (contentURI.scheme == "file") {
                contentURI.path // Return file path directly for file scheme
            } else {
                null // Could not retrieve path
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    @JvmStatic
    fun resizeBitmap(bit: Bitmap?, width: Int, height: Int): Bitmap? {
        if (bit == null) {
            return null
        }
        val wr = width.toFloat()
        val hr = height.toFloat()
        var wd = bit.width.toFloat()
        var he = bit.height.toFloat()
        val rat1 = wd / he
        val rat2 = he / wd
        if (wd > wr) {
            if (wr * rat2 > hr) {
                he = hr
                wd = he * rat1
            } else {
                wd = wr
                he = wd * rat2
            }
        } else if (he > hr) {
            he = hr
            wd = he * rat1
            if (wd > wr) {
                wd = wr
                he = wd * rat2
            }
        } else if (rat1 > 0.75f) {
            if (wr * rat2 > hr) {
                he = hr
                wd = he * rat1
            } else {
                wd = wr
                he = wd * rat2
            }
        } else if (rat2 > 1.5f) {
            he = hr
            wd = he * rat1
            if (wd > wr) {
                wd = wr
                he = wd * rat2
            }
        } else if (wr * rat2 > hr) {
            he = hr
            wd = he * rat1
        } else {
            wd = wr
            he = wd * rat2
        }
        return Bitmap.createScaledBitmap(bit, wd.toInt(), he.toInt(), false)
    }

    @JvmStatic
    fun dpToPx(c: Context, dp: Int): Int {
        val f = dp.toFloat()
        c.resources
        return (f * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun pxToDp(context: Context, px: Float): Float {
        return px / ((context.resources.displayMetrics.densityDpi.toFloat()) / 160.0f)
    }

    fun bitmapmasking(original: Bitmap, mask: Bitmap?): Bitmap {
        val result = Bitmap.createBitmap(original.width, original.height, Bitmap.Config.ARGB_8888)
        val mCanvas = Canvas(result)
        val paint = Paint(1)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_OUT))
        mCanvas.drawBitmap(original, 0.0f, 0.0f, null)
        mCanvas.drawBitmap(mask!!, 0.0f, 0.0f, paint)
        paint.setXfermode(null)
        return result
    }

    @JvmStatic
    fun getThumbnail(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        var b = bitmap.copy(bitmap.config!!, true)
        val w = b!!.width
        val h = b.height
        b = if (h > w) {
            cropCenterBitmap(b, w, w)
        } else {
            cropCenterBitmap(b, h, h)
        }
        return Bitmap.createScaledBitmap(b!!, width, height, true)
    }

    fun cropCenterBitmap(src: Bitmap?, w: Int, h: Int): Bitmap? {
        if (src == null) {
            return null
        }
        val width = src.width
        val height = src.height
        if (width < w && height < h) {
            return src
        }
        var x = 0
        var y = 0
        if (width > w) {
            x = (width - w) / 2
        }
        if (height > h) {
            y = (height - h) / 2
        }
        var cw = w
        var ch = h
        if (w > width) {
            cw = width
        }
        if (h > height) {
            ch = height
        }
        return Bitmap.createBitmap(src, x, y, cw, ch)
    }

    fun mergelogo(bitmap: Bitmap, logo: Bitmap, screenSize: Float, largeSize: Float): Bitmap {
        var logo = logo
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config!!)
        val wr = bitmap.width.toFloat()
        val hr = bitmap.height.toFloat()
        var wd = logo.width.toFloat()
        var he = logo.height.toFloat()
        val rat1 = wd / he
        val rat2 = he / wd
        if (wd > wr) {
            wd = wr
            logo = Bitmap.createScaledBitmap(logo, wd.toInt(), (wd * rat2).toInt(), false)
        } else if (he > hr) {
            he = hr
            logo = Bitmap.createScaledBitmap(logo, (he * rat1).toInt(), he.toInt(), false)
        } else {
            logo = Bitmap.createScaledBitmap(
                logo,
                ((wd * largeSize) / screenSize).toInt(),
                ((he * largeSize) / screenSize).toInt(),
                false
            )
        }
        val canvas = Canvas(result)
        canvas.density = 640
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null)
        canvas.drawBitmap(
            logo,
            (bitmap.width - logo.width).toFloat(),
            (bitmap.height - logo.height).toFloat(),
            null
        )
        bitmap.recycle()
        logo.recycle()
        return result
    }

    fun CropBitmapTransparency(sourceBitmap: Bitmap): Bitmap? {
        var minX = sourceBitmap.width
        var minY = sourceBitmap.height
        var maxX = -1
        var maxY = -1
        for (y in 0 until sourceBitmap.height) {
            for (x in 0 until sourceBitmap.width) {
                if (((sourceBitmap.getPixel(x, y) shr 24) and 255) > 0) {
                    if (x < minX) {
                        minX = x
                    }
                    if (x > maxX) {
                        maxX = x
                    }
                    if (y < minY) {
                        minY = y
                    }
                    if (y > maxY) {
                        maxY = y
                    }
                }
            }
        }
        if (maxX < minX || maxY < minY) {
            return null
        }
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1)
    }

    fun decodeSampledBitmapFromResource(
        res: Resources?,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        var reqWidth = reqWidth
        var reqHeight = reqHeight
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)
        if (reqWidth <= 0) {
            reqWidth = 1
        }
        if (reqHeight <= 0) {
            reqHeight = 1
        }
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    @JvmStatic
    fun getTiledBitmap(ctx: Context, resId: Int, width: Int, height: Int, flag: Boolean): Bitmap {
        val rect = Rect(0, 0, width, height)
        val paint = Paint()
        val options = BitmapFactory.Options()
        options.inScaled = flag
        var t = BitmapFactory.decodeResource(ctx.resources, resId, options)
        if (flag) {
            t = Bitmap.createScaledBitmap(t!!, width / 4, height / 4, true)
        }
        paint.setShader(BitmapShader(t!!, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT))
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(b).drawRect(rect, paint)
        return b
    }

    fun getTiledBitmap(
        activity: Activity?,
        resId: String?,
        seek_tailys: SeekBar,
        width: Int,
        height: Int
    ): Bitmap {
        val rect = Rect(0, 0, width, height)
        val paint = Paint()
        val prog = seek_tailys.progress + 10
        val options = BitmapFactory.Options()
        options.inScaled = false
        val bytes = JniUtils.decryptResourceJNI(activity as Context?, resId)
        paint.setShader(
            BitmapShader(
                Bitmap.createScaledBitmap(
                    BitmapFactory.decodeByteArray(
                        bytes,
                        0,
                        bytes.size,
                        options
                    ), prog, prog, true
                ), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT
            )
        )
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(b).drawRect(rect, paint)
        return b
    }

    fun getColoredBitmap(color: Int, width: Int, height: Int): Bitmap {
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(result).drawColor(color)
        return result
    }

    fun getSpannableString(ctx: Context, ttf: Typeface?, stringId: Int): CharSequence {
        val spannableString = SpannableString(ctx.resources.getString(stringId))
        spannableString.setSpan(
            CustomTypefaceSpan(ttf),
            0,
            ctx.resources.getString(stringId).length,
            0
        )
        val builder = SpannableStringBuilder().append(spannableString)
        return builder.subSequence(0, builder.length)
    }

    fun getRotatedBitmap(bit: Bitmap, deg: Float): Bitmap {
        val result = Bitmap.createBitmap(bit.width, bit.height, Bitmap.Config.ARGB_8888)
        val mCanvas = Canvas(result)
        val matrix = Matrix()
        val px = (bit.width / 2).toFloat()
        val py = (bit.height / 2).toFloat()
        matrix.postTranslate(((-bit.width) / 2).toFloat(), ((-bit.height) / 2).toFloat())
        matrix.postRotate(deg)
        matrix.postTranslate(px, py)
        mCanvas.drawBitmap(bit, matrix, null)
        return result
    }

    @Throws(Exception::class)
    fun resampleImageAndSaveToNewLocation(pathInput: String?, pathOutput: String?) {
        resampleImage(pathInput, 800).compress(
            Bitmap.CompressFormat.PNG,
            100,
            FileOutputStream(pathOutput)
        )
    }
}