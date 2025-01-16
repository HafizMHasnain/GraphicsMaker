package com.example.graphicsmaker.sticker_fragment

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.graphicsmaker.R
import com.example.graphicsmaker.adapter.CatalogAdapter
import com.example.graphicsmaker.main.Constants
import com.example.graphicsmaker.main.OnGetImageOnTouch
import com.example.graphicsmaker.premium.SubscriptionViewModel
import com.example.graphicsmaker.utility.MyGridView

class FragmentPoster() : Fragment() {
    val viewModel: SubscriptionViewModel by viewModels<SubscriptionViewModel>()
    var ch: Boolean = false
    var getImage: OnGetImageOnTouch? = null
    private var gridView: MyGridView? = null
    var pos: Int = 0
    private var ttf: Typeface? = null
    private var ttfHeader: Typeface? = null
    private val lock: List<Int> = mutableListOf(3, 5, 9, 15, 19)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_poster, container, false)

        this.ttfHeader = Constants.getHeaderTypeface(
            requireActivity()
        )
        this.ttf = Constants.getTextTypeface(requireActivity())
        this.getImage = activity as OnGetImageOnTouch?
        this.gridView = view.findViewById<View>(R.id.catalog) as MyGridView
        gridView!!.adapter =
            CatalogAdapter(
                requireActivity(),
                R.layout.single_poster_catalog,
                Constants.posters,
                lock,viewModel
            )
        gridView!!.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (lock.contains(position)) {
                  showPremiumDialog(position)
                }
                else {
                    getImage!!.ongetPosition(
                        Constants.posters[position],
                        "Poster",
                        "",
                        null,
                        null,
                        "",
                        0,
                        "",
                        "hideVideo",
                        false
                    )
                }
            }
        }
        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    fun showPremiumDialog(position: Int) {
        val dialog = Dialog(requireActivity()/*, 16974126*/)
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
               remove_watermark_video_dialog(position)
                dialog.dismiss()
            }
        })
        lp.dimAmount = 0.7f
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_
        dialog.show()
        dialog.window!!.attributes = lp
        dialog.window!!.addFlags(2)
    }

    override fun onResume() {
        super.onResume()
        if (this.ch) {
            remove_watermark_video_dialog(this.pos)
            this.ch = false
        }
    }

    fun remove_watermark_video_dialog(position: Int) {
        val dialog = Dialog(requireActivity()/*, 16974126*/)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.remove_watermark_vidadv_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = -1
        lp.height = -2
        val header_text = dialog.findViewById<View>(R.id.headertext) as TextView
        header_text.text = requireActivity().resources.getString(R.string.useart)
        header_text.typeface = ttfHeader
        val remove_watermark_msg = dialog.findViewById<View>(R.id.remove_watermark_msg) as TextView
        remove_watermark_msg.text = requireActivity().resources.getString(R.string.use_texture)
        remove_watermark_msg.typeface = ttf
        val no_thanks = dialog.findViewById<View>(R.id.no_thanks) as Button
        no_thanks.typeface = ttf
        no_thanks.setOnClickListener { dialog.dismiss() }
        val watch_ad = dialog.findViewById<View>(R.id.watch_ad) as Button
        watch_ad.text = requireActivity().resources.getString(R.string.watchnow)
        watch_ad.typeface = ttf
        watch_ad.setOnClickListener {
            dialog.dismiss()
            getImage!!.ongetPosition(
                Constants.posters[position],
                "Poster",
                "",
                null,
                null,
                "",
                0,
                "",
                "showVideo",
                false
            )
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
                    Glide.get((requireActivity())).clearDiskCache()
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }.start()
            Glide.get(requireActivity()).clearMemory()
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        } catch (e2: Exception) {
            e2.printStackTrace()
        }

        Constants.freeMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.gridView = null
        Constants.freeMemory()
    }
}
