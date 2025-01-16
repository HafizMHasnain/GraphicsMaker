package com.example.graphicsmaker.main

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.graphicsmaker.R
import com.example.graphicsmaker.create.ViewTemplateCanvasFinal
import com.example.graphicsmaker.scale.ImageSource
import com.example.graphicsmaker.scale.SubsamplingScaleImageView
import vocsy.ads.GoogleAds
import java.io.File
import java.io.FileOutputStream

class ShareImageActivity : Activity(), View.OnClickListener {
    var btn_remowatermark: RelativeLayout? = null
    var ch: Boolean = false
    var editor: SharedPreferences.Editor? = null
    var imagePath: String? = ""
    private var imageView: SubsamplingScaleImageView? = null
    var isSaved: Boolean = false
    var mAdView: RelativeLayout? = null

    private var phototUri: Uri? = null
    var prefs: SharedPreferences? = null
    var remove_ad_pref: SharedPreferences? = null
    var saveImageSize: Float = 0f
    var saveTemplateAsync: SaveTemplateAsync? = null
    var screenWidth: Float = 0f
    var sharedpreferences: SharedPreferences? = null
    var templateId: Int = -1
    var tf1: Typeface? = null
    var tfText: Typeface? = null
    var ttfBold: Typeface? = null
    var wayStr: String? = null

    inner class SaveTemplateAsync constructor() : AsyncTask<String?, String?, Boolean>() {
        var dialogIs: ProgressDialog? = null
        var targetDensity: Int = 640

        override fun onPreExecute() {
            super.onPreExecute()
            this.dialogIs = ProgressDialog(this@ShareImageActivity)
            dialogIs!!.setMessage(this@ShareImageActivity.resources.getString(R.string.plzwait))
            dialogIs!!.setCancelable(false)
            dialogIs!!.show()
        }

        override fun doInBackground(vararg params: String?): Boolean {
            try {
                if (this@ShareImageActivity.templateId != -1) {
                    val bitmap: Bitmap
                    bitmap = if (saveImageSize >= (512f)) {
                        ViewTemplateCanvasFinal(
                            this@ShareImageActivity,
                            this@ShareImageActivity.screenWidth,
                            this@ShareImageActivity.screenWidth,
                            this@ShareImageActivity.saveImageSize,
                            this@ShareImageActivity.saveImageSize,
                            this,
                            false,
                            this.targetDensity
                        )
                            .getTemplateBitmap(
                                this@ShareImageActivity.templateId
                            )!!
                    } else {
                        Bitmap.createScaledBitmap(
                            ViewTemplateCanvasFinal(
                                this@ShareImageActivity,
                                this@ShareImageActivity.screenWidth,
                                this@ShareImageActivity.screenWidth,
                                512f,
                                512f,
                                this,
                                false,
                                this.targetDensity
                            ).getTemplateBitmap(
                                this@ShareImageActivity.templateId
                            )!!,
                            saveImageSize.toInt(),
                            saveImageSize.toInt(), false
                        )
                    }
                    this@ShareImageActivity.isSaved =
                        this@ShareImageActivity.saveWithoutWatermark(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } catch (e2: OutOfMemoryError) {
                e2.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(isDownloaded: Boolean) {
            super.onPostExecute(isDownloaded)
            dialogIs!!.dismiss()
            Constants.freeMemory()
            if (this@ShareImageActivity.isSaved) {
                this@ShareImageActivity.setImageUriOnImageView(this@ShareImageActivity.imagePath)
            } else {
                Toast.makeText(
                    this@ShareImageActivity.applicationContext,
                    this@ShareImageActivity.resources.getString(
                        R.string.picUpImg
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_image)
        this.remove_ad_pref = PreferenceManager.getDefaultSharedPreferences(
            applicationContext
        )
        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.adView))
        this.tfText = Constants.getTextTypeface(this)
        initUI()


        if (remove_ad_pref!!.getBoolean("isDesignerPurchased", false)) {
            (findViewById<View>(R.id.remove_water_txt) as TextView).text =
                resources.getString(R.string.inapp_tittle)
        }
        val dimension = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dimension)
        this.screenWidth = dimension.widthPixels.toFloat()
        this.sharedpreferences = getSharedPreferences(MyPREFERENCES, 0)
        this.editor = getSharedPreferences("MY_PREFS_NAME", 0).edit()
        this.prefs = getSharedPreferences("MY_PREFS_NAME", 0)


        this.ttfBold = Constants.getTextHeadingTypeface(this)
        (findViewById<View>(R.id.remove_water_txt) as TextView).setTypeface(
            this.tfText
        )
//        ((TextView) findViewById (R.id.txtShare)).setTypeface(this.tfText);
//        ((TextView) findViewById (R.id.txtMore)).setTypeface(this.tfText);
        if (templateId != -1) {
            saveTemplateAsync = SaveTemplateAsync()
            saveTemplateAsync!!.execute(*arrayOfNulls<String>(0))
        }
    }

    fun initUI() {
        this.imageView = findViewById<View>(R.id.image) as SubsamplingScaleImageView
        this.tf1 = Constants.getHeaderTypeface(this)
        (findViewById<View>(R.id.txt_toolbar) as TextView).typeface = this.tf1
        findViewById<View>(R.id.btn_home).setOnClickListener(this)
        findViewById<View>(R.id.btn_share).setOnClickListener(
            this
        )
        findViewById<View>(R.id.btn_more).setOnClickListener(this)
        this.btn_remowatermark = findViewById<View>(R.id.btn_remowatermark) as RelativeLayout
        btn_remowatermark!!.setOnClickListener(this)
        val extra = intent.extras
        if (extra != null) {
            this.wayStr = extra.getString("way")
            this.imagePath = extra.getString("uri")
            if (this.wayStr == "logo") {
                this.templateId = extra.getInt("templateId")
                this.saveImageSize = extra.getFloat("saveImageSize")
                if (this.remove_ad_pref?.getBoolean("removeWatermark", false) == true) {
                    btn_remowatermark!!.visibility = View.GONE
                } else {
                    this.btn_remowatermark?.visibility = View.VISIBLE;
                }
                showImageSaveDialog(wayStr ?: "")
            } else if (this.wayStr == "poster") {
                this.templateId = extra.getInt("templateId")
                this.saveImageSize = extra.getFloat("saveImageSize")
                if (this.remove_ad_pref?.getBoolean("removeWatermark", false) == true) {
                    btn_remowatermark!!.visibility = View.GONE
                } else {
                    this.btn_remowatermark?.visibility = View.VISIBLE;
                }
                showImageSaveDialog(wayStr ?: "")
            } else if (this.wayStr == "card") {
                this.templateId = extra.getInt("templateId")
                this.saveImageSize = extra.getFloat("saveImageSize")
                if (this.remove_ad_pref?.getBoolean("removeWatermark", false) == true) {
                    btn_remowatermark!!.visibility = View.GONE
                } else {
                    this.btn_remowatermark?.visibility = View.VISIBLE;
                }
                showImageSaveDialog(wayStr ?: "")
            } else {
                btn_remowatermark!!.visibility = View.GONE
            }
            if (imagePath != "") {
                setImageUriOnImageView(this.imagePath)
                return
            }
            return
        }
        Toast.makeText(this, resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun setImageUriOnImageView(imagePath: String?) {
        if (imagePath == "") {
            Toast.makeText(this, resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        this.phototUri = Uri.parse(imagePath)
        try {
            imageView!!.setImage(ImageSource.uri(Uri.fromFile(File(imagePath))))
        } catch (outOfMemoryError: OutOfMemoryError) {
            outOfMemoryError.printStackTrace()
        }
    }

    private fun showImageSaveDialog(msg: String) {
        val dialog1 = Dialog(this /*, 16974126*/)
        dialog1.requestWindowFeature(1)
        dialog1.setContentView(R.layout.save_success_picture_dialog)
        dialog1.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog1.window!!.attributes)
        lp.width = -1
        lp.height = -2
        val heater = (dialog1.findViewById<View>(R.id.heater) as TextView)
        heater.text = msg
        heater.typeface = this.tf1
        val body = (dialog1.findViewById<View>(R.id.txt_free) as TextView)
        body.text = getString(R.string.save_Imgmsg, msg)
        body.typeface = this.tfText
        val textPath = dialog1.findViewById<View>(R.id.txt_path) as TextView
        textPath.typeface = tfText
        textPath.text = imagePath
        val btn_ok = dialog1.findViewById<View>(R.id.btn_ok) as Button
        btn_ok.typeface = tfText
        btn_ok.setOnClickListener { dialog1.dismiss() }
        lp.dimAmount = 0.7f
        dialog1.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog1.show()
        dialog1.window!!.attributes = lp
        dialog1.window!!.addFlags(2)
    }

    private fun shareImage() {
        GoogleAds.getInstance().showCounterInterstitialAd(this@ShareImageActivity) {
            val intent: Intent = Intent("android.intent.action.SEND")
            val file: File = File(phototUri!!.getPath())
            intent.setType("image/*")
            intent.putExtra(
                "android.intent.extra.SUBJECT",
                getResources().getString(R.string.app_name)
            )
            intent.putExtra(
                "android.intent.extra.TEXT",
                (getResources().getString(R.string.sharetext) + " " + getResources().getString(
                    R.string.app_name
                ) + ". " + getResources().getString(R.string.sharetext1)) + "https://play.google.com/store/apps/details?id=" + getPackageName()
            )
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file))
            startActivity(Intent.createChooser(intent, getString(R.string.share_via).toString()))
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_home) {
            GoogleAds.getInstance().showCounterInterstitialAd(this) {
                val intent: Intent = Intent(getApplicationContext(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }


            return
        } else if (id == R.id.btn_share) {
            shareImage()
            return
        } else if (id == R.id.btn_more) {
            val url =
                "https://play.google.com/store/apps/developer?id=" + resources.getString(R.string.dev_name)
            val i = Intent("android.intent.action.VIEW")
            i.setData(Uri.parse(url))
            startActivity(i)
            return
        } else if (id == R.id.btn_remowatermark) {
            showPremiumDialog()
            return
        }
        return
    }

    private fun showPremiumDialog() {
        val dialog = Dialog(this /*, 16974126*/)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.premium_dialog)
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
            View.OnClickListener {
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
            })

        dialog.findViewById<View>(R.id.no_thanks).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                this@ShareImageActivity.remove_watermark_video_dialog()
                dialog.dismiss()
            }
        })
        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.show()
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(2)
    }

    private fun sendEmail() {
        val mailto =
            "mailto:sattiger12@gmail.com?cc=&subject=" + Uri.encode(resources.getString(R.string.app_name) + " V" + "1.0" + " " + 3) + "&body=" + Uri.encode(
                resources.getString(R.string.email_msg)
            )
        val emailIntent = Intent("android.intent.action.SENDTO")
        emailIntent.setData(Uri.parse(mailto))
        try {
            startActivityForResult(emailIntent, OPEN_HELP_ACITIVITY)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, resources.getString(R.string.email_error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun saveWithoutWatermark(btmSimple: Bitmap): Boolean {
        val bitmap = btmSimple.copy(btmSimple.config!!, true)
        val pictureFile = File(this.imagePath)
        try {
            if (!pictureFile.exists()) {
                pictureFile.createNewFile()
            }
            val ostream = FileOutputStream(pictureFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream)
            ostream.flush()
            ostream.close()
            bitmap.recycle()
            btmSimple.recycle()
            sendBroadcast(
                Intent(
                    "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
                    Uri.fromFile(pictureFile)
                )
            )
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun remove_watermark_video_dialog() {
        val dialog = Dialog(this /*, 16974126*/)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.remove_watermark_vidadv_dialog)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = -1
        lp.height = -2
        (dialog.findViewById<View>(R.id.headertext) as TextView).typeface = this.tf1
        (dialog.findViewById<View>(R.id.remove_watermark_msg) as TextView).typeface = this.tfText
        val no_thanks = dialog.findViewById<View>(R.id.no_thanks) as Button
        no_thanks.typeface = tfText
        no_thanks.setOnClickListener { dialog.dismiss() }
        val watch_ad = dialog.findViewById<View>(R.id.watch_ad) as Button
        watch_ad.typeface = tfText
        watch_ad.setOnClickListener {
            dialog.dismiss()
            //                GoogleAds.getInstance().showRewardedAd(ShareImageActivity.this, () -> {
            if (templateId != -1) {
                saveTemplateAsync = SaveTemplateAsync()
                saveTemplateAsync!!.execute(*arrayOfNulls<String>(0))
            }


            //                });
            val dialog1 = Dialog(this@ShareImageActivity  /*, 16974126*/)
            dialog1.requestWindowFeature(1)
            dialog1.setContentView(R.layout.interner_connection_dialog)
            dialog1.setCancelable(true)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog1.window!!.attributes)
            lp.width = -1
            lp.height = -2
            (dialog1.findViewById<View>(R.id.heater) as TextView).setTypeface(
                this@ShareImageActivity.tf1
            )
            (dialog1.findViewById<View>(R.id.txt_free) as TextView).setTypeface(
                this@ShareImageActivity.tfText
            )
            val btn_ok = dialog1.findViewById<View>(R.id.btn_ok) as Button
            btn_ok.setTypeface(this@ShareImageActivity.tfText)
            btn_ok.setOnClickListener { dialog1.dismiss() }
            lp.dimAmount = 0.7f
            dialog1.window!!.attributes.windowAnimations = R.style.DialogAnimation_
            dialog1.show()
            dialog1.window!!.attributes = lp
            dialog1.window!!.addFlags(2)
        }
        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_

        if (GoogleAds.checkConnection(this)) {
            dialog.show()
            dialog.window!!.attributes = lp
            dialog.window!!.addFlags(2)
        }
    }

    private val isNetworkAvailable: Boolean
        get() {
            val activeNetworkInfo =
                (getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == OPEN_HELP_ACITIVITY) {
            //this.lay_helpFeedback.setVisibility(View.GONE);
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (this.ch) {
            remove_watermark_video_dialog()
            this.ch = false
        }
        if (remove_ad_pref!!.getBoolean("isAdsDisabled", false)) {
            mAdView!!.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i("testing", "In  Logo Maker OnDestroy")
        try {
            Thread {
                try {
                    Glide.get(this@ShareImageActivity).clearDiskCache()
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }.start()
            Glide.get(this).clearMemory()
            if (this.saveTemplateAsync != null) {
                if (saveTemplateAsync!!.status == AsyncTask.Status.PENDING) {
                    saveTemplateAsync!!.cancel(true)
                }
                if (saveTemplateAsync!!.status == AsyncTask.Status.RUNNING) {
                    saveTemplateAsync!!.cancel(true)
                }
            }
            this.phototUri = null
            this.imageView = null
            this.btn_remowatermark = null
            System.gc()
            Runtime.getRuntime().gc()
        } catch (e4: OutOfMemoryError) {
            e4.printStackTrace()
        } catch (e32: Exception) {
            e32.printStackTrace()
        }
    }


    companion object {
        private const val MyPREFERENCES = "LogoMakerPref"
        private const val OPEN_HELP_ACITIVITY = 2299
    }
}
