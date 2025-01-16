package com.example.graphicsmaker.main


import android.app.ComponentCaller
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.graphicsmaker.R
import com.example.graphicsmaker.adapter.DesignerViewPagerAdapter
import com.example.graphicsmaker.create.DatabaseHandler.Companion.getDbHandler
import com.example.graphicsmaker.sticker_fragment.CallFragmentInterface
import com.example.graphicsmaker.sticker_fragment.FragmentStepThree
import com.example.graphicsmaker.sticker_fragment.FragmentStepThree.ChangePage
import it.neokree.materialtabs.MaterialTab
import it.neokree.materialtabs.MaterialTabHost
import it.neokree.materialtabs.MaterialTabListener
import vocsy.ads.GoogleAds

class DesignerLogoActivity() : AppCompatActivity(), MaterialTabListener, CallFragmentInterface, ChangePage {
    var adapter: DesignerViewPagerAdapter? = null
    var editor: SharedPreferences.Editor? = null
    var pager: ViewPager? = null
    var prefs: SharedPreferences? = null
    var tabHost: MaterialTabHost? = null
    var thumbLoadAsync: LordDataOperationAsync? = null

    inner class LordDataOperationAsync(): AsyncTask<String?, Void?, Long>() {
        var danim: AnimationDrawable? = null
        var dialogIs: Dialog? = null
        var img_drawable: ImageView? = null
        var result: Long = 0

        override fun onPreExecute() {
            this.dialogIs = Dialog(this@DesignerLogoActivity, R.style.MyDialogTheme)
            dialogIs!!.requestWindowFeature(1)
            dialogIs!!.setContentView(R.layout.progressdialog)
            dialogIs!!.window!!.setBackgroundDrawable(ColorDrawable(0))
            dialogIs!!.setCancelable(false)
            this.img_drawable = dialogIs!!.findViewById<View>(R.id.img_drawable) as ImageView
            this.danim = img_drawable!!.drawable as AnimationDrawable
            danim!!.start()
            dialogIs!!.show()
        }

        override fun doInBackground(vararg params: String?): Long {
            this.result = getDbHandler(this@DesignerLogoActivity).createTemplates(
                params[0], params[1], params[2], params[3], "", this@DesignerLogoActivity
            ).toLong()
            return this.result
        }

        override fun onPostExecute(result: Long) {
            danim!!.stop()
            dialogIs!!.dismiss()
            this.img_drawable = null
            if (result.toInt() != 0 && this@DesignerLogoActivity.pager != null) {
                pager!!.setCurrentItem(2, true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_templates)

        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.adView))

        this.editor = getSharedPreferences("MY_PREFS_NAME", 0).edit()
        this.prefs = getSharedPreferences("MY_PREFS_NAME", 0)
        val txtheader = findViewById<View>(R.id.txt_appname) as TextView
        txtheader.text = resources.getString(R.string.temp_Designer)
        txtheader.typeface = Constants.getHeaderTypeface(this)
        initUI()
        (findViewById<View>(R.id.btn_bck) as ImageButton).setOnClickListener(
            View.OnClickListener {
                GoogleAds.getInstance().showCounterInterstitialAd(this@DesignerLogoActivity, {
                    onBackPressed()
                })
            })
    }


    private fun initUI() {
        this.tabHost = findViewById<View>(R.id.tabHost) as MaterialTabHost
        tabHost!!.visibility = View.VISIBLE
        this.pager = findViewById<View>(R.id.pager) as ViewPager
        this.adapter = DesignerViewPagerAdapter(this, supportFragmentManager)
        adapter!!.notifyDataSetChanged()
        pager!!.adapter = this.adapter
        pager!!.offscreenPageLimit = 2
        pager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 1 && (prefs!!.getString(
                        "companyName",
                        null
                    ) == null || (prefs!!.getString("companyName", null) == ""))
                ) {
                    pager!!.currentItem = 0
                    tabHost!!.setSelectedNavigationItem(0)
                    this@DesignerLogoActivity.showDialogCompany("company")
                } else if (position == 2 && (prefs!!.getString(
                        "fontName",
                        null
                    ) == null || (prefs!!.getString("fontName", null) == ""))
                ) {
                    pager!!.currentItem = 1
                    tabHost!!.setSelectedNavigationItem(1)
                    this@DesignerLogoActivity.showDialogCompany("font")
                } else {
                    tabHost!!.setSelectedNavigationItem(position)
                }
            }

            override fun onPageSelected(position: Int) {
                val view = this@DesignerLogoActivity.currentFocus
                if (view != null) {
                    (this@DesignerLogoActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        view.windowToken,
                        0
                    )
                }
                if (position == 0) {
                    try {
                        if (prefs!!.getString("companyName", null) == null) {
                            pager!!.setCurrentItem(0, true)
                            return
                        }
                    } catch (e: Exception) {
                        return
                    }
                }
                if (position == 1 && prefs!!.getString("fontName", null) == null) {
                    pager!!.setCurrentItem(1, true)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        for (i in 0 until adapter!!.count) {
            tabHost!!.addTab(
                tabHost!!.newTab().setText(
                    adapter!!.getPageTitle(i)
                ).setTabListener(this)
            )
        }
        if ((prefs!!.getString("companyName", null) != null) && (prefs!!.getString(
                "fontName",
                null
            ) != null) && prefs!!.getString(
                "companyName",
                null
            ) != "" && prefs!!.getString("fontName", null) != ""
        ) {
            pager!!.setCurrentItem(2, true)
        }
    }

    private fun showDialogCompany(strIs: String) {
        val dialog1 = Dialog(this /*, 16974132*/)
        dialog1.requestWindowFeature(1)
        dialog1.setCancelable(false)
        dialog1.setContentView(R.layout.designer_dialog)
        (dialog1.findViewById<View>(R.id.heater) as TextView).typeface =
            Constants.getHeaderTypeface(
                this
            )
        val text = dialog1.findViewById<View>(R.id.txt_free) as TextView
        if ((strIs == "company")) {
            text.text = resources.getString(R.string.txtform1)
        } else {
            text.text = resources.getString(R.string.txtform2)
        }
        text.typeface = Constants.getTextTypeface(this)
        val btn_ok = dialog1.findViewById<View>(R.id.btn_ok) as Button
        btn_ok.typeface = Constants.getTextTypeface(this)
        btn_ok.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dialog1.dismiss()
            }
        })
        dialog1.show()
    }

    override fun onTabSelected(tab: MaterialTab) {
        try {
            if (tab.position != 1 || prefs!!.getString("companyName", null) != null) {
                if ((tab.position != 2 || prefs!!.getString(
                        "fontName",
                        null
                    ) != null) && this.pager != null
                ) {
                    pager!!.setCurrentItem(tab.position, true)
                }
            }
        } catch (e: Exception) {
        }
    }

    override fun onTabReselected(tab: MaterialTab) {
    }

    override fun onTabUnselected(tab: MaterialTab) {
    }

    override fun onBackPressed() {
        if (this.thumbLoadAsync != null) {
            if (thumbLoadAsync!!.status == AsyncTask.Status.PENDING) {
                thumbLoadAsync!!.cancel(true)
            }
            if (thumbLoadAsync!!.status == AsyncTask.Status.RUNNING) {
                thumbLoadAsync!!.cancel(true)
            }
        }
        if (this.pager == null) {
            return
        }
        if (pager!!.currentItem == 2) {
            pager!!.setCurrentItem(1, true)
        } else if (pager!!.currentItem == 1) {
            pager!!.setCurrentItem(0, true)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCallFragment(frag1: String, frag2: String) {
        if ((frag1 == "step2") && (frag2 == "") && (this.pager != null)) {
            pager!!.setCurrentItem(1, true)
        }
        if ((frag1 == "step2") && (frag2 == "step3") && (this.pager != null)) {
            var industryName =
                prefs!!.getString("indestryName", "OTHER")
            var companyName = prefs!!.getString("companyName", "")
            var tagLine = prefs!!.getString("tagLine", "")
            val fontName = prefs!!.getString("fontName", "")
            companyName = companyName!!.replace("'".toRegex(), "''")
            industryName = industryName!!.replace("'".toRegex(), "''")
            tagLine = tagLine!!.replace("'".toRegex(), "''")
            if ((companyName == "")) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.txt_companyNametoast),
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            companyName = companyName.trim { it <= ' ' }
            tagLine = tagLine.trim { it <= ' ' }
            this.thumbLoadAsync = LordDataOperationAsync()
            thumbLoadAsync!!.execute(*arrayOf(companyName, tagLine, industryName, fontName))
        }
    }

    override fun setSecondPage(bln: Boolean?) {
        pager!!.currentItem = 1
        tabHost!!.setSelectedNavigationItem(1)
    }

    public override fun onDestroy() {
        super.onDestroy()
        this.tabHost = null
        this.pager = null
        this.adapter = null
        if (this.thumbLoadAsync != null) {
            if (thumbLoadAsync!!.status == AsyncTask.Status.PENDING) {
                thumbLoadAsync!!.cancel(true)
                this.thumbLoadAsync = null
            }
            if (thumbLoadAsync!!.status == AsyncTask.Status.RUNNING) {
                thumbLoadAsync!!.cancel(true)
                this.thumbLoadAsync = null
            }
        }
        Constants.freeMemory()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if ((this.pager != null) && (pager!!.childCount != 0) && (adapter!!.currentFragment(
                pager!!.currentItem
            ) is FragmentStepThree)
        ) {
            adapter!!.currentFragment(pager!!.currentItem)
                ?.onActivityResult(requestCode, resultCode, data)
        }
    }
}
