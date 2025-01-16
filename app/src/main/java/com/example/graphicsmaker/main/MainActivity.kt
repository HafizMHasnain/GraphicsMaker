package com.example.graphicsmaker.main

import android.annotation.SuppressLint
import android.app.ComponentCaller
import android.app.Dialog
import android.content.Intent
import android.content.Intent.ShortcutIconResource
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.graphicsmaker.BuildConfig
import com.example.graphicsmaker.LaunchManager
import com.example.graphicsmaker.R
import com.example.graphicsmaker.databinding.ActivityMainBinding
import com.example.graphicsmaker.databinding.FreeTrialDialogLayoutBinding
import com.example.graphicsmaker.premium.SubscribePremium
import vocsy.ads.GetSmartAdmob
import vocsy.ads.GoogleAds

class MainActivity() : AppCompatActivity(), View.OnClickListener {
    private val viewModel: MainViewModel by viewModels<MainViewModel>()
    var editor: SharedPreferences.Editor? = null
    private var isOpenFisrtTime = false
    var mAdView: RelativeLayout? = null
    //    FrameLayout nativeFrameLayout;
    var prefs: SharedPreferences? = null
    var ttf: Typeface? = null
    var ttfText: Typeface? = null

    private var toggle: ActionBarDrawerToggle? = null
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val color = Color.parseColor("#494949")
        binding.contentMainInc.myProjects.imageTintList = ColorStateList.valueOf(color)
        binding.contentMainInc.saveDesign.imageTintList = ColorStateList.valueOf(color)

        // Set up the toggle for the drawer
        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, R.string.open_drawer, R.string.close_drawer
        )
        toggle!!.drawerArrowDrawable.color = Color.parseColor("#ffffff")
        binding.drawerLayout.addDrawerListener(toggle!!)
        toggle?.syncState()
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navigationView.setNavigationItemSelectedListener { item ->
            val intent: Intent
            when (item.itemId) {
                R.id.nav_logo -> {
                    intent = Intent(this@MainActivity, CreatePoster::class.java)
                    intent.putExtra("ratio", "1:1")
                    intent.putExtra("profile", "no")
                    intent.putExtra("hex", "no")
                    intent.putExtra("key", "logo")
                    intent.putExtra("loadUserFrame", true)
                    this@MainActivity.startActivity(intent)
                }

                R.id.nav_card -> {
                    intent = Intent(this@MainActivity, CreatePoster::class.java)
                    intent.putExtra("key", "card")
                    intent.putExtra("ratio", "1:1")
                    intent.putExtra("profile", "no")
                    intent.putExtra("hex", "no")
                    intent.putExtra("loadUserFrame", true)
                    this@MainActivity.startActivity(intent)
                }

                R.id.nav_poster -> {
                    intent = Intent(this@MainActivity, CreatePoster::class.java)
                    intent.putExtra("key", "poster")
                    intent.putExtra("ratio", "1:1")
                    intent.putExtra("profile", "no")
                    intent.putExtra("hex", "no")
                    intent.putExtra("loadUserFrame", true)
                    this@MainActivity.startActivity(intent)
                }

                R.id.nav_request_design -> {
                    val url = "https://form.jotform.com/212854219603353"
                    val uri = Uri.parse(url)
                    val rintent = Intent(Intent.ACTION_VIEW, uri)
                    if (rintent.resolveActivity(this@MainActivity.packageManager) != null) {
                        startActivity(rintent)
                    }
                }

                R.id.nav_rate_us -> startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
                    )
                )

                R.id.nav_about_us -> {
                    val dialog = Dialog(this@MainActivity)
                    dialog.setContentView(R.layout.about_us_dialog_layout)
                    val dialogButton = dialog.findViewById<View>(R.id.closeDialog) as Button
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }

                R.id.nav_share -> {
                    val sendIntent = Intent()
                    sendIntent.setAction(Intent.ACTION_SEND)
                    sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Hey check out this awesome Business Card, Logo and Poster app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                    )
                    sendIntent.setType("text/plain")
                    startActivity(sendIntent)
                }

                R.id.nav_exit -> finish()
                R.id.nav_remove_ads ->
                    startActivity(
                        Intent(
                            this@MainActivity,
                            SubscribePremium::class.java
                        )
                    )
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true

        }




        val fullScreenDialog = FullScreenDialog(this, viewModel = viewModel)
        if (LaunchManager.isFirstLaunchInSession) {
            fullScreenDialog.show()
            LaunchManager.isFirstLaunchInSession = false // Mark as no longer the first launch
        }

        val adsUrls = arrayOf(
            getString(R.string.bnr_admob),  // 1st Banner Ads Id
            getString(R.string.native_admob),  // 2st Native Ads Id
            getString(R.string.int_admob),  // 3st interstitial Ads Id
            getString(R.string.app_open_admob),  // 4st App-Open Ads Id
            getString(R.string.video_admob) // 5st Rewarded Ads Id
        )

        GetSmartAdmob(this, adsUrls, { success: Boolean ->
            success
        }).execute()

        //add admob banner
        GoogleAds.getInstance().admobBanner(this, findViewById(R.id.adView))


        try {
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            builder.detectFileUriExposure()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        this.editor = getSharedPreferences("MY_PREFS_NAME", 0).edit()
        this.prefs = getSharedPreferences("MY_PREFS_NAME", 0)

        if (Build.VERSION.SDK_INT >= 23 && !((checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED) /*&& (checkSelfPermission(
                "android.permission.READ_EXTERNAL_STORAGE"
            ) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED)*/
                    )
        ) {
            permissionDialog()
        }

        this.ttf = Constants.getHeaderTypeface(this)
        val ttfFirst = Constants.getFirstPageTypeface(this)
        this.ttfText = Constants.getTextTypeface(this)

        binding.contentMainInc.logoDesignLayout.setOnClickListener(this)
        binding.contentMainInc.posterDesignLayout.setOnClickListener(this)
        binding.contentMainInc.cardDesignLayout.setOnClickListener(this)
        binding.contentMainInc.saveProjectLayout.setOnClickListener(this)
        binding.contentMainInc.saveImageLayout.setOnClickListener(this)
        binding.contentMainInc.requestDesignLayout.setOnClickListener(this)
        binding.contentMainInc.privacyPolicy.setOnClickListener(this)

        binding.contentMainInc.privacyPolicy.typeface = ttfText

    }


    class FullScreenDialog(val activity: MainActivity, var viewModel: MainViewModel) : Dialog(activity,R.style.FullScreenDialogTheme) {
        private var _binding: FreeTrialDialogLayoutBinding? = null
        val binding get() = _binding!!
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE) // Remove title
            _binding = FreeTrialDialogLayoutBinding.inflate(layoutInflater)
            setContentView(binding.root)    // Set your custom layout
            window?.apply {
                setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
                setWindowAnimations(android.R.style.Animation_Dialog) // Optional animations
            }

            viewModel.subscriptionPrice.observe(activity) { updatePrice ->
                updatePrice ?: "Loading..."
                Log.d("MainActivity",updatePrice)
                binding.prictText.text = "Subscription Price: ${updatePrice}/month"
            }



            binding.btnSubscribe.setOnClickListener {
                dismiss() // Close the dialog
                val intent = Intent(context, SubscribePremium::class.java)
                context.startActivity(intent)
            }

            // Set up the Cancel button
            binding.btnCancel.setOnClickListener {
                dismiss() // Close the dialog
            }

        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.main_share) {
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey check out this awesome Business Card, Logo and Poster app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
            )
            sendIntent.setType("text/plain")
            startActivity(sendIntent)

            return true
        }

        return super.onOptionsItemSelected(item) // Call the super method if no action was handled
    }


    override fun onResume() {
        super.onResume()
        if (viewModel.isPremiumActive.value == true) {
            findViewById<View>(R.id.adView).visibility = View.GONE
        }
    }


    override fun onClick(view: View) {

        when (view.id) {
            binding.contentMainInc.posterDesignLayout.id ->{
                GoogleAds.getInstance().showCounterInterstitialAd(this@MainActivity) {
                    editor?.putString("typeOfDesign","poster")?.commit()
                    intent = Intent(this, CreatePoster::class.java)
                    intent.putExtra("key","poster")
                    intent.putExtra("ratio", "1:1")
                    intent.putExtra("profile", "no")
                    intent.putExtra("hex", "no")
                    intent.putExtra("loadUserFrame", true)
                    startActivity(intent)
                }
                return
            }

            binding.contentMainInc.cardDesignLayout.id ->{
                GoogleAds.getInstance().showCounterInterstitialAd(this@MainActivity) {
                    editor?.putString("typeOfDesign","card")?.commit()
                    intent = Intent(this, CreatePoster::class.java)
                    intent.putExtra("key","card")
                    intent.putExtra("ratio", "1:1")
                    intent.putExtra("profile", "no")
                    intent.putExtra("hex", "no")
                    intent.putExtra("loadUserFrame", true)
                    startActivity(intent)
                }
                return
            }

            R.id.privacyPolicy -> {
                val ii = Intent("android.intent.action.VIEW")
                ii.setData(Uri.parse(getString(R.string.privacyPolicy)))
                startActivity(ii)
                return
            }

            R.id.logo_design_layout -> {
                editor?.putString("typeOfDesign","logo")?.commit()
                callActivity("1:1")
                return
            }

            R.id.save_image_layout -> {
                GoogleAds.getInstance().showCounterInterstitialAd(this@MainActivity) {
                    startActivity(Intent(this, SavedHistoryActivity::class.java))
                }
                return
            }

            R.id.save_project_layout -> {

                GoogleAds.getInstance().showCounterInterstitialAd(this@MainActivity) {
                    startActivity(
                        Intent(
                            this@MainActivity, TemplatesActivity::class.java
                        )
                    )
                }
                return

            }

            R.id.request_design_layout -> {
                val url = "https://github.com/sponsors/HasaniaSoft-Pvt-Ltd/"
                val i1 = Intent("android.intent.action.VIEW")
                i1.setData(Uri.parse(url))
                startActivity(i1)
                return
            }

            else -> return
        }
    }


    private fun callActivity(ratio: String) {
        GoogleAds.getInstance().showCounterInterstitialAd(this) {
            val intent: Intent = Intent(this@MainActivity, CreatePoster::class.java)
            intent.putExtra("ratio", ratio)
            intent.putExtra("profile", "no")
            intent.putExtra("hex", "no")
            intent.putExtra("loadUserFrame", true)
            startActivity(intent)
        }
    }


    fun createShortCut() {
        val shortIntent = Intent("com.android.launcher.action.INSTALL_SHORTCUT")
        shortIntent.putExtra("duplicate", false)
        shortIntent.putExtra("android.intent.extra.shortcut.NAME", getString(R.string.app_name))
        shortIntent.putExtra(
            "android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(
                applicationContext, R.mipmap.ic_launcher
            )
        )
        shortIntent.putExtra(
            "android.intent.extra.shortcut.INTENT", Intent(
                applicationContext, MainActivity::class.java
            )
        )
        sendBroadcast(shortIntent)
    }

    override fun onBackPressed() {
        exitAlertDialog()
    }

    fun exitAlertDialog() {
        val dialog = Dialog(this/*, 16974126*/)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.exitalert_dialog)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = -1
        lp.height = -2
        try {
            (dialog.findViewById<View>(R.id.adView) as RelativeLayout).addView(this.mAdView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        (dialog.findViewById<View>(R.id.header_text) as TextView).typeface = this.ttf
        (dialog.findViewById<View>(R.id.msg) as TextView).typeface = this.ttfText
        val btn_no = dialog.findViewById<View>(R.id.btn_no) as Button
        val btn_yes = dialog.findViewById<View>(R.id.btn_yes) as Button
        btn_no.typeface = ttfText
        btn_no.setOnClickListener { dialog.dismiss() }
        btn_yes.typeface = ttfText
        btn_yes.setOnClickListener {
            this@MainActivity.finish()
            System.exit(0)
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            GoogleAds.getInstance().admobBanner(this@MainActivity, findViewById(R.id.adView))
        }

        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.show()
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(2)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if (requestCode == 101 && Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission("android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) || (checkSelfPermission(
                    "android.permission.READ_EXTERNAL_STORAGE"
                ) != PackageManager.PERMISSION_GRANTED) || (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)
            ) {
                permissionDialog()
            }
        }
    }

    fun permissionDialog() {
        val dialog = Dialog(this/*, 16974128*/)
        dialog.setContentView(R.layout.permissionsdialog)
        dialog.setTitle(resources.getString(R.string.permission).toString())
        dialog.setCancelable(false)
        (dialog.findViewById<View>(R.id.ok) as Button).setOnClickListener(
            View.OnClickListener {
                this@MainActivity.requestPermissions(
                    arrayOf(
                        "android.permission.CAMERA",
                        /* "android.permission.READ_EXTERNAL_STORAGE",
                         "android.permission.WRITE_EXTERNAL_STORAGE"*/
                    ), 100
                )
                dialog.dismiss()
            })
        if (this.isOpenFisrtTime) {
            val setting = dialog.findViewById<View>(R.id.settings) as Button
            setting.visibility = View.VISIBLE
            setting.setOnClickListener(object : View.OnClickListener {
                @SuppressLint("WrongConstant")
                override fun onClick(v: View) {
                    val intent = Intent(
                        "android.settings.APPLICATION_DETAILS_SETTINGS",
                        Uri.fromParts("package", this@MainActivity.packageName, null)
                    )
                    intent.addFlags(SQLiteDatabase.CREATE_IF_NECESSARY)
                    this@MainActivity.startActivityForResult(intent, 101)
                    dialog.dismiss()
                }
            })
        }
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != 100) {
            return
        }
        if (grantResults[0] == 0) {
            if (Build.VERSION.SDK_INT < 23) {
                return
            }
            if ((checkSelfPermission("android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED)
            /* || (checkSelfPermission(
                 "android.permission.READ_EXTERNAL_STORAGE"
             ) != PackageManager.PERMISSION_GRANTED) || (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)*/
            ) {
                this.isOpenFisrtTime = true
                permissionDialog()
            }
        } else if (Build.VERSION.SDK_INT < 23) {
        } else {
            if ((checkSelfPermission("android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) /*|| (checkSelfPermission(
                    "android.permission.READ_EXTERNAL_STORAGE"
                ) != PackageManager.PERMISSION_GRANTED) || (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)*/
            ) {
                this.isOpenFisrtTime = true
                permissionDialog()
            }
        }
    }


    private val isNetworkAvailable: Boolean
        get() {
            val activeNetworkInfo =
                (getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("LOG", "MainActivity onDestroy")
        _binding = null
    }


    companion object {
        private val PERMISSIONS_REQUEST = 100
        private val REQUEST_PERMISSION = 101
    }
}
