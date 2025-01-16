package com.example.graphicsmaker.sticker_fragment

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.graphicsmaker.R
import com.example.graphicsmaker.main.Constants
import vocsy.ads.GoogleAds

class StickersFragment : Fragment() {
    var adapter: StickerGrid? = null
    var colorType: String? = null
    var grid: GridView? = null
    var onGetSnap: GetSnapListener? = null
    var remove_ad_pref: SharedPreferences? = null
    var stkrName: Array<String>? = null
    private var ttf: Typeface? = null
    private var ttfHeader: Typeface? = null
    private val lock = listOf(2,5,7,9,13,16)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_frag, container, false)
        this.remove_ad_pref = PreferenceManager.getDefaultSharedPreferences(
            requireActivity()
        )
        val cataName = arguments?.getString("cataName")

        if ((cataName == "cam")) {
            this.stkrName = Constants.camera_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "vid")) {
            this.stkrName = Constants.video_stkr_list
            this.colorType = "colored"
        } else if ((cataName == NotificationCompat.CATEGORY_SOCIAL)) {
            this.stkrName = Constants.social_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "lef")) {
            this.stkrName = Constants.leaf_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "corp")) {
            this.stkrName = Constants.corporate_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "pro")) {
            this.stkrName = Constants.property_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "sport")) {
            this.stkrName = Constants.sport_stkr_list
            this.colorType = "white"
        } else if ((cataName == "rest")) {
            this.stkrName = Constants.restaurant_cafe_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "cir")) {
            this.stkrName = Constants.circle_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "squre")) {
            this.stkrName = Constants.square_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "butter")) {
            this.stkrName = Constants.butterfly_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "cars")) {
            this.stkrName = Constants.cars_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "music")) {
            this.stkrName = Constants.music_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "party")) {
            this.stkrName = Constants.party_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "ngo")) {
            this.stkrName = Constants.ngo_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "festi")) {
            this.stkrName = Constants.festival_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "tattoo")) {
            this.stkrName = Constants.tattoo_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "flower")) {
            this.stkrName = Constants.flower_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "star")) {
            this.stkrName = Constants.star_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "heart")) {
            this.stkrName = Constants.heart_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "hallow")) {
            this.stkrName = Constants.halloween_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "holi")) {
            this.stkrName = Constants.holiday_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "toys")) {
            this.stkrName = Constants.toys_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "animals")) {
            this.stkrName = Constants.animal_bird_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "text")) {
            this.stkrName = Constants.text_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "shape")) {
            this.stkrName = Constants.shape_stkr_list
            this.colorType = "white"
        } else if ((cataName == "badge")) {
            this.stkrName = Constants.badge_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "bakery")) {
            this.stkrName = Constants.bakery_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "ribbon")) {
            this.stkrName = Constants.ribbon_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "icon")) {
            this.stkrName = Constants.icon_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "payment")) {
            this.stkrName = Constants.payment_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "beauty")) {
            this.stkrName = Constants.beauty_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "bistro")) {
            this.stkrName = Constants.bistro_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "profession")) {
            this.stkrName = Constants.profession_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "people")) {
            this.stkrName = Constants.people_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "christianity")) {
            this.stkrName = Constants.christianity_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "pets")) {
            this.stkrName = Constants.pets_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "letters")) {
            this.stkrName = Constants.letters_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "babymom")) {
            this.stkrName = Constants.babymom_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "fashion")) {
            this.stkrName = Constants.fasion_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "business")) {
            this.stkrName = Constants.business_stkr_list
            this.colorType = "colored"
        } else if ((cataName == "threed")) {
            this.stkrName = Constants.threed_stkr_list
            this.colorType = "colored"
        }
        this.ttfHeader = Constants.getHeaderTypeface(
            requireActivity()
        )
        this.ttf = Constants.getTextTypeface(requireActivity())
        this.adapter = StickerGrid(requireActivity(), this.stkrName!!, this.remove_ad_pref!!,lock)
        this.onGetSnap = requireActivity() as GetSnapListener

        this.grid = view.findViewById<View>(R.id.grid) as GridView
        grid!!.adapter = this.adapter
        grid!!.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                if (lock.contains(position)) {
                    pos = position
                    cur_stkrName = stkrName
                    cur_colorType = colorType
                    showPremiumDialog(position)

                } else if (remove_ad_pref?.getBoolean("isAdsDisabled", false) == true) {
                    onGetSnap!!.onSnapFilter(
                        stkrName!![position],
                        this@StickersFragment.colorType,
                        ""
                    )
                } else {
                    onGetSnap!!.onSnapFilter(
                        stkrName!![position],
                        this@StickersFragment.colorType,
                        ""
                    )
                }
            }
        return view
    }

    override fun setMenuVisibility(visible: Boolean) {
        super.setMenuVisibility(visible)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    private fun showPremiumDialog(position: Int) {
        val dialog = Dialog(requireActivity()/*, 16974126*/)
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
                val pakagename = activity?.packageName
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

        dialog.findViewById<View>(R.id.no_thanks).setOnClickListener {
            this@StickersFragment.remove_watermark_video_dialog()
            dialog.dismiss()
        }
        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.show()
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(2)
    }

    fun remove_watermark_video_dialog() {
        val dialog = Dialog(requireActivity()/*, 16974126*/)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.remove_watermark_vidadv_dialog)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = -1
        lp.height = -2
        val header_text = dialog.findViewById<View>(R.id.headertext) as TextView
        header_text.text = activity?.resources?.getString(R.string.useart) ?: ""
        header_text.typeface = ttfHeader
        val remove_watermark_msg = dialog.findViewById<View>(R.id.remove_watermark_msg) as TextView
        remove_watermark_msg.text = activity?.resources?.getString(R.string.useart_msg) ?: ""
        remove_watermark_msg.typeface = ttf
        val no_thanks = dialog.findViewById<View>(R.id.no_thanks) as Button
        no_thanks.typeface = ttf
        no_thanks.setOnClickListener { dialog.dismiss() }
        val watch_ad = dialog.findViewById<View>(R.id.watch_ad) as Button
        watch_ad.text = activity?.resources?.getString(R.string.watchnow) ?: ""
        watch_ad.typeface = ttf
        watch_ad.setOnClickListener {
            if (!remove_ad_pref!!.getBoolean("isAdsDisabled", false)) {
                GoogleAds.getInstance().showRewardedAd(activity) {
                    onGetSnap!!.onSnapFilter(
                        cur_stkrName!![pos], cur_colorType, ""
                    )
                }

                val dialog1 = Dialog(requireActivity()/*, 16974126*/)
                dialog1.requestWindowFeature(1)
                dialog1.setContentView(R.layout.interner_connection_dialog)
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

                if (!GoogleAds.checkConnection(requireActivity())) {
                    dialog1.window!!.attributes.windowAnimations = R.style.DialogAnimation_
                    dialog1.show()
                    dialog1.window!!.attributes = lp
                    dialog1.window!!.addFlags(2)
                }
            }

            dialog.dismiss()
        }
        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.show()
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
            this.grid = null
            this.adapter = null
            this.onGetSnap = null
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        } catch (e2: Exception) {
            e2.printStackTrace()
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
            this.grid = null
            this.adapter = null
            this.onGetSnap = null
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        } catch (e2: Exception) {
            e2.printStackTrace()
        }
        Constants.freeMemory()
    }

    companion object {
        var pos: Int = 0
        var cur_stkrName: Array<String>? = null
        var cur_colorType: String? = null
    }
}
