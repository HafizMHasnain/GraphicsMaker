package com.example.graphicsmaker.create

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.util.Log
import com.example.graphicsmaker.msl.demo.view.ComponentInfo
import com.example.graphicsmaker.msl.textmodule.TextInfo
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class DatabaseHandler(var mContext: Context) : SQLiteOpenHelper(
    mContext, DATABASE_PATH, null, 1) {
    external fun createTemplates(
        str: String?,
        str2: String?,
        str3: String?,
        str4: String?,
        str5: String?,
        str6: String?
    ): Int

    override fun onCreate(db: SQLiteDatabase) {
        if (createDBStructure(db)) {
            try {
                val dbFilePath = copyShippedDataBase()
                if (dbFilePath != null) {
                    val didSucceedCopying = CopyTempTablesToLocalDB(db, dbFilePath)
                    db_delete(dbFilePath)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e2: Exception) {
                e2.printStackTrace()
            } catch (e3: Error) {
                e3.printStackTrace()
            }
        }
        Log.i("testing", "Database Created")
    }


    fun correctResIds(db: SQLiteDatabase): Boolean {
        val cursor = db.rawQuery(
            "SELECT TEMPLATE_ID, FRAME_NAME FROM TEMPLATES WHERE TYPE = 'USER' AND PROFILE_TYPE IN ('Background','Texture')",
            null
        )
        if (cursor == null || cursor.count <= 0 || !cursor.moveToFirst()) {
            return false
        }
        do {
            val Template_id = cursor.getInt(0)
            val Res_id = cursor.getString(1)
            val oldNumber = Res_id.replace("[^0-9]".toRegex(), "")
            try {
                val newNumberInt = oldNumber.toInt() + 1
                if (newNumberInt > 0) {
                    db.execSQL(
                        "UPDATE TEMPLATES SET FRAME_NAME ='" + Res_id.replace(
                            oldNumber.toRegex(),
                            newNumberInt.toString()
                        ) + "'  WHERE TEMPLATE_ID = " + Template_id + " ;"
                    )
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        } while (cursor.moveToNext())
        cursor.close()
        return false
    }

    @Throws(IOException::class)
    private fun copyShippedDataBase(): String? {
        try {
            val myInput = mContext.assets.open("LOGOMAKER_DB.db")
            val path = mContext.getDatabasePath("Temp.db").path
            val dbFile = File(path)
            if (!dbFile.exists()) {
                dbFile.createNewFile()
            }
            val myOutput: OutputStream = FileOutputStream(dbFile)
            val buffer = ByteArray(2048)
            while (true) {
                val length = myInput.read(buffer)
                if (length > 0) {
                    myOutput.write(buffer, 0, length)
                } else {
                    myOutput.flush()
                    myOutput.close()
                    myInput.close()
                    return path
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun createDuplicateTemplate(templateId: Int): Int {
        var newTempId = 0
        val db = writableDatabase
        try {
            val cursorTemplate =
                db.rawQuery("SELECT  * FROM  TEMPLATES WHERE TEMPLATE_ID = $templateId", null)
            if (cursorTemplate != null && cursorTemplate.count > 0 && cursorTemplate.moveToFirst()) {
                do {
                    val values = TemplateInfo()
                    values.templatE_ID = cursorTemplate.getInt(0)
                    values.thumB_URI = cursorTemplate.getString(1)
                    values.framE_NAME = cursorTemplate.getString(2)
                    values.ratio = cursorTemplate.getString(3)
                    values.profilE_TYPE = cursorTemplate.getString(4)
                    values.seeK_VALUE = cursorTemplate.getString(5)
                    values.type = "USER"
                    values.temP_PATH = cursorTemplate.getString(7)
                    values.tempcolor = cursorTemplate.getString(8)
                    values.overlaY_NAME = cursorTemplate.getString(9)
                    values.overlaY_OPACITY = cursorTemplate.getInt(10)
                    values.overlaY_BLUR = cursorTemplate.getInt(11)
                    values.shaP_NAME = cursorTemplate.getString(12)
                    val parts = values.ratio.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    newTempId = insertTemplateRowData(values, db).toInt()
                    val cursorComponent = db.rawQuery(
                        "SELECT * FROM COMPONENT_INFO WHERE TEMPLATE_ID='$templateId'",
                        null
                    )
                    var cursorText: Cursor
                    if (cursorComponent == null || cursorComponent.count <= 0 || !cursorComponent.moveToFirst()) {
                        cursorText = db.rawQuery(
                            "SELECT * FROM TEXT_INFO WHERE TEMPLATE_ID='$templateId'",
                            null
                        )
                        if (cursorText == null && cursorText.count > 0 && cursorText.moveToFirst()) {
                            do {
                                val valuesTextInfo = TextInfo()
                                valuesTextInfo.texT_ID = cursorText.getInt(0)
                                valuesTextInfo.templatE_ID = cursorText.getInt(1)
                                valuesTextInfo.text = cursorText.getString(2)
                                valuesTextInfo.fonT_NAME = cursorText.getString(3)
                                valuesTextInfo.texT_COLOR = cursorText.getInt(4)
                                valuesTextInfo.texT_ALPHA = cursorText.getInt(5)
                                valuesTextInfo.shadoW_COLOR = cursorText.getInt(6)
                                valuesTextInfo.shadoW_PROG = cursorText.getInt(7)
                                valuesTextInfo.bG_DRAWABLE = cursorText.getString(8)
                                valuesTextInfo.bG_COLOR = cursorText.getInt(9)
                                valuesTextInfo.bG_ALPHA = cursorText.getInt(10)
                                valuesTextInfo.poS_X = cursorText.getFloat(11)
                                valuesTextInfo.poS_Y = cursorText.getFloat(12)
                                valuesTextInfo.width = cursorText.getInt(13)
                                valuesTextInfo.height = cursorText.getInt(14)
                                valuesTextInfo.rotation = cursorText.getFloat(15)
                                valuesTextInfo.type = cursorText.getString(16)
                                valuesTextInfo.order = cursorText.getInt(17)
                                valuesTextInfo.xRotateProg = cursorText.getInt(18)
                                valuesTextInfo.yRotateProg = cursorText.getInt(19)
                                valuesTextInfo.zRotateProg = cursorText.getInt(20)
                                valuesTextInfo.curveRotateProg = cursorText.getInt(21)
                                valuesTextInfo.fielD_ONE = cursorText.getInt(22)
                                valuesTextInfo.fielD_TWO = cursorText.getString(23)
                                valuesTextInfo.fielD_THREE = cursorText.getString(24)
                                valuesTextInfo.fielD_FOUR = cursorText.getString(25)
                                valuesTextInfo.texT_GRAVITY = cursorText.getString(26)
                                valuesTextInfo.templatE_ID = newTempId
                                insertTextRowData(valuesTextInfo, db)
                            } while (cursorText.moveToNext())
                        }
                    } else {
                        do {
                            val valuesComponent = ComponentInfo()
                            valuesComponent.comP_ID = cursorComponent.getInt(0)
                            valuesComponent.poS_X = cursorComponent.getFloat(2)
                            valuesComponent.poS_Y = cursorComponent.getFloat(3)
                            valuesComponent.width = cursorComponent.getInt(4)
                            valuesComponent.height = cursorComponent.getInt(5)
                            valuesComponent.rotation = cursorComponent.getFloat(6)
                            valuesComponent.y_ROTATION = cursorComponent.getFloat(7)
                            valuesComponent.reS_ID = cursorComponent.getString(8)
                            valuesComponent.type = cursorComponent.getString(9)
                            valuesComponent.order = cursorComponent.getInt(10)
                            valuesComponent.stC_COLOR = cursorComponent.getInt(11)
                            valuesComponent.stC_OPACITY = cursorComponent.getInt(12)
                            valuesComponent.xRotateProg = cursorComponent.getInt(13)
                            valuesComponent.yRotateProg = cursorComponent.getInt(14)
                            valuesComponent.zRotateProg = cursorComponent.getInt(15)
                            valuesComponent.scaleProg = cursorComponent.getInt(16)
                            valuesComponent.stkR_PATH = cursorComponent.getString(17)
                            valuesComponent.colortype = cursorComponent.getString(18)
                            valuesComponent.stC_HUE = cursorComponent.getInt(19)
                            valuesComponent.fielD_ONE = cursorComponent.getInt(20)
                            val margin = cursorComponent.getString(21)
                            valuesComponent.fielD_TWO = cursorComponent.getString(21)
                            valuesComponent.fielD_TWO = margin
                            valuesComponent.fielD_THREE = cursorComponent.getString(22)
                            valuesComponent.fielD_FOUR = cursorComponent.getString(23)
                            valuesComponent.templatE_ID = newTempId
                            insertComponentInfoRowData(valuesComponent, db)
                        } while (cursorComponent.moveToNext())
                        cursorText = db.rawQuery(
                            "SELECT * FROM TEXT_INFO WHERE TEMPLATE_ID='$templateId'",
                            null
                        )
                        if (cursorText == null) {

                        }
                    }
                } while (cursorTemplate.moveToNext())
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        db.close()
        return newTempId
    }

    private fun CopyTempTablesToLocalDB(db: SQLiteDatabase, dbFilePath: String): Boolean {
        val dimension = DisplayMetrics()
        (mContext as Activity).windowManager.defaultDisplay.getMetrics(dimension)
        val templateRatio = (dimension.widthPixels.toFloat()) / 1532.0f
        try {
            db.setTransactionSuccessful()
            db.endTransaction()
            db.execSQL("ATTACH DATABASE '$dbFilePath' AS TEMP1")
            db.beginTransaction()
            try {
                db.execSQL("DELETE FROM STICKERMASTER ")
                db.execSQL("DELETE FROM INDUSTRY_STICKERS  ")
                db.execSQL("DELETE FROM FONTSMASTER ")
                db.execSQL("INSERT INTO STICKERMASTER SELECT * FROM TEMP1.STICKERMASTER;")
                db.execSQL("INSERT INTO INDUSTRY_STICKERS  SELECT * FROM TEMP1.INDUSTRY_STICKERS ;")
                db.execSQL("INSERT INTO FONTSMASTER SELECT * FROM TEMP1.FONTSMASTER;")
                db.execSQL("DELETE FROM COMPONENT_INFO WHERE TEMPLATE_ID IN (SELECT TEMPLATE_ID FROM TEMPLATES WHERE TYPE != 'USER')")
                db.execSQL("DELETE FROM TEXT_INFO WHERE TEMPLATE_ID IN (SELECT TEMPLATE_ID FROM TEMPLATES WHERE TYPE != 'USER')")
                db.execSQL("DELETE FROM TEMPLATES WHERE TYPE != 'USER'")
                val cursorTemplate = db.rawQuery(
                    "SELECT  * FROM TEMP1.TEMPLATES WHERE TYPE!='USER' ORDER BY TEMPLATE_ID DESC;",
                    null
                )
                if (cursorTemplate == null || cursorTemplate.count <= 0 || !cursorTemplate.moveToFirst()) {
                    db.setTransactionSuccessful()
                    db.endTransaction()
                    db.execSQL("DETACH DATABASE TEMP1;")
                    db.beginTransaction()
                    return false
                }
                do {
                    val values = TemplateInfo()
                    values.templatE_ID = cursorTemplate.getInt(0)
                    values.thumB_URI = cursorTemplate.getString(1)
                    values.framE_NAME = cursorTemplate.getString(2)
                    values.ratio = cursorTemplate.getString(3)
                    values.profilE_TYPE = cursorTemplate.getString(4)
                    values.seeK_VALUE = cursorTemplate.getString(5)
                    values.type = cursorTemplate.getString(6)
                    values.temP_PATH = cursorTemplate.getString(7)
                    values.tempcolor = cursorTemplate.getString(8)
                    values.overlaY_NAME = cursorTemplate.getString(9)
                    values.overlaY_OPACITY = cursorTemplate.getInt(10)
                    values.overlaY_BLUR = cursorTemplate.getInt(11)
                    values.shaP_NAME = cursorTemplate.getString(12)
                    val parts = values.ratio.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    val aspectRatioWidth = parts[0].toInt()
                    val aspectRatioHeight = parts[1].toInt()
                    val newTempId = insertTemplateRowData(values, db)
                    val cursorComponent = db.rawQuery(
                        "SELECT * FROM TEMP1.COMPONENT_INFO WHERE TEMPLATE_ID=" + values.templatE_ID,
                        null
                    )
                    var cursorText: Cursor
                    var pos_y: Float
                    var width: Float
                    var height: Float
                    var margin: String
                    var partsMargin: Array<String>
                    var leftMargin: Int
                    if (cursorComponent == null || cursorComponent.count <= 0 || !cursorComponent.moveToFirst()) {
                        cursorText = db.rawQuery(
                            "SELECT * FROM TEMP1.TEXT_INFO WHERE TEMPLATE_ID='" + values.templatE_ID + "'",
                            null
                        )
                        if (cursorText == null && cursorText.count > 0 && cursorText.moveToFirst()) {
                            do {
                                val valuesTextInfo = TextInfo()
                                valuesTextInfo.texT_ID = cursorText.getInt(0)
                                valuesTextInfo.templatE_ID = cursorText.getInt(1)
                                valuesTextInfo.text = cursorText.getString(2)
                                valuesTextInfo.fonT_NAME = cursorText.getString(3)
                                valuesTextInfo.texT_COLOR = cursorText.getInt(4)
                                valuesTextInfo.texT_ALPHA = cursorText.getInt(5)
                                valuesTextInfo.shadoW_COLOR = cursorText.getInt(6)
                                valuesTextInfo.shadoW_PROG = cursorText.getInt(7)
                                valuesTextInfo.bG_DRAWABLE = cursorText.getString(8)
                                valuesTextInfo.bG_COLOR = cursorText.getInt(9)
                                valuesTextInfo.bG_ALPHA = cursorText.getInt(10)
                                pos_y = cursorText.getFloat(12) * templateRatio
                                width = cursorText.getFloat(13) * templateRatio
                                height = cursorText.getFloat(14) * templateRatio
                                valuesTextInfo.poS_X = cursorText.getFloat(11) * templateRatio
                                valuesTextInfo.poS_Y = pos_y
                                valuesTextInfo.width = width.toInt()
                                valuesTextInfo.height = height.toInt()
                                valuesTextInfo.rotation = cursorText.getFloat(15)
                                valuesTextInfo.type = cursorText.getString(16)
                                valuesTextInfo.order = cursorText.getInt(17)
                                valuesTextInfo.xRotateProg = cursorText.getInt(18)
                                valuesTextInfo.yRotateProg = cursorText.getInt(19)
                                valuesTextInfo.zRotateProg = cursorText.getInt(20)
                                valuesTextInfo.curveRotateProg = cursorText.getInt(21)
                                valuesTextInfo.fielD_ONE = cursorText.getInt(22)
                                margin = cursorText.getString(23)
                                if (margin != "") {
                                    partsMargin =
                                        margin.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                            .toTypedArray()
                                    leftMargin = partsMargin[0].toInt()
                                    margin = ((leftMargin.toFloat()) * templateRatio).toInt()
                                        .toString() + "," + ((partsMargin[1].toInt()
                                        .toFloat()) * templateRatio).toInt().toString()
                                }
                                valuesTextInfo.fielD_TWO = margin
                                valuesTextInfo.fielD_THREE = cursorText.getString(24)
                                valuesTextInfo.fielD_FOUR = cursorText.getString(25)
                                valuesTextInfo.texT_GRAVITY = cursorText.getString(26)
                                valuesTextInfo.templatE_ID = newTempId.toInt()
                                insertTextRowData(valuesTextInfo, db)
                            } while (cursorText.moveToNext())
                        }
                    } else {
                        do {
                            val valuesComponent = ComponentInfo()
                            valuesComponent.comP_ID = cursorComponent.getInt(0)
                            pos_y = cursorComponent.getFloat(3) * templateRatio
                            width = cursorComponent.getFloat(4) * templateRatio
                            height = cursorComponent.getFloat(5) * templateRatio
                            valuesComponent.poS_X = cursorComponent.getFloat(2) * templateRatio
                            valuesComponent.poS_Y = pos_y
                            valuesComponent.width = width.toInt()
                            valuesComponent.height = height.toInt()
                            valuesComponent.rotation = cursorComponent.getFloat(6)
                            valuesComponent.y_ROTATION = cursorComponent.getFloat(7)
                            valuesComponent.reS_ID = cursorComponent.getString(8)
                            valuesComponent.type = cursorComponent.getString(9)
                            valuesComponent.order = cursorComponent.getInt(10)
                            valuesComponent.stC_COLOR = cursorComponent.getInt(11)
                            valuesComponent.stC_OPACITY = cursorComponent.getInt(12)
                            valuesComponent.xRotateProg = cursorComponent.getInt(13)
                            valuesComponent.yRotateProg = cursorComponent.getInt(14)
                            valuesComponent.zRotateProg = cursorComponent.getInt(15)
                            valuesComponent.scaleProg = cursorComponent.getInt(16)
                            valuesComponent.stkR_PATH = cursorComponent.getString(17)
                            valuesComponent.colortype = cursorComponent.getString(18)
                            valuesComponent.stC_HUE = cursorComponent.getInt(19)
                            valuesComponent.fielD_ONE = cursorComponent.getInt(20)
                            margin = cursorComponent.getString(21)
                            if (margin != "") {
                                partsMargin =
                                    margin.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()
                                leftMargin = partsMargin[0].toInt()
                                margin = ((leftMargin.toFloat()) * templateRatio).toInt()
                                    .toString() + "," + ((partsMargin[1].toInt()
                                    .toFloat()) * templateRatio).toInt().toString()
                            }
                            valuesComponent.fielD_TWO = margin
                            valuesComponent.fielD_THREE = cursorComponent.getString(22)
                            valuesComponent.fielD_FOUR = cursorComponent.getString(23)
                            valuesComponent.templatE_ID = newTempId.toInt()
                            insertComponentInfoRowData(valuesComponent, db)
                        } while (cursorComponent.moveToNext())
                        cursorText = db.rawQuery(
                            "SELECT * FROM TEMP1.TEXT_INFO WHERE TEMPLATE_ID='" + values.templatE_ID + "'",
                            null
                        )
                        if (cursorText == null) {
                        }
                    }
                } while (cursorTemplate.moveToNext())
                db.setTransactionSuccessful()
                db.endTransaction()
                db.execSQL("DETACH DATABASE TEMP1;")
                db.beginTransaction()
                return false
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        } catch (e2: SQLException) {
            Log.e("RK Error", "Couldnt Attach DB$e2")
            e2.printStackTrace()
        }
        return false
    }

    private fun CopyFontMasterTablesToLocalDB(db: SQLiteDatabase, dbFilePath: String): Boolean {
        try {
            db.setTransactionSuccessful()
            db.endTransaction()
            db.execSQL("ATTACH DATABASE '$dbFilePath' AS TEMP1")
            db.beginTransaction()
            try {
                db.execSQL("DELETE FROM FONTSMASTER ")
                db.execSQL("INSERT INTO FONTSMASTER SELECT * FROM TEMP1.FONTSMASTER;")
                db.setTransactionSuccessful()
                db.endTransaction()
                db.execSQL("DETACH DATABASE TEMP1;")
                db.beginTransaction()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        } catch (e2: SQLException) {
            Log.e("RK Error", "Couldnt Attach DB$e2")
            e2.printStackTrace()
        }
        return false
    }

    fun db_delete(dbFilePath: String?) {
        val file = File(dbFilePath)
        if (file.exists()) {
            file.delete()
            println("delete database file.")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    private fun createDBStructure(db: SQLiteDatabase): Boolean {
        db.execSQL(CREATE_TABLE_TEMPLATES)
        db.execSQL(CREATE_TABLE_TEXT_INFO)
        db.execSQL(CREATE_TABLE_COMPONENT_INFO)
        db.execSQL("CREATE TABLE IF NOT EXISTS STICKERMASTER ( id INTEGER, resID TEXT, category TEXT, type TEXT, seq INTEGER,  UseAsBoundary TEXT, InitialsDesign TEXT, UseAsBadge TEXT, FutureCat4 TEXT, FutureCat5 TEXT, PrimaryColor TEXT, SecondaryColor TEXT, BoundingRect TEXT, FutureText2 TEXT, FutureText3 TEXT, FutureText4 TEXT, PRIMARY KEY(id));")
        db.execSQL("CREATE TABLE IF NOT EXISTS INDUSTRY_STICKERS ( id INTEGER, Industry TEXT, resID TEXT, Field1 TEXT, Field2 TEXT, Field3 TEXT, Field4 TEXT, PRIMARY KEY(id) );")
        db.execSQL("CREATE TABLE  IF NOT EXISTS FONTSMASTER  (\n  id  INTEGER,\n  FontNameInRES  TEXT,\n  FontName  TEXT,\n  Seq  INTEGER,\n  Type  INTEGER,\n  Cat1  TEXT,\n  Cat2  TEXT,\n  Cat3  TEXT,\n  Cat4  TEXT,\n  Cat5  TEXT,\n PRIMARY KEY( id )\n);")
        return true
    }

    private fun updateExistingDB(db: SQLiteDatabase, oldVersion: Int, newVersion: Int): Boolean {
        val createStickersSQLStatement =
            "CREATE TABLE IF NOT EXISTS STICKERMASTER ( id INTEGER, resID TEXT, category TEXT, type TEXT, seq INTEGER,  UseAsBoundary TEXT, InitialsDesign TEXT, UseAsBadge TEXT, FutureCat4 TEXT, FutureCat5 TEXT, PrimaryColor TEXT, SecondaryColor TEXT, BoundingRect TEXT, FutureText2 TEXT, FutureText3 TEXT, FutureText4 TEXT, PRIMARY KEY(id));"
        try {
            db.execSQL(createStickersSQLStatement)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS INDUSTRY_STICKERS ( id INTEGER, Industry TEXT, resID TEXT, Field1 TEXT, Field2 TEXT, Field3 TEXT, Field4 TEXT, PRIMARY KEY(id) );")
        } catch (e2: SQLException) {
            e2.printStackTrace()
        }
        try {
            db.execSQL(createStickersSQLStatement)
        } catch (e22: SQLException) {
            e22.printStackTrace()
        }
        try {
            db.execSQL("CREATE TABLE  IF NOT EXISTS FONTSMASTER  (  id  INTEGER,  FontNameInRES  TEXT,  FontName  TEXT,  Seq  INTEGER,  Type  INTEGER,  Cat1  TEXT,  Cat2  TEXT,  Cat3  TEXT,  Cat4  TEXT,  Cat5  TEXT, PRIMARY KEY( id ));")
        } catch (e222: SQLException) {
            e222.printStackTrace()
        }
        try {
            db.execSQL("ALTER TABLE TEXT_INFO RENAME TO TEXT_INFO_Old")
            db.execSQL(CREATE_TABLE_TEXT_INFO)
            db.execSQL("INSERT INTO TEXT_INFO ( TEXT_ID , TEMPLATE_ID, TEXT, FONT_NAME, TEXT_COLOR, TEXT_ALPHA, SHADOW_COLOR, SHADOW_PROG, BG_DRAWABLE, BG_COLOR, BG_ALPHA, POS_X, POS_Y, WIDHT, HEIGHT, ROTATION, TYPE, ORDER_, XROTATEPROG, YROTATEPROG, ZROTATEPROG, CURVEPROG, FIELD_ONE, FIELD_TWO, FIELD_THREE, FIELD_FOUR)\nSELECT  TEXT_ID , TEMPLATE_ID, TEXT, FONT_NAME, TEXT_COLOR, TEXT_ALPHA, SHADOW_COLOR, SHADOW_PROG, BG_DRAWABLE, BG_COLOR, BG_ALPHA, POS_X, POS_Y, WIDHT, HEIGHT, ROTATION, TYPE, ORDER_, XROTATEPROG, YROTATEPROG, ZROTATEPROG, CURVEPROG, FIELD_ONE, FIELD_TWO, FIELD_THREE, FIELD_FOUR \nFROM TEXT_INFO_Old")
            db.execSQL("DROP TABLE TEXT_INFO_Old")
        } catch (e2222: SQLException) {
            e2222.printStackTrace()
        }
        return true
    }

    fun insertTemplateRow(tInfo: TemplateInfo): Long {
        val db = writableDatabase
        val insert = insertTemplateRowData(tInfo, db)
        db.close()
        return insert
    }

    fun insertTemplateRowData(tInfo: TemplateInfo, db: SQLiteDatabase): Long {

        // Convert Bitmap to ByteArray
        val bitmap: Bitmap = tInfo.art_BG
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()

        val values = ContentValues()
        values.put(ART_TYPE,tInfo.art_Type)
        values.put(ART_BG,byteArray)
        values.put(THUMB_URI, tInfo.thumB_URI)
        values.put(FRAME_NAME, tInfo.framE_NAME)
        values.put(RATIO, tInfo.ratio)
        values.put(PROFILE_TYPE, tInfo.profilE_TYPE)
        values.put(SEEK_VALUE, tInfo.seeK_VALUE)
        values.put(TYPE, tInfo.type)
        values.put(TEMP_PATH, tInfo.temP_PATH)
        values.put(TEMP_COLOR, tInfo.tempcolor)
        values.put(OVERLAY_NAME, tInfo.overlaY_NAME)
        values.put(OVERLAY_OPACITY, tInfo.overlaY_OPACITY)
        values.put(OVERLAY_BLUR, tInfo.overlaY_BLUR)
        values.put(SHAP_NAME, tInfo.shaP_NAME)
        Log.i("testing", "Before insertion ")
        val insert = db.insert(TEMPLATES, null, values)
        Log.i("testing", "ID $insert")
        Log.i("testing", "Framepath " + tInfo.framE_NAME)
        Log.i("testing", "Thumb Path " + tInfo.thumB_URI)
        return insert
    }

    fun insertComponentInfoRow(cInfo: ComponentInfo) {
        val db = writableDatabase
        insertComponentInfoRowData(cInfo, db)
        db.close()
    }

    fun insertComponentInfoRowData(cInfo: ComponentInfo, db: SQLiteDatabase) {
        val values = ContentValues()
        values.put(TEMPLATE_ID, cInfo.templatE_ID)
        values.put(POS_X, cInfo.poS_X)
        values.put(POS_Y, cInfo.poS_Y)
        values.put(WIDHT, cInfo.width)
        values.put(HEIGHT, cInfo.height)
        values.put(ROTATION, cInfo.rotation)
        values.put(Y_ROTATION, cInfo.y_ROTATION)
        values.put(RES_ID, cInfo.reS_ID)
        values.put(TYPE, cInfo.type)
        values.put(ORDER, cInfo.order)
        values.put(STC_COLOR, cInfo.stC_COLOR)
        values.put(STC_OPACITY, cInfo.stC_OPACITY)
        values.put(XROTATEPROG, cInfo.xRotateProg)
        values.put(YROTATEPROG, cInfo.yRotateProg)
        values.put(ZROTATEPROG, cInfo.zRotateProg)
        values.put(STC_SCALE, cInfo.scaleProg)
        values.put(STKR_PATH, cInfo.stkR_PATH)
        values.put(COLORTYPE, cInfo.colortype)
        values.put(STC_HUE, cInfo.stC_HUE)
        values.put(FIELD_ONE, cInfo.fielD_ONE)
        values.put(FIELD_TWO, cInfo.fielD_TWO)
        values.put(FIELD_THREE, cInfo.fielD_THREE)
        values.put(FIELD_FOUR, cInfo.fielD_FOUR)
        Log.e("insert id", "" + db.insert(COMPONENT_INFO, null, values))
    }

    fun insertTextRow(tInfo: TextInfo) {
        val db = writableDatabase
        insertTextRowData(tInfo, db)
        db.close()
    }

    fun insertTextRowData(tInfo: TextInfo, db: SQLiteDatabase) {
        val values = ContentValues()
        values.put(TEMPLATE_ID, tInfo.templatE_ID)
        values.put(TEXT, tInfo.text)
        values.put(FONT_NAME, tInfo.fonT_NAME)
        values.put(TEXT_COLOR, tInfo.texT_COLOR)
        values.put(TEXT_ALPHA, tInfo.texT_ALPHA)
        values.put(SHADOW_COLOR, tInfo.shadoW_COLOR)
        values.put(SHADOW_PROG, tInfo.shadoW_PROG)
        values.put(BG_DRAWABLE, tInfo.bG_DRAWABLE)
        values.put(BG_COLOR, tInfo.bG_COLOR)
        values.put(BG_ALPHA, tInfo.bG_ALPHA)
        values.put(POS_X, tInfo.poS_X)
        values.put(POS_Y, tInfo.poS_Y)
        values.put(WIDHT, tInfo.width)
        values.put(HEIGHT, tInfo.height)
        values.put(ROTATION, tInfo.rotation)
        values.put(TYPE, tInfo.type)
        values.put(ORDER, tInfo.order)
        values.put(XROTATEPROG, tInfo.xRotateProg)
        values.put(YROTATEPROG, tInfo.yRotateProg)
        values.put(ZROTATEPROG, tInfo.zRotateProg)
        values.put(CURVEPROG, tInfo.curveRotateProg)
        values.put(FIELD_ONE, tInfo.fielD_ONE)
        values.put(FIELD_TWO, tInfo.fielD_TWO)
        values.put(FIELD_THREE, tInfo.fielD_THREE)
        values.put(FIELD_FOUR, tInfo.fielD_FOUR)
        values.put(TEXT_GRAVITY, tInfo.texT_GRAVITY)
        Log.i("testing", "Before TEXT insertion ")
        Log.i("testing", "TEXT ID " + db.insert(TEXT_INFO, null, values))
    }



    fun getTemplateListDes(type: String, sort: String): ArrayList<TemplateInfo?> {
        val templateList: ArrayList<TemplateInfo?> = ArrayList<TemplateInfo?>()
        val query = if (sort == "RANDOM") {
            "SELECT  * FROM TEMPLATES WHERE TYPE='$type' ORDER BY random();"
        } else {
            "SELECT  * FROM TEMPLATES WHERE TYPE='" + type + "'" + " ORDER BY " + TEMPLATE_ID + " DESC;"
        }
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor == null || cursor.count <= 0 || !cursor.moveToFirst()) {
            db.close()
            return templateList
        }
        do {

            val values = TemplateInfo()
            values.templatE_ID = cursor.getInt(0)
            values.art_Type = cursor.getString(1)
            val byteArray = cursor.getBlob(2)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            values.art_BG = bitmap
            values.thumB_URI = cursor.getString(3)
            values.framE_NAME = cursor.getString(4)
            values.ratio = cursor.getString(5)
            values.profilE_TYPE = cursor.getString(6)
            values.seeK_VALUE = cursor.getString(7)
            values.type = cursor.getString(8)
            values.temP_PATH = cursor.getString(9)
            values.tempcolor = cursor.getString(10)
            values.overlaY_NAME = cursor.getString(11)
            values.overlaY_OPACITY = cursor.getInt(12)
            values.overlaY_BLUR = cursor.getInt(13)
            values.shaP_NAME = cursor.getString(14)
            templateList.add(values)
        } while (cursor.moveToNext())
        db.close()
        return templateList
    }

    fun getTemplateByID(temp_id: Int): TemplateInfo {
        val template = TemplateInfo()
        val query = "SELECT  * FROM TEMPLATES WHERE TEMPLATE_ID = $temp_id;"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor == null || cursor.count <= 0 || !cursor.moveToFirst()) {
            db.close()
            return template
        }
        do {
            template.templatE_ID = cursor.getInt(0)
            template.art_Type = cursor.getString(1)
            val byteArray = cursor.getBlob(2)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            template.art_BG = bitmap
            template.thumB_URI = cursor.getString(3)
            template.framE_NAME = cursor.getString(4)
            template.ratio = cursor.getString(5)
            template.profilE_TYPE = cursor.getString(6)
            template.seeK_VALUE = cursor.getString(7)
            template.type = cursor.getString(8)
            template.temP_PATH = cursor.getString(9)
            template.tempcolor = cursor.getString(10)
            template.overlaY_NAME = cursor.getString(11)
            template.overlaY_OPACITY = cursor.getInt(12)
            template.overlaY_BLUR = cursor.getInt(13)
            template.shaP_NAME = cursor.getString(14)
        } while (cursor.moveToNext())
        db.close()
        return template
    }

    fun getComponentInfoList(templateID: Int, shape: String): ArrayList<ComponentInfo?> {
        val componentInfoList: ArrayList<ComponentInfo?> = ArrayList<ComponentInfo?>()
        val query =
            "SELECT * FROM COMPONENT_INFO WHERE TEMPLATE_ID='" + templateID + "' AND " + TYPE + " = '" + shape + "'"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor == null || cursor.count <= 0 || !cursor.moveToFirst()) {
            db.close()
            return componentInfoList
        }
        do {
            val values = ComponentInfo()
            values.comP_ID = cursor.getInt(0)
            values.templatE_ID = cursor.getInt(1)
            values.poS_X = cursor.getFloat(2)
            values.poS_Y = cursor.getFloat(3)
            values.width = cursor.getInt(4)
            values.height = cursor.getInt(5)
            values.rotation = cursor.getFloat(6)
            values.y_ROTATION = cursor.getFloat(7)
            values.reS_ID = cursor.getString(8)
            values.type = cursor.getString(9)
            values.order = cursor.getInt(10)
            values.stC_COLOR = cursor.getInt(11)
            values.stC_OPACITY = cursor.getInt(12)
            values.xRotateProg = cursor.getInt(13)
            values.yRotateProg = cursor.getInt(14)
            values.zRotateProg = cursor.getInt(15)
            values.scaleProg = cursor.getInt(16)
            values.stkR_PATH = cursor.getString(17)
            values.colortype = cursor.getString(18)
            values.stC_HUE = cursor.getInt(19)
            values.fielD_ONE = cursor.getInt(20)
            values.fielD_TWO = cursor.getString(21)
            values.fielD_THREE = cursor.getString(22)
            values.fielD_FOUR = cursor.getString(23)
            componentInfoList.add(values)
        } while (cursor.moveToNext())
        db.close()
        return componentInfoList
    }

    val listOfIndustries: ArrayList<String?>
        get() {
            val listOfIndustries: ArrayList<String?> = ArrayList<String?>()
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT DISTINCT INDUSTRY FROM INDUSTRY_STICKERS;", null)
            if (cursor == null || cursor.count <= 0 || !cursor.moveToFirst()) {
                db.close()
                return listOfIndustries
            }
            do {
                listOfIndustries.add(cursor.getString(0))
            } while (cursor.moveToNext())
            db.close()
            return listOfIndustries
        }

    fun createTemplates(
        companyName: String?,
        tagLine: String?,
        industry: String?,
        fontStyle: String?,
        Type: String?,
        context: Context?
    ): Int {
        var result = 0
        try {
            System.loadLibrary("sqliteXOXO")
            result = createTemplates(DATABASE_PATH, companyName, tagLine, industry, fontStyle, Type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun getTextInfoList(templateID: Int): ArrayList<TextInfo?> {
        val textInfoArrayList: ArrayList<TextInfo?> = ArrayList<TextInfo?>()
        val query = "SELECT * FROM TEXT_INFO WHERE TEMPLATE_ID='$templateID'"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor == null || cursor.count <= 0 || !cursor.moveToFirst()) {
            db.close()
            return textInfoArrayList
        }
        do {
            val values = TextInfo()
            values.texT_ID = cursor.getInt(0)
            values.templatE_ID = cursor.getInt(1)
            values.text = cursor.getString(2)
            values.fonT_NAME = cursor.getString(3)
            values.texT_COLOR = cursor.getInt(4)
            values.texT_ALPHA = cursor.getInt(5)
            values.shadoW_COLOR = cursor.getInt(6)
            values.shadoW_PROG = cursor.getInt(7)
            values.bG_DRAWABLE = cursor.getString(8)
            values.bG_COLOR = cursor.getInt(9)
            values.bG_ALPHA = cursor.getInt(10)
            values.poS_X = cursor.getFloat(11)
            values.poS_Y = cursor.getFloat(12)
            values.width = cursor.getInt(13)
            values.height = cursor.getInt(14)
            values.rotation = cursor.getFloat(15)
            values.type = cursor.getString(16)
            values.order = cursor.getInt(17)
            values.xRotateProg = cursor.getInt(18)
            values.yRotateProg = cursor.getInt(19)
            values.zRotateProg = cursor.getInt(20)
            values.curveRotateProg = cursor.getInt(21)
            values.fielD_ONE = cursor.getInt(22)
            values.fielD_TWO = cursor.getString(23)
            values.fielD_THREE = cursor.getString(24)
            values.fielD_FOUR = cursor.getString(25)
            values.texT_GRAVITY = cursor.getString(26)
            textInfoArrayList.add(values)
        } while (cursor.moveToNext())
        db.close()
        return textInfoArrayList
    }

    fun deleteTemplateInfo(templateID: Int): Boolean {
        try {
            val db = writableDatabase
            db.execSQL("DELETE FROM TEMPLATES WHERE TEMPLATE_ID='$templateID'")
            db.execSQL("DELETE FROM COMPONENT_INFO WHERE TEMPLATE_ID='$templateID'")
            db.execSQL("DELETE FROM TEXT_INFO WHERE TEMPLATE_ID='$templateID'")
            db.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun updateTemplateColor(
        StkrName: String,
        primaryColor: Int,
        secondryColor: Int,
        templateId: Int,
        primaryTextId: Int,
        secondaryTextId: Int
    ): Boolean {
        try {
            val db = writableDatabase
            db.execSQL("UPDATE STICKERMASTER SET PrimaryColor =$primaryColor,SecondaryColor =$secondryColor  WHERE resId = '$StkrName' ;")
            db.execSQL("UPDATE  Text_info set TEXT_COLOR = $primaryColor where Text_Id = $primaryTextId and Template_id =$templateId ;")
            db.execSQL("UPDATE  Text_info set TEXT_COLOR = $secondryColor where Text_Id = $secondaryTextId and Template_id =$templateId ;")
            db.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    companion object {
        private const val ART_TYPE  = "ART_TYPE"
        private const val ART_BG = "ART_BG"
        private const val BG_ALPHA = "BG_ALPHA"
        private const val BG_COLOR = "BG_COLOR"
        private const val BG_DRAWABLE = "BG_DRAWABLE"
        private const val COLORTYPE = "COLORTYPE"
        private const val COMPONENT_INFO = "COMPONENT_INFO"
        private const val COMP_ID = "COMP_ID"
        private const val CREATE_TABLE_COMPONENT_INFO =
            "CREATE TABLE IF NOT EXISTS COMPONENT_INFO(COMP_ID INTEGER PRIMARY KEY,TEMPLATE_ID TEXT,POS_X TEXT,POS_Y TEXT,WIDHT TEXT,HEIGHT TEXT,ROTATION TEXT,Y_ROTATION TEXT,RES_ID TEXT,TYPE TEXT,ORDER_ TEXT,STC_COLOR TEXT,STC_OPACITY TEXT,XROTATEPROG TEXT,YROTATEPROG TEXT,ZROTATEPROG TEXT,STC_SCALE TEXT,STKR_PATH TEXT,COLORTYPE TEXT,STC_HUE TEXT,FIELD_ONE TEXT,FIELD_TWO TEXT,FIELD_THREE TEXT,FIELD_FOUR TEXT)"
        private const val CREATE_TABLE_TEMPLATES =
            "CREATE TABLE IF NOT EXISTS TEMPLATES(TEMPLATE_ID INTEGER PRIMARY KEY,ART_TYPE TEXT,ART_BG BLOB,THUMB_URI TEXT,FRAME_NAME TEXT,RATIO TEXT,PROFILE_TYPE TEXT,SEEK_VALUE TEXT,TYPE TEXT,TEMP_PATH TEXT,TEMP_COLOR TEXT,OVERLAY_NAME TEXT,OVERLAY_OPACITY TEXT,OVERLAY_BLUR TEXT,SHAP_NAME TEXT)"
        private const val CREATE_TABLE_TEXT_INFO =
            "CREATE TABLE IF NOT EXISTS TEXT_INFO(TEXT_ID INTEGER PRIMARY KEY,TEMPLATE_ID TEXT,TEXT TEXT,FONT_NAME TEXT,TEXT_COLOR TEXT,TEXT_ALPHA TEXT,SHADOW_COLOR TEXT,SHADOW_PROG TEXT,BG_DRAWABLE TEXT,BG_COLOR TEXT,BG_ALPHA TEXT,POS_X TEXT,POS_Y TEXT,WIDHT TEXT,HEIGHT TEXT,ROTATION TEXT,TYPE TEXT,ORDER_ TEXT,XROTATEPROG TEXT,YROTATEPROG TEXT,ZROTATEPROG TEXT,CURVEPROG TEXT,FIELD_ONE TEXT,FIELD_TWO TEXT,FIELD_THREE TEXT,FIELD_FOUR TEXT,TEXT_GRAVITY TEXT)"
        private const val CURVEPROG = "CURVEPROG"
        private const val DATABASE_NAME = "LOGOMAKER_DB"
        private var DATABASE_PATH = ""
        private const val DATABASE_VERSION = 1
        private const val FIELD_FOUR = "FIELD_FOUR"
        private const val FIELD_ONE = "FIELD_ONE"
        private const val FIELD_THREE = "FIELD_THREE"
        private const val FIELD_TWO = "FIELD_TWO"
        private const val FONT_NAME = "FONT_NAME"
        private const val FRAME_NAME = "FRAME_NAME"
        private const val HEIGHT = "HEIGHT"
        private const val ORDER = "ORDER_"
        private const val OVERLAY_BLUR = "OVERLAY_BLUR"
        private const val OVERLAY_NAME = "OVERLAY_NAME"
        private const val OVERLAY_OPACITY = "OVERLAY_OPACITY"
        private const val POS_X = "POS_X"
        private const val POS_Y = "POS_Y"
        private const val PROFILE_TYPE = "PROFILE_TYPE"
        private const val RATIO = "RATIO"
        private const val RES_ID = "RES_ID"
        private const val ROTATION = "ROTATION"
        private const val SEEK_VALUE = "SEEK_VALUE"
        private const val SHADOW_COLOR = "SHADOW_COLOR"
        private const val SHADOW_PROG = "SHADOW_PROG"
        private const val SHAP_NAME = "SHAP_NAME"
        private const val STC_COLOR = "STC_COLOR"
        private const val STC_HUE = "STC_HUE"
        private const val STC_OPACITY = "STC_OPACITY"
        private const val STC_SCALE = "STC_SCALE"
        private const val STKR_PATH = "STKR_PATH"
        private const val TEMPLATES = "TEMPLATES"
        private const val TEMPLATE_ID = "TEMPLATE_ID"
        private const val TEMP_COLOR = "TEMP_COLOR"
        private const val TEMP_PATH = "TEMP_PATH"
        private const val TEXT = "TEXT"
        private const val TEXT_ALPHA = "TEXT_ALPHA"
        private const val TEXT_COLOR = "TEXT_COLOR"
        private const val TEXT_GRAVITY = "TEXT_GRAVITY"
        private const val TEXT_ID = "TEXT_ID"
        private const val TEXT_INFO = "TEXT_INFO"
        private const val THUMB_URI = "THUMB_URI"
        private const val TYPE = "TYPE"
        private const val WIDHT = "WIDHT"
        private const val XROTATEPROG = "XROTATEPROG"
        private const val YROTATEPROG = "YROTATEPROG"
        private const val Y_ROTATION = "Y_ROTATION"
        private const val ZROTATEPROG = "ZROTATEPROG"

        @JvmStatic
        fun getDbHandler(context: Context): DatabaseHandler {
            DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).path
            return DatabaseHandler(context)
        }
    }
}
