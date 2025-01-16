package com.example.graphicsmaker.sticker_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Typeface
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graphicsmaker.R
import com.example.graphicsmaker.adapter.RecyclerItemClickListener
import com.example.graphicsmaker.adapter.RecyclerTemplateAdapter
import com.example.graphicsmaker.create.DatabaseHandler.Companion.getDbHandler
import com.example.graphicsmaker.create.TemplateInfo
import com.example.graphicsmaker.create.ViewTemplateCanvasFinal
import com.example.graphicsmaker.main.Constants
import com.example.graphicsmaker.main.CreatePoster
import com.example.graphicsmaker.utility.StorageConfiguration
import java.io.File
import java.io.FileOutputStream

class FragmentStepThree : Fragment() {
    private var animSlideDown: Animation? = null
    private var animSlideUp: Animation? = null

    var changePage: ChangePage? = null
    var countSize: Int = 0

    var lay_dialog: RelativeLayout? = null
    var myFramesAdapter: RecyclerTemplateAdapter? = null
    var positionCopy: Int = 0

    var progress_bar: ProgressBar? = null
    var recylr_templates: RecyclerView? = null
    var remove_ad_pref: SharedPreferences? = null
    var saveImageSize: Float = 1024.0f
    var saveTemplateAsync: SaveTemplateAsync? = null
    var screenWidth: Float = 0f
    private var templateList: ArrayList<TemplateInfo?> = ArrayList()
    var thumbLoadAsync: LoadingDataOperationAsync? = null
    var ttf: Typeface? = null
    var txt_dialog: TextView? = null

    interface ChangePage {
        fun setSecondPage(bool: Boolean?)
    }

    inner class LoadingDataOperationAsync : AsyncTask<String?, Void?, String>() {
        override fun onPreExecute() {
            if (this@FragmentStepThree.progress_bar != null) {
                progress_bar!!.visibility = View.VISIBLE
            }
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                val dh = getDbHandler(requireContext())
                this@FragmentStepThree.templateList = dh.getTemplateListDes("SAMPLE", "RANDOM")
                dh.close()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            return "yes"
        }

        override fun onPostExecute(result: String) {
            if (result == "yes") {
                if (templateList.size != 0) {
                    this@FragmentStepThree.myFramesAdapter = RecyclerTemplateAdapter(
                        this@FragmentStepThree.activity,
                        this@FragmentStepThree.templateList,
                        this@FragmentStepThree.screenWidth
                    )
                    recylr_templates!!.adapter = this@FragmentStepThree.myFramesAdapter
                    myFramesAdapter!!.notifyDataSetChanged()
                } else {
                    this@FragmentStepThree.showErrorDialog()
                }
            }
            if (this@FragmentStepThree.progress_bar != null) {
                progress_bar!!.visibility = View.GONE
            }
        }
    }

    inner class SaveTemplateAsync(var template_id: Int, var fstLoading: Boolean) :
        AsyncTask<String?, String?, Boolean>() {
        var dialogIs: ProgressDialog? = null

        override fun onPreExecute() {
            super.onPreExecute()
            this.dialogIs = ProgressDialog(this@FragmentStepThree.activity)
            dialogIs!!.setMessage(this@FragmentStepThree.resources.getString(R.string.plzwait))
            dialogIs!!.setCancelable(false)
            dialogIs!!.show()
        }

        override fun doInBackground(vararg params: String?): Boolean {
            var saveImageIs = false
            if (this.fstLoading) {
                getDbHandler(requireContext()).createDuplicateTemplate(
                    this.template_id
                )
            }
            try {
                val outputBitmap: Bitmap? = ViewTemplateCanvasFinal(
                    this@FragmentStepThree.activity,
                    this@FragmentStepThree.screenWidth * 2.0f,
                    this@FragmentStepThree.screenWidth * 2.0f,
                    this@FragmentStepThree.saveImageSize,
                    this@FragmentStepThree.saveImageSize,
                    this,
                    false,
                    160
                ).getTemplateBitmap(
                    this.template_id
                )
                saveImageIs = this@FragmentStepThree.saveImage(
                    requireActivity(),
                    outputBitmap,
                    true
                )
                if (outputBitmap != null) {
                    outputBitmap.recycle()
                }
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
            } catch (e2: Exception) {
                e2.printStackTrace()
            }
            return saveImageIs
        }

        override fun onPostExecute(isDownloaded: Boolean) {
            super.onPostExecute(isDownloaded)
            dialogIs!!.dismiss()
            Constants.freeMemory()
            val intent: Intent
            if (isDownloaded) {
                intent = Intent(this@FragmentStepThree.activity, CreatePoster::class.java)
                intent.putExtra("templateId", this.template_id)
                intent.putExtra("loadUserFrame", false)
                intent.putExtra("Temp_Type", "MY_TEMP")
                this@FragmentStepThree.startActivity(intent)
            } else if (this@FragmentStepThree.countSize == 0) {
                countSize++
                this@FragmentStepThree.saveImageSize =
                    (this@FragmentStepThree.saveImageSize * 80.0f) / 100.0f
                this@FragmentStepThree.purchaseDesign(false)
            } else if (this@FragmentStepThree.countSize < 4) {
                countSize++
                this@FragmentStepThree.saveImageSize =
                    (this@FragmentStepThree.saveImageSize * 80.0f) / 100.0f
                this@FragmentStepThree.purchaseDesign(false)
            } else {
                Toast.makeText(
                    requireActivity(),
                    requireActivity().resources.getString(
                        R.string.save_err
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                intent = Intent(this@FragmentStepThree.activity, CreatePoster::class.java)
                intent.putExtra("templateId", this.template_id)
                intent.putExtra("loadUserFrame", false)
                intent.putExtra("Temp_Type", "MY_TEMP")
                this@FragmentStepThree.startActivity(intent)
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            try {
                if (this.thumbLoadAsync != null) {
                    thumbLoadAsync!!.cancel(true)
                    this.thumbLoadAsync = null
                }
                templateList.clear()
                this.thumbLoadAsync = LoadingDataOperationAsync()
                thumbLoadAsync!!.execute(*arrayOf(""))
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_designer_template, container, false)

        this.remove_ad_pref = PreferenceManager.getDefaultSharedPreferences(
            activity
        )
        this.lay_dialog = view.findViewById<View>(R.id.lay_dialog) as RelativeLayout
        this.txt_dialog = view.findViewById<View>(R.id.txt_dialog) as TextView
        this.animSlideUp = Constants.getAnimUp(requireActivity())
        this.animSlideDown = Constants.getAnimDown(requireActivity())
        this.ttf = Constants.getTextTypeface(requireActivity())
        txt_dialog!!.setTypeface(this.ttf)
        (view.findViewById<View>(R.id.txtPrimimum) as TextView).setTypeface(
            this.ttf
        )
        val dimension = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(dimension)
        this.screenWidth = (dimension.widthPixels / 2).toFloat()
        this.changePage = activity as ChangePage
        this.progress_bar = view.findViewById<View>(R.id.progress_bar) as ProgressBar
        progress_bar!!.visibility = View.GONE
        this.recylr_templates = view.findViewById<View>(R.id.gridview) as RecyclerView
        recylr_templates!!.layoutManager = GridLayoutManager(requireActivity(), 2)
        recylr_templates!!.setHasFixedSize(true)
        recylr_templates!!.addOnItemTouchListener(
            RecyclerItemClickListener(
                activity, RecyclerItemClickListener.OnItemClickListener { view, position ->
                    if (remove_ad_pref!!.getBoolean("isDesignerPurchased", false)) {
                        RecyclerTemplateAdapter.isTemplateLoaded = true
                        val template_id =
                            templateList[position]!!.templatE_ID
                        val intent =
                            Intent(this@FragmentStepThree.requireActivity(), CreatePoster::class.java)
                        intent.putExtra("templateId", template_id)
                        intent.putExtra("loadUserFrame", false)
                        intent.putExtra("Temp_Type", "MY_TEMP")
                        this@FragmentStepThree.startActivity(intent)
                        return@OnItemClickListener
                    }
                    this@FragmentStepThree.positionCopy = position
                    lay_dialog!!.visibility = View.VISIBLE
                    lay_dialog!!.startAnimation(this@FragmentStepThree.animSlideUp)
                })
        )
        recylr_templates!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    1 -> {
                        if (lay_dialog!!.visibility == View.VISIBLE) {
                            lay_dialog!!.startAnimation(this@FragmentStepThree.animSlideDown)
                            lay_dialog!!.visibility = View.GONE
                            return
                        }
                        return
                    }

                    else -> return
                }
            }
        })
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode != 4) {
                return@OnKeyListener false
            }
            if (this@FragmentStepThree.thumbLoadAsync != null) {
                if (thumbLoadAsync!!.status == AsyncTask.Status.PENDING) {
                    thumbLoadAsync!!.cancel(true)
                }
                if (thumbLoadAsync!!.status == AsyncTask.Status.RUNNING) {
                    thumbLoadAsync!!.cancel(true)
                }
            }
            if (lay_dialog!!.visibility != View.VISIBLE) {
                return@OnKeyListener false
            }
            lay_dialog!!.startAnimation(this@FragmentStepThree.animSlideDown)
            lay_dialog!!.visibility = View.GONE
            true
        })
        lay_dialog!!.setOnTouchListener { v, event -> false }
        (view.findViewById<View>(R.id.btn_inapp) as RelativeLayout).setOnClickListener { this@FragmentStepThree.showPremiumDialog() }
        return view
    }

    override fun onResume() {
        super.onResume()
        RecyclerTemplateAdapter.isTemplateLoaded = false
        if (this.myFramesAdapter != null) {
            myFramesAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this.thumbLoadAsync != null) {
            thumbLoadAsync!!.cancel(true)
            this.thumbLoadAsync = null
            this.progress_bar = null
        }
        this.recylr_templates = null
        this.myFramesAdapter = null
        if (this.saveTemplateAsync != null) {
            if (saveTemplateAsync!!.status == AsyncTask.Status.PENDING) {
                saveTemplateAsync!!.cancel(true)
            }
            if (saveTemplateAsync!!.status == AsyncTask.Status.RUNNING) {
                saveTemplateAsync!!.cancel(true)
            }
        }
        Constants.freeMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            Thread {
                try {
                    Glide.get(requireActivity()).clearDiskCache()
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }.start()
            Glide.get(requireActivity()).clearMemory()
            if (this.thumbLoadAsync != null) {
                if (thumbLoadAsync!!.status == AsyncTask.Status.PENDING) {
                    thumbLoadAsync!!.cancel(true)
                }
                if (thumbLoadAsync!!.status == AsyncTask.Status.RUNNING) {
                    thumbLoadAsync!!.cancel(true)
                }
            }
            this.recylr_templates = null
            this.myFramesAdapter = null
            templateList.clear()
            if (this.thumbLoadAsync != null) {
                thumbLoadAsync!!.cancel(true)
                this.thumbLoadAsync = null
                this.progress_bar = null
            }
            this.txt_dialog = null
            this.ttf = null
            this.animSlideUp = null
            this.animSlideDown = null
            this.lay_dialog = null
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        } catch (e2: Exception) {
            e2.printStackTrace()
        }
        Constants.freeMemory()
    }

    private fun showErrorDialog() {
        val dialog1 = Dialog(requireActivity(), 16974132)
        dialog1.requestWindowFeature(1)
        dialog1.setCancelable(true)
        dialog1.setContentView(R.layout.error_designer_dialog)
        (dialog1.findViewById<View>(R.id.heater) as TextView).typeface =
            Constants.getHeaderTypeface(
                requireActivity()
            )
        (dialog1.findViewById<View>(R.id.txt_free) as TextView).typeface =
            Constants.getTextTypeface(
                requireActivity()
            )
        val btn_ok = dialog1.findViewById<View>(R.id.btn_ok) as Button
        btn_ok.typeface = Constants.getTextTypeface(requireActivity())
        btn_ok.setOnClickListener {
            this@FragmentStepThree.sendEmail()
            dialog1.dismiss()
        }
        dialog1.show()
    }

    private fun sendEmail() {
        val mailto =
            "mailto:abc@gmail.com?cc=&subject=" + Uri.encode(resources.getString(R.string.app_name) + " V" + "1.0" + " " + 3) + "&body=" + Uri.encode(
                resources.getString(R.string.designer_msg)
            )
        val emailIntent = Intent("android.intent.action.SENDTO")
        emailIntent.setData(Uri.parse(mailto))
        try {
            startActivityForResult(emailIntent, OPEN_HELP_ACITIVITY)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, resources.getString(R.string.email_error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showPremiumDialog() {
        val dialog = Dialog(requireActivity(), 16974126)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.premium_dialog)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = -1
        lp.height = -2

        (dialog.findViewById<View>(R.id.txtHeadet) as TextView).typeface =
            Constants.getHeaderTypeface(
                requireActivity()
            )

        (dialog.findViewById<View>(R.id.headingtxt4) as TextView).setTypeface(
            Constants.getHeaderTypeface(
                requireActivity()
            ), Typeface.BOLD
        )
        (dialog.findViewById<View>(R.id.txt4) as TextView).typeface = Constants.getHeaderTypeface(
            requireActivity()
        )

        (dialog.findViewById<View>(R.id.no_thanks) as Button).typeface =
            Constants.getHeaderTypeface(
                requireActivity()
            )

        (dialog.findViewById<View>(R.id.btn_PremiumMonthly) as RelativeLayout).setOnClickListener(
            View.OnClickListener {
                dialog.dismiss()
                val pakagename = requireActivity().packageName
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
                dialog.dismiss()
            }
        })
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.show()
        dialog.window!!.addFlags(2)
    }

    private fun saveImage(activity: Activity, bitmap: Bitmap?, inPNG: Boolean): Boolean {
        try {
            val pictureFileDir = StorageConfiguration.getSavedImagesPath()
            if (pictureFileDir.exists() || pictureFileDir.mkdirs()) {
                var photoFile = "IMG_" + System.currentTimeMillis()
                photoFile = if (inPNG) {
                    "$photoFile.png"
                } else {
                    "$photoFile.jpg"
                }
                val pictureFile = File(pictureFileDir.path + File.separator + photoFile)
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
                            bitmap!!.width, bitmap.height, bitmap.config!!
                        )
                        val canvas = Canvas(newBitmap)
                        canvas.drawColor(-1)
                        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null)
                        saved = newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
                        newBitmap.recycle()
                    }
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
                    return false
                }
            }
            Log.d("", "Can't create directory to save image.")
            Toast.makeText(
                activity,
                activity.resources.getString(R.string.create_dir_err),
                Toast.LENGTH_LONG
            ).show()
            return false
        } catch (e2: Exception) {
            e2.printStackTrace()
            Log.i("testing", "Exception" + e2.message)
            return false
        }
    }

    private fun purchaseDesign(booleanIs: Boolean) {
        if (booleanIs) {
            lay_dialog!!.startAnimation(this.animSlideDown)
            lay_dialog!!.visibility = View.GONE
        }
        this.saveTemplateAsync = SaveTemplateAsync(
            templateList[positionCopy]!!.templatE_ID, booleanIs
        )
        saveTemplateAsync!!.execute(*arrayOfNulls<String>(0))
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OPEN_HELP_ACITIVITY) {
            changePage!!.setSecondPage(true)
        }
    }


    companion object {
        private const val OPEN_HELP_ACITIVITY = 2299
    }
}
