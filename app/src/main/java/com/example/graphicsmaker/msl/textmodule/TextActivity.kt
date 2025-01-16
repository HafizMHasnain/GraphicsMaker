package com.example.graphicsmaker.msl.textmodule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ListAdapter
import android.widget.SeekBar
import android.widget.Toast
import com.example.graphicsmaker.R
import com.example.graphicsmaker.databinding.ActivityTextBinding
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener

class TextActivity : Activity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private var _binding: ActivityTextBinding? = null
    val binding get() = _binding!!

    private var bgAlpha = 255
    private var bgColor = 0
    private var bgDrawable = "0"

    private var bundle: Bundle? = null
    var clickedView: View? = null
    var firsttime: Boolean = true
    private var fontName = ""
    var hex: String = ""
    var hex1: String = ""
    var imageId: IntArray = intArrayOf(
        R.drawable.btxt0,
        R.drawable.btxt1,
        R.drawable.btxt2,
        R.drawable.btxt3,
        R.drawable.btxt4,
        R.drawable.btxt5,
        R.drawable.btxt6,
        R.drawable.btxt7,
        R.drawable.btxt8,
        R.drawable.btxt9,
        R.drawable.btxt10,
        R.drawable.btxt11,
        R.drawable.btxt12,
        R.drawable.btxt13,
        R.drawable.btxt14,
        R.drawable.btxt15,
        R.drawable.btxt16,
        R.drawable.btxt17,
        R.drawable.btxt18,
        R.drawable.btxt19,
        R.drawable.btxt20,
        R.drawable.btxt21,
        R.drawable.btxt22,
        R.drawable.btxt23,
        R.drawable.btxt24
    )
    private var imm: InputMethodManager? = null
    private var isKbOpened = true
    var pallete: Array<String> = arrayOf(
        "#4182b6",
        "#4149b6",
        "#7641b6",
        "#b741a7",
        "#c54657",
        "#d1694a",
        "#24352a",
        "#b8c847",
        "#67bb43",
        "#41b691",
        "#293b2f",
        "#1c0101",
        "#420a09",
        "#b4794b",
        "#4b86b4",
        "#93b6d2",
        "#72aa52",
        "#67aa59",
        "#fa7916",
        "#16a1fa",
        "#165efa",
        "#1697fa"
    )
    var processValue: Int = 100
    private var shadowColor = Color.parseColor("#7641b6")
    private var shadowProg = 5
    private var tAlpha = 100
    private var tColor = Color.parseColor("#4149b6")
    private var text = ""
    private var textGravity = "C"
    private var ttf: Typeface? = null
    var value: Int = 0
    var value2: Int = 0
    private var xpos = 0f
    private var ypos = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTextBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.ttf = Typeface.createFromAsset(assets, "SANSATION_BOLD.TTF")
        this.imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        initUI()
        initViewPager()
        binding.layBackTxt.post {
           initUIData()
            binding.laykeyboard.performClick()
        }
        binding.headertext.typeface = this.ttf
        binding.autoFitEditText.viewTreeObserver.addOnGlobalLayoutListener {
            if (isKeyboardShown(binding.autoFitEditText.rootView)) {
                binding.layBelow.visibility = View.GONE
                setSelected(R.id.laykeyboard)
                firsttime = false
            } else if (!firsttime) {
                setSelected(clickedView!!.id)
                clickedView!!.performClick()
            }
        }
    }

    private fun isKeyboardShown(rootView: View): Boolean {
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        return ((rootView.bottom - r.bottom).toFloat()) > 128.0f * rootView.resources.displayMetrics.density
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun initUI() {

        this.clickedView =binding.layTxtfont
        binding.icKb.setOnClickListener(this)
binding.seekBar1.progress = this.processValue
        binding.autoFitEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 0) {
                    binding.hintTxt.visibility = View.VISIBLE
                } else {
                    binding.hintTxt.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {
            }

        })
        binding.txtBgNone.setOnClickListener(this)
        binding.listview.adapter = ImageViewAdapter(this, this.imageId)
        binding.listview.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                binding.layBackTxt.visibility = View.GONE
                val mDrawableName = "btxt$position"
                val resID = resources.getIdentifier(
                    mDrawableName,
                    "drawable",
                    packageName
                )
                bgDrawable = mDrawableName
                bgColor = 0
                binding.layBackTxt.setImageBitmap(
                    getTiledBitmap(
                        this@TextActivity, resID,
                        binding.autoFitEditText.width,
                        binding.autoFitEditText.height
                    )
                )
            }
        binding.colorPicker3.setOnClickListener(
            View.OnClickListener {
                AmbilWarnaDialog(
                    this@TextActivity,
                    bgColor,
                    object : OnAmbilWarnaListener {
                        override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                            bgDrawable = "0"
                            bgColor = color
                            binding.layBackTxt.setImageBitmap(null)
                            binding.layBackTxt.setBackgroundColor(bgColor)
                            binding.layBackTxt.visibility = View.GONE
                        }

                        override fun onCancel(dialog: AmbilWarnaDialog) {
                        }
                    }).show()
            })
        binding.colorPicker2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                AmbilWarnaDialog(
                    this@TextActivity,
                    shadowColor,
                    object : OnAmbilWarnaListener {
                        override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                            updateColor(color, 2)
                        }

                        override fun onCancel(dialog: AmbilWarnaDialog) {
                        }
                    }).show()
            }
        })
        binding.colorPicker1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                AmbilWarnaDialog(
                    this@TextActivity,
                    tColor,
                    object : OnAmbilWarnaListener {
                        override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                            updateColor(color, 1)
                        }

                        override fun onCancel(dialog: AmbilWarnaDialog) {
                        }
                    }).show()
            }
        })

        val colorListAdapter3: ListAdapter = ColorListAdapter(this, this.pallete)
        binding.colorListview3.adapter = colorListAdapter3
        binding.colorListview3.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                bgDrawable = "0"
                bgColor = (colorListAdapter3.getItem(position) as Int)
                binding.layBackTxt.setImageBitmap(null)
                binding.layBackTxt.setBackgroundColor(bgColor)
                binding.layBackTxt.visibility = View.GONE
            }
        }

        val colorListAdapter2: ListAdapter = ColorListAdapter(
            this, this.pallete
        )
        binding.colorListview2.adapter = colorListAdapter2
        binding.colorListview2.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                updateColor((colorListAdapter2.getItem(position) as Int), 2)
            }
        }

        val colorListAdapter1: ListAdapter = ColorListAdapter(
            this, this.pallete
        )
        binding.colorListview1.adapter = colorListAdapter1
        binding.colorListview1.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                updateColor((colorListAdapter1.getItem(position) as Int), 1)
            }
        }
        binding.btnBack.setOnClickListener(this)
        binding.btnOk.setOnClickListener(this)
        binding.laykeyboard.setOnClickListener(this)
        binding.layTxtfont.setOnClickListener(this)
        binding.layTxtcolor.setOnClickListener(this)
        binding.layTxtshadow.setOnClickListener(this)
        binding.layTxtbg.setOnClickListener(this)
        binding.seekBar1.setOnSeekBarChangeListener(this)
        binding.seekBar2.setOnSeekBarChangeListener(this)
        binding.seekBar3.setOnSeekBarChangeListener(this)
        binding.seekBar2.progress = 5
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(binding.autoFitEditText, 0)
    }

    private fun initViewPager() {
        val adapter = AssetsGrid(this, resources.getStringArray(R.array.fonts_array))
        binding.fontGridview.adapter = adapter
        binding.fontGridview.onItemClickListener = object:AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

        }
    }

    private fun initUIData() {
        bundle = intent.extras
        if (bundle != null) {
            xpos = bundle!!.getFloat("X", 0.0f)
            ypos = bundle!!.getFloat("Y", 0.0f)
            text = bundle!!.getString("text", "")
            fontName = bundle!!.getString("fontName", "")
            tColor = bundle!!.getInt("tColor", Color.parseColor("#4149b6"))
            tAlpha = bundle!!.getInt("tAlpha", 100)
            shadowColor =
                bundle!!.getInt("shadowColor", Color.parseColor("#7641b6"))
            shadowProg = bundle!!.getInt("shadowProg", 5)
            bgDrawable = bundle!!.getString("bgDrawable", "0")
            bgColor = bundle!!.getInt("bgColor", 0)
            bgAlpha = bundle!!.getInt("bgAlpha", 255)
            textGravity = bundle!!.getString("gravity", "")
            binding.autoFitEditText.setText(this.text)
            binding.autoFitEditText.adjustTextGravity(this.textGravity)
            binding.seekBar1.progress = this.tAlpha
            binding.seekBar2.progress = this.shadowProg
            updateColor(tColor, 1)
            updateColor(shadowColor, 2)
            if (bgDrawable != "0") {
                binding.layBackTxt.setImageBitmap(
                    getTiledBitmap(
                        this, resources.getIdentifier(this.bgDrawable, "drawable", packageName),
                        binding.autoFitEditText.width,
                        binding.autoFitEditText.height
                    )
                )
                binding.layBackTxt.visibility = View.VISIBLE
                binding.layBackTxt.postInvalidate()
                binding.layBackTxt.requestLayout()
            }
            if (bgColor != 0) {
                binding.layBackTxt.setBackgroundColor(this.bgColor)
                binding.layBackTxt.visibility = View.VISIBLE
            }
            binding.seekBar3.progress = this.bgAlpha
            try {
                binding.autoFitEditText.typeface = Typeface.createFromAsset(
                    assets, this.fontName
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.btn_back) {
            imm!!.hideSoftInputFromWindow(v.applicationWindowToken, 0)
            finish()
        } else if (i == R.id.btn_ok) {
            if (binding.autoFitEditText.text.toString().trim { it <= ' ' }.length > 0) {
                imm!!.hideSoftInputFromWindow(v.applicationWindowToken, 0)
                val intent = Intent()
                intent.putExtras((infoBundle)!!)
                setResult(-1, intent)
                finish()
                return
            }
            Toast.makeText(
                this,
                resources.getString(R.string.textlib_warn_text),
                Toast.LENGTH_SHORT
            ).show()
        } else if (i == R.id.laykeyboard || i == R.id.ic_kb) {
            isKbOpened = true
            firsttime = true
            setSelected(v.id)
            imm!!.showSoftInput(binding.autoFitEditText, 0)
        } else if (i == R.id.lay_txtfont) {
            setSelected(v.id)
            clickedView = v
            binding.fontGridRel.visibility = View.GONE
            binding.colorRel.visibility = View.GONE
            binding.shadowRel.visibility = View.GONE
            binding.bgRel.visibility = View.GONE
            binding.layBelow.visibility = View.GONE
            imm!!.hideSoftInputFromWindow(v.applicationWindowToken, 0)
        } else if (i == R.id.lay_txtcolor) {
            setSelected(v.id)
            clickedView = v
            binding.fontGridRel.visibility = View.GONE
            binding.colorRel.visibility = View.GONE
            binding.shadowRel.visibility = View.GONE
            binding.bgRel.visibility = View.GONE
            binding.layBelow.visibility = View.GONE
            imm!!.hideSoftInputFromWindow(v.applicationWindowToken, 0)
        } else if (i == R.id.lay_txtshadow) {
            setSelected(v.id)
            clickedView = v
            binding.fontGridRel.visibility = View.GONE
            binding.colorRel.visibility = View.GONE
            binding.shadowRel.visibility = View.GONE
            binding.bgRel.visibility = View.GONE
            binding.layBelow.visibility = View.GONE
            imm!!.hideSoftInputFromWindow(v.applicationWindowToken, 0)
        } else if (i == R.id.lay_txtbg) {
            setSelected(v.id)
            clickedView = v
            binding.fontGridRel.visibility = View.GONE
            binding.colorRel.visibility = View.GONE
            binding.shadowRel.visibility = View.GONE
            binding.bgRel.visibility = View.GONE
            binding.layBelow.visibility = View.GONE
            imm!!.hideSoftInputFromWindow(v.applicationWindowToken, 0)
        } else if (i == R.id.txt_bg_none) {
            binding.layBackTxt.visibility = View.GONE
            bgDrawable = "0"
            bgColor = 0
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        this.processValue = progress
        this.value = progress
        val i = seekBar.id
        if (i == R.id.seekBar1) {
            binding.autoFitEditText.alpha = (seekBar.progress.toFloat()) / (seekBar.max.toFloat())
        } else if (i == R.id.seekBar2) {
            if (hex1 == "") {
                binding.autoFitEditText.setShadowLayer(
                    progress.toFloat(),
                    0.0f,
                    0.0f,
                    Color.parseColor("#fdab52")
                )
            } else {
                binding.autoFitEditText.setShadowLayer(
                    progress.toFloat(),
                    0.0f,
                    0.0f,
                    Color.parseColor("#" + hex1)
                )
            }
        } else if (i == R.id.seekBar3) {
            binding.layBackTxt.alpha = (progress.toFloat()) / 255.0f
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

    private val infoBundle: Bundle? get() {
            if (this.bundle == null) {
                this.bundle = Bundle()
            }
            this.text = binding.autoFitEditText.text.toString().trim { it <= ' ' }.replace("\n", " ")
            bundle!!.putFloat("X", this.xpos)
            bundle!!.putFloat("Y", this.ypos)
            bundle!!.putString("text", this.text)
            bundle!!.putString("fontName", this.fontName)
            bundle!!.putInt("tColor", this.tColor)
            bundle!!.putInt("tAlpha", binding.seekBar1.progress)
            bundle!!.putInt("shadowColor", this.shadowColor)
            bundle!!.putInt("shadowProg", binding.seekBar2.progress)
            bundle!!.putString("bgDrawable", this.bgDrawable)
            bundle!!.putInt("bgColor", this.bgColor)
            bundle!!.putInt("bgAlpha", binding.seekBar3.progress)
            bundle!!.putString("gravity", this.textGravity)
            return bundle
        }

    private fun updateColor(color: Int, i: Int) {
        if (i == 1) {
            this.tColor = color
            this.hex = Integer.toHexString(color)
            binding.autoFitEditText.setTextColor(Color.parseColor("#" + this.hex))
        } else if (i == 2) {
            this.shadowColor = color
            val progress = binding.seekBar2.progress
            this.hex1 = Integer.toHexString(color)
            binding.autoFitEditText.setShadowLayer(
                progress.toFloat(),
                0.0f,
                0.0f,
                Color.parseColor("#" + this.hex1)
            )
        }
    }

    fun setSelected(id: Int) {
        if (id == R.id.laykeyboard) {
            binding.laykeyboard.getChildAt(0).setBackgroundResource(R.drawable.textlib_kb1)
            binding.layTxtfont.getChildAt(0).setBackgroundResource(R.drawable.textlib_text)
            binding.layTxtcolor.getChildAt(0).setBackgroundResource(R.drawable.textlib_tcolor)
            binding.layTxtshadow.getChildAt(0).setBackgroundResource(R.drawable.textlib_tshadow)
            binding.layTxtbg.getChildAt(0).setBackgroundResource(R.drawable.textlib_tbg)
        }
        if (id == R.id.lay_txtfont) {
            binding.laykeyboard.getChildAt(0).setBackgroundResource(R.drawable.textlib_kb)
            binding.layTxtfont.getChildAt(0).setBackgroundResource(R.drawable.textlib_text1)
            binding.layTxtcolor.getChildAt(0).setBackgroundResource(R.drawable.textlib_tcolor)
            binding.layTxtshadow.getChildAt(0).setBackgroundResource(R.drawable.textlib_tshadow)
            binding.layTxtbg.getChildAt(0).setBackgroundResource(R.drawable.textlib_tbg)
        }
        if (id == R.id.lay_txtcolor) {
            binding.laykeyboard.getChildAt(0).setBackgroundResource(R.drawable.textlib_kb)
            binding.layTxtfont.getChildAt(0).setBackgroundResource(R.drawable.textlib_text)
            binding.layTxtcolor.getChildAt(0).setBackgroundResource(R.drawable.textlib_tcolor1)
            binding.layTxtshadow.getChildAt(0).setBackgroundResource(R.drawable.textlib_tshadow)
            binding.layTxtbg.getChildAt(0).setBackgroundResource(R.drawable.textlib_tbg)
        }
        if (id == R.id.lay_txtshadow) {
            binding.laykeyboard.getChildAt(0).setBackgroundResource(R.drawable.textlib_kb)
            binding.layTxtfont.getChildAt(0).setBackgroundResource(R.drawable.textlib_text)
            binding.layTxtcolor.getChildAt(0).setBackgroundResource(R.drawable.textlib_tcolor)
            binding.layTxtshadow.getChildAt(0).setBackgroundResource(R.drawable.textlib_tshadow1)
            binding.layTxtbg.getChildAt(0).setBackgroundResource(R.drawable.textlib_tbg)
        }
        if (id == R.id.lay_txtbg) {
            binding.laykeyboard.getChildAt(0).setBackgroundResource(R.drawable.textlib_kb)
            binding.layTxtfont.getChildAt(0).setBackgroundResource(R.drawable.textlib_text)
            binding.layTxtcolor.getChildAt(0).setBackgroundResource(R.drawable.textlib_tcolor)
            binding.layTxtshadow.getChildAt(0).setBackgroundResource(R.drawable.textlib_tshadow)
            binding.layTxtbg.getChildAt(0).setBackgroundResource(R.drawable.textlib_tbg1)
        }
    }

    private fun getTiledBitmap(ctx: Context, resId: Int, width: Int, height: Int): Bitmap {
        val rect = Rect(0, 0, width, height)
        val paint = Paint()
        paint.setShader(
            BitmapShader(
                BitmapFactory.decodeResource(
                    ctx.resources,
                    resId,
                    BitmapFactory.Options()
                ), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT
            )
        )
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        Canvas(b).drawRect(rect, paint)
        return b
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
