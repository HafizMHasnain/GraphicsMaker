package com.example.graphicsmaker.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.example.graphicsmaker.JniUtils
import com.example.graphicsmaker.R
import com.example.graphicsmaker.create.BitmapDataObject
import com.example.graphicsmaker.utility.ImageUtils
import com.example.graphicsmaker.utility.StorageConfiguration
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

object Constants {
    var posters: Array<String> = arrayOf("poster_1", "poster_1", "poster_1")
    var cards: Array<String> = arrayOf("card_1", "card_1", "card_1")
    var backgrounds_list: Array<String> = arrayOf(
        "b32",
        "b41",
        "b42",
        "b33",
        "b34",
        "b35",
        "b36",
        "b37",
        "b38",
        "b39",
        "b40",
        "b43",
        "b49",
        "b50",
        "b10",
        "b5",
        "b6",
        "b1",
        "b4",
        "b2",
        "b3",
        "b9",
        "b7",
        "b8",
        "b11",
        "b12",
        "b13",
        "b14",
        "b15",
        "b16",
        "b17",
        "b18",
        "b19",
        "b20",
        "b21",
        "b22",
        "b23",
        "b24",
        "b44",
        "b45",
        "b46",
        "b47",
        "b48",
        "b25",
        "b26",
        "b27",
        "b28",
        "b29",
        "b30",
        "b31",
        "b51",
        "b52",
        "b53",
        "b54",
        "b55",
        "b56",
        "b57",
        "b58",
        "b59",
        "b60"
    )
    var textures_list: Array<String> = arrayOf(
        "t20",
        "t30",
        "t18",
        "t29",
        "t31",
        "t32",
        "t33",
        "t34",
        "t35",
        "t36",
        "t37",
        "t38",
        "t39",
        "t40",
        "t41",
        "t42",
        "t43",
        "t44",
        "t10",
        "t1",
        "t6",
        "t2",
        "t3",
        "t5",
        "t4",
        "t9",
        "t7",
        "t8",
        "t11",
        "t12",
        "t13",
        "t14",
        "t15",
        "t16",
        "t17",
        "t27",
        "t28",
        "t22",
        "t23",
        "t24",
        "t21",
        "t25",
        "t26",
        "t19",
        "t45",
        "t46",
        "t47",
        "t48",
        "t49",
        "t50",
        "t51",
        "t52",
        "t53",
        "t54",
        "t55",
        "t56",
        "t57",
        "t58",
        "t59",
        "t60"
    )
    var shapes_list: Array<String> = arrayOf(
        "shape_3",
        "shape_4",
        "shape_1",
        "shape_2",
        "shape_5",
        "shape_6",
        "shape_7",
        "shape_8",
        "shape_9",
        "shape_10",
        "shape_13",
        "shape_14",
        "shape_11",
        "shape_12",
        "shape_15",
        "shape_16",
        "shape_17",
        "shape_18",
        "shape_19",
        "shape_20",
        "shape_21",
        "shape_22",
        "shape_23"
    )

    var badge_stkr_list: Array<String> = arrayOf("cam_13",)
    var bakery_stkr_list: Array<String> = arrayOf("cam_13",)
    var ribbon_stkr_list: Array<String> = arrayOf("cam_13",)
    var icon_stkr_list: Array<String> = arrayOf("cam_13",)
    var payment_stkr_list: Array<String> = arrayOf("cam_13",)
    var beauty_stkr_list: Array<String> = arrayOf("cam_13",)
    var bistro_stkr_list: Array<String> = arrayOf("cam_13",)
    var profession_stkr_list: Array<String> = arrayOf("cam_13",)
    var people_stkr_list: Array<String> = arrayOf("cam_13",)
    var christianity_stkr_list: Array<String> = arrayOf("cam_13",)
    var pets_stkr_list: Array<String> = arrayOf("cam_13",)
    var letters_stkr_list: Array<String> = arrayOf("cam_13",)
    var babymom_stkr_list: Array<String> = arrayOf("cam_13",)
    var fasion_stkr_list: Array<String> = arrayOf("cam_13",)
    var business_stkr_list: Array<String> = arrayOf("cam_13",)
    var threed_stkr_list: Array<String> = arrayOf("cam_13",)
    var camera_stkr_list: Array<String> = arrayOf(
        "cam_13",
        "cam_14",
        "cam_15",
        "cam_16",
        "cam_17",
        "cam_3",
        "cam_4",
        "cam_1",
        "cam_2",
        "cam_5",
        "cam_6",
        "cam_7",
        "cam_8",
        "cam_9",
        "cam_10",
        "cam_11",
        "cam_12",
        "cam_18",
        "cam_19",
        "cam_20",
        "cam_21"
    )
    var square_stkr_list: Array<String> = arrayOf(
        "squ_3",
        "squ_4",
        "squ_1",
        "squ_2",
        "squ_5",
        "squ_6",
        "squ_7",
        "squ_8",
        "squ_9",
        "squ_10",
        "squ_11",
        "squ_12"
    )
    var butterfly_stkr_list: Array<String> = arrayOf(
        "but_3",
        "but_4",
        "but_1",
        "but_2",
        "but_5",
        "but_6",
        "but_7",
        "but_8",
        "but_9",
        "but_10",
        "but_11",
        "but_12",
        "but_13",
        "but_14",
        "but_15"
    )
    var cars_stkr_list: Array<String> = arrayOf(
        "car_3",
        "car_4",
        "car_1",
        "car_2",
        "car_5",
        "car_6",
        "car_7",
        "car_8",
        "car_9",
        "car_10",
        "car_11",
        "car_12",
        "car_13",
        "car_14",
        "car_15"
    )
    var music_stkr_list: Array<String> = arrayOf(
        "mus_3",
        "mus_4",
        "mus_1",
        "mus_2",
        "mus_5",
        "mus_6",
        "mus_7",
        "mus_8",
        "mus_9",
        "mus_10",
        "mus_11",
        "mus_12"
    )
    var party_stkr_list: Array<String> = arrayOf(
        "par_3",
        "par_4",
        "par_1",
        "par_2",
        "par_5",
        "par_6",
        "par_7",
        "par_8",
        "par_9",
        "par_10",
        "par_11",
        "par_12",
        "par_13",
        "par_14",
        "par_15",
        "par_16",
        "par_17",
        "par_18",
        "par_19",
        "par_20",
        "par_21",
        "par_22",
        "par_23",
        "par_24",
        "par_25"
    )
    var ngo_stkr_list: Array<String> = arrayOf(
        "ngo_3",
        "ngo_4",
        "ngo_1",
        "ngo_2",
        "ngo_5",
        "ngo_6",
        "ngo_7",
        "ngo_8",
        "ngo_9",
        "ngo_10",
        "ngo_11",
        "ngo_12",
        "ngo_13",
        "ngo_14",
        "ngo_15",
        "ngo_16",
        "ngo_17",
        "ngo_18"
    )
    var festival_stkr_list: Array<String> = arrayOf(
        "fes_3",
        "fes_4",
        "fes_1",
        "fes_2",
        "fes_5",
        "fes_6",
        "fes_7",
        "fes_8",
        "fes_9",
        "fes_10",
        "fes_11",
        "fes_12",
        "fes_13",
        "fes_14",
        "fes_15",
        "fes_16",
        "fes_17",
        "fes_18",
        "fes_19",
        "fes_20",
        "fes_21",
        "fes_22",
        "fes_23",
        "fes_24",
        "fes_25",
        "fes_26",
        "fes_27",
        "fes_28",
        "fes_29",
        "fes_30",
        "fes_31"
    )
    var tattoo_stkr_list: Array<String> = arrayOf(
        "tato_3",
        "tato_4",
        "tato_1",
        "tato_2",
        "tato_5",
        "tato_6",
        "tato_7",
        "tato_8",
        "tato_9",
        "tato_10",
        "tato_11",
        "tato_12",
        "tato_13",
        "tato_14",
        "tato_15",
        "tato_16",
        "tato_17",
        "tato_18",
        "tato_19",
        "tato_20"
    )
    var flower_stkr_list: Array<String> = arrayOf(
        "flow_3",
        "flow_4",
        "flow_1",
        "flow_2",
        "flow_5",
        "flow_6",
        "flow_7",
        "flow_8",
        "flow_9",
        "flow_10",
        "flow_11",
        "flow_12",
        "flow_13",
        "flow_14",
        "flow_15"
    )
    var star_stkr_list: Array<String> = arrayOf(
        "star_3",
        "star_4",
        "star_1",
        "star_2",
        "star_5",
        "star_6",
        "star_7",
        "star_8",
        "star_9",
        "star_10",
        "star_11"
    )
    var video_stkr_list: Array<String> = arrayOf(
        "vid_8",
        "vid_9",
        "vid_10",
        "vid_11",
        "vid_12",
        "vid_13",
        "vid_14",
        "vid_15",
        "vid_3",
        "vid_4",
        "vid_1",
        "vid_2",
        "vid_5",
        "vid_6",
        "vid_7",
        "vid_16"
    )
    var heart_stkr_list: Array<String> = arrayOf(
        "hea_1",
        "hea_2",
        "hea_3",
        "hea_4",
        "hea_5",
        "hea_6",
        "hea_7",
        "hea_8",
        "hea_9",
        "hea_10",
        "hea_11",
        "hea_12",
        "hea_13",
        "hea_14",
        "hea_15",
        "hea_16",
        "hea_17",
        "hea_18",
        "hea_19",
        "hea_20",
        "hea_21",
        "hea_22"
    )
    var halloween_stkr_list: Array<String> = arrayOf(
        "hall_3",
        "hall_4",
        "hall_1",
        "hall_2",
        "hall_5",
        "hall_6",
        "hall_7",
        "hall_8",
        "hall_9",
        "hall_10",
        "hall_11",
        "hall_12",
        "hall_13",
        "hall_14",
        "hall_15",
        "hall_16",
        "hall_17",
        "hall_18",
        "hall_19",
        "hall_20"
    )
    var holiday_stkr_list: Array<String> = arrayOf(
        "hol_1",
        "hol_2",
        "hol_3",
        "hol_4",
        "hol_5",
        "hol_6",
        "hol_7",
        "hol_8",
        "hol_9",
        "hol_10",
        "hol_11",
        "hol_12",
        "hol_13",
        "hol_14",
        "hol_15",
        "hol_16",
        "hol_17",
        "hol_18",
        "hol_19",
        "hol_20",
        "hol_21",
        "hol_22",
        "hol_23",
        "hol_24",
        "hol_25"
    )
    var toys_stkr_list: Array<String> = arrayOf(
        "toy_3",
        "toy_4",
        "toy_1",
        "toy_2",
        "toy_5",
        "toy_6",
        "toy_7",
        "toy_8",
        "toy_9",
        "toy_10",
        "toy_11",
        "toy_12",
        "toy_13",
        "toy_14",
        "toy_15",
        "toy_16",
        "toy_17",
        "toy_18",
        "toy_19",
        "toy_20",
        "toy_21",
        "toy_22",
        "toy_23",
        "toy_24",
        "toy_25"
    )
    var animal_bird_stkr_list: Array<String> = arrayOf(
        "ani_3",
        "ani_4",
        "ani_1",
        "ani_2",
        "ani_5",
        "ani_6",
        "ani_7",
        "ani_8",
        "ani_9",
        "ani_10",
        "ani_11",
        "ani_12",
        "ani_13",
        "ani_14",
        "ani_15",
        "ani_16",
        "ani_17",
        "ani_18",
        "ani_19",
        "ani_20",
        "ani_21",
        "ani_22",
        "ani_23",
        "ani_24",
        "ani_25",
        "ani_26",
        "ani_27",
        "ani_28",
        "ani_29",
        "ani_30",
        "ani_31",
        "ani_32",
        "ani_33",
        "ani_34",
        "ani_35",
        "ani_36",
        "ani_37",
        "ani_38",
        "ani_39",
        "ani_40",
        "ani_41",
        "ani_42",
        "ani_43",
        "ani_44",
        "ani_45"
    )
    var text_stkr_list: Array<String> = arrayOf(
        "text_3",
        "text_4",
        "text_1",
        "text_2",
        "text_5",
        "text_6",
        "text_7",
        "text_8",
        "text_9",
        "text_10",
        "text_11",
        "text_12",
        "text_13",
        "text_14",
        "text_15",
        "text_16",
        "text_17",
        "text_18",
        "text_19",
        "text_20",
        "text_21",
        "text_22",
        "text_23",
        "text_24"
    )
    var social_stkr_list: Array<String> = arrayOf(
        "soc_3",
        "soc_4",
        "soc_1",
        "soc_2",
        "soc_5",
        "soc_6",
        "soc_7",
        "soc_8",
        "soc_9",
        "soc_10",
        "soc_11",
        "soc_12",
        "soc_13"
    )
    var leaf_stkr_list: Array<String> = arrayOf(
        "lea_3",
        "lea_4",
        "lea_1",
        "lea_2",
        "lea_5",
        "lea_6",
        "lea_7",
        "lea_8",
        "lea_9",
        "lea_10",
        "lea_11",
        "lea_12",
        "lea_13",
        "lea_14",
        "lea_15",
        "lea_16",
        "lea_17",
        "lea_18",
        "lea_19",
        "lea_20",
        "lea_21",
        "lea_22"
    )
    var corporate_stkr_list: Array<String> = arrayOf(
        "corp_8",
        "corp_9",
        "corp_10",
        "corp_11",
        "corp_12",
        "corp_13",
        "corp_14",
        "corp_15",
        "corp_3",
        "corp_4",
        "corp_1",
        "corp_2",
        "corp_5",
        "corp_6",
        "corp_7",
        "corp_16",
        "corp_17",
        "corp_18",
        "corp_19",
        "corp_20",
        "des_2"
    )
    var property_stkr_list: Array<String> = arrayOf(
        "pro_3",
        "pro_4",
        "pro_1",
        "pro_2",
        "pro_5",
        "pro_6",
        "pro_7",
        "pro_8",
        "pro_9",
        "pro_10",
        "pro_11",
        "pro_12",
        "pro_13",
        "pro_14",
        "pro_15",
        "pro_16",
        "pro_17",
        "pro_18",
        "pro_19",
        "pro_20",
        "pro_21",
        "pro_22",
        "pro_23",
        "pro_24",
        "pro_25",
        "pro_26",
        "pro_27",
        "pro_28",
        "pro_29"
    )
    var sport_stkr_list: Array<String> = arrayOf(
        "sport_3",
        "sport_4",
        "sport_1",
        "sport_2",
        "sport_5",
        "sport_6",
        "sport_7",
        "sport_8",
        "sport_9",
        "sport_10",
        "sport_11",
        "sport_12",
        "sport_13",
        "sport_14",
        "sport_15",
        "sport_16",
        "sport_17",
        "sport_18"
    )
    var restaurant_cafe_stkr_list: Array<String> = arrayOf(
        "rest_3",
        "rest_4",
        "rest_1",
        "rest_2",
        "rest_5",
        "rest_6",
        "rest_7",
        "rest_8",
        "rest_9",
        "rest_10",
        "rest_11",
        "rest_12",
        "rest_13",
        "rest_14",
        "rest_15",
        "rest_16",
        "rest_17",
        "rest_18",
        "rest_19",
        "rest_20",
        "rest_21",
        "rest_22",
        "rest_23",
        "rest_24",
        "rest_25",
        "rest_26",
        "rest_27",
        "rest_28"
    )
    var circle_stkr_list: Array<String> = arrayOf(
        "cir_3",
        "cir_4",
        "cir_1",
        "cir_2",
        "cir_5",
        "cir_6",
        "cir_7",
        "cir_8",
        "cir_9",
        "cir_10",
        "cir_11",
        "cir_12",
        "cir_13",
        "cir_14",
        "cir_15",
        "cir_16",
        "cir_17",
        "cir_18",
        "cir_19",
        "cir_20",
        "cir_21",
        "cir_22",
        "cir_23",
        "cir_24",
        "cir_25",
        "cir_26",
        "cir_27"
    )
    var shape_stkr_list: Array<String> = arrayOf(
        "sh3",
        "sh4",
        "sh1",
        "sh2",
        "sh5",
        "sh6",
        "sh7",
        "sh8",
        "sh9",
        "sh10",
        "sh11",
        "sh14",
        "sh12",
        "sh13",
        "sh15",
        "sh16",
        "sh17",
        "sh18",
        "sh19",
        "sh20",
        "sh21",
        "sh22",
        "sh23",
        "sh24",
        "sh25",
        "sh26",
        "sh27",
        "sh28",
        "sh29",
        "sh30",
        "sh31",
        "sh32",
        "sh33",
        "sh34",
        "sh35",
        "sh36",
        "sh37",
        "sh38",
        "sh39",
        "sh40",
        "sh41",
        "sh42",
        "sh43",
        "sh44",
        "sh45",
        "sh46",
        "sh47",
        "sh48",
        "sh49",
        "sh50",
        "sh51",
        "sh52",
        "sh53",
        "sh54",
        "sh55",
        "sh56",
        "sh57",
        "sh58",
        "sh59",
        "sh60",
        "sh61",
        "sh62",
        "sh63",
        "sh64",
        "sh65",
        "sh66",
        "sh67",
        "sh68"
    )
    var overlayImg: Array<String> = arrayOf(
        "o3",
        "o4",
        "o1",
        "o2",
        "o5",
        "o6",
        "o7",
        "o8",
        "o9",
        "o10",
        "o11",
        "o12",
        "o13",
        "o14",
        "o26",
        "o27",
        "o28",
        "o29",
        "o30",
        "o15",
        "o16",
        "o17",
        "o18",
        "o19",
        "o20",
        "o21",
        "o22",
        "o23",
        "o24",
        "o25",
        "o31"
    )

    fun getFirstPageTypeface(activity: Activity): Typeface {
        return Typeface.createFromAsset(activity.assets, "OPENSANS_SEMIBOLD.TTF")
    }

    @JvmStatic
    fun getHeaderTypeface(activity: Activity): Typeface {
        return Typeface.createFromAsset(activity.assets, "SANSATION_BOLD.TTF")
    }

    @JvmStatic
    fun getTextTypeface(activity: Activity): Typeface {
        return Typeface.createFromAsset(activity.assets, "SANSATION_REGULAR.TTF")
    }

    fun getTextHeadingTypeface(activity: Activity): Typeface {
        return Typeface.createFromAsset(activity.assets, "akifont9.ttf")
    }

    @JvmStatic
    fun getAnimUp(activity: Activity?): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.slide_up)
    }

    @JvmStatic
    fun getAnimDown(activity: Activity?): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.slide_down)
    }

    fun guidelines_bitmap(context: Activity?, w: Int, h: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        val paint = Paint()
        paint.color = -1
        paint.strokeWidth = ImageUtils.dpToPx(context, 2).toFloat()
        paint.setPathEffect(DashPathEffect(floatArrayOf(5.0f, 5.0f), 1.0f))
        paint.style = Paint.Style.STROKE
        val paint1 = Paint()
        paint1.color = ViewCompat.MEASURED_STATE_MASK
        paint1.strokeWidth = ImageUtils.dpToPx(context, 2).toFloat()
        paint1.setPathEffect(DashPathEffect(floatArrayOf(5.0f, 5.0f), 1.0f))
        paint1.style = Paint.Style.STROKE
        c.drawLine((w / 4).toFloat(), 0.0f, (w / 4).toFloat(), h.toFloat(), paint)
        c.drawLine((w / 2).toFloat(), 0.0f, (w / 2).toFloat(), h.toFloat(), paint)
        c.drawLine(((w * 3) / 4).toFloat(), 0.0f, ((w * 3) / 4).toFloat(), h.toFloat(), paint)
        c.drawLine(0.0f, (h / 4).toFloat(), w.toFloat(), (h / 4).toFloat(), paint)
        c.drawLine(0.0f, (h / 2).toFloat(), w.toFloat(), (h / 2).toFloat(), paint)
        c.drawLine(0.0f, ((h * 3) / 4).toFloat(), w.toFloat(), ((h * 3) / 4).toFloat(), paint)
        c.drawLine(((w / 4) + 2).toFloat(), 0.0f, ((w / 4) + 2).toFloat(), h.toFloat(), paint1)
        c.drawLine(((w / 2) + 2).toFloat(), 0.0f, ((w / 2) + 2).toFloat(), h.toFloat(), paint1)
        c.drawLine(
            (((w * 3) / 4) + 2).toFloat(),
            0.0f,
            (((w * 3) / 4) + 2).toFloat(),
            h.toFloat(),
            paint1
        )
        c.drawLine(0.0f, ((h / 4) + 2).toFloat(), w.toFloat(), ((h / 4) + 2).toFloat(), paint1)
        c.drawLine(0.0f, ((h / 2) + 2).toFloat(), w.toFloat(), ((h / 2) + 2).toFloat(), paint1)
        c.drawLine(
            0.0f,
            (((h * 3) / 4) + 2).toFloat(),
            w.toFloat(),
            (((h * 3) / 4) + 2).toFloat(),
            paint1
        )
        return bitmap
    }

    fun saveBitmapObject1(bitmap: Bitmap): String {
        var temp_path = ""
        val myDir = StorageConfiguration.getDesignPath()
        myDir.mkdirs()
        val file1 = File(myDir, "raw1-" + System.currentTimeMillis() + ".png")
        temp_path = file1.absolutePath
        if (file1.exists()) {
            file1.delete()
        }
        try {
            val ostream = FileOutputStream(file1)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream)
            ostream.close()
            return temp_path
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("testing", "Exception" + e.message)
            return ""
        }
    }

    fun saveBitmapObject(activity: Activity, btmSimple: Bitmap, pathIs: String?): Boolean {
        val bitmap = btmSimple.copy(btmSimple.config!!, true)
        val pictureFile = File(pathIs)
        try {
            if (!pictureFile.exists()) {
                pictureFile.createNewFile()
            }
            val ostream = FileOutputStream(pictureFile)
            val saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream)
            ostream.flush()
            ostream.close()
            bitmap.recycle()
            activity.sendBroadcast(
                Intent(
                    "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
                    Uri.fromFile(pictureFile)
                )
            )
            return saved
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("testing", "Exception" + e.message)
            return false
        }
    }

    fun saveBitmapObject(activity: Activity, bitmap: Bitmap): String? {
        val myDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            ".LogoMaker Stickers/template thumbnail"
        )
        myDir.mkdirs()
        val file = File(myDir, "IMG_" + System.currentTimeMillis() + ".png")
        if (file.exists()) {
            file.delete()
        }
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
            val out = FileOutputStream(file)
            val os = ObjectOutputStream(out)
            val bdo = BitmapDataObject()
            bdo.imageByteArray = getBytesFromBitmap(bitmap)
            os.writeObject(bdo)
            os.close()
            out.close()
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("MAINACTIVITY", "Exception" + e.message)
            Toast.makeText(
                activity,
                activity.resources.getString(R.string.save_err),
                Toast.LENGTH_SHORT
            ).show()
            return null
        }
    }

    fun saveBitmapObjectSticker(bitmap: Bitmap): String {
        var temp_path = ""
        val myDir = StorageConfiguration.getDesignPath()
        val file1 = File(myDir, "IMG_" + System.currentTimeMillis() + ".png")
        temp_path = file1.absolutePath
        if (file1.exists()) {
            file1.delete()
        }
        try {
            val ostream = FileOutputStream(file1)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream)
            ostream.close()
            return temp_path
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("testing", "Exception" + e.message)
            return ""
        }
    }

    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    @JvmStatic
    fun freeMemory() {
        System.runFinalization()
        Runtime.getRuntime().gc()
        System.gc()
    }

    fun getTiledBitmap(
        activity: Activity?,
        resId: String?,
        imgBtmap: Bitmap,
        seek_tailys: SeekBar
    ): Bitmap {
        val rect = Rect(0, 0, imgBtmap.width, imgBtmap.height)
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
        val b = Bitmap.createBitmap(imgBtmap.width, imgBtmap.height, Bitmap.Config.ARGB_8888)
        Canvas(b).drawRect(rect, paint)
        return b
    }

    fun drawableToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap {
        val mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mutableBitmap)
        drawable.setBounds(0, 0, widthPixels, heightPixels)
        drawable.draw(canvas)
        return mutableBitmap
    }
}
