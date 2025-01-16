package com.example.graphicsmaker.sticker_fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.example.graphicsmaker.R
import com.example.graphicsmaker.create.DatabaseHandler.Companion.getDbHandler
import com.example.graphicsmaker.main.Constants
import com.example.graphicsmaker.msl.textmodule.AutoResizeTextView

class FragmentStepOne() : Fragment(), AdapterView.OnItemSelectedListener {
    var _indestryArr: ArrayList<String?> = ArrayList()
    var _indestryName: String? = ""
    var danim: AnimationDrawable? = null
    var editor: SharedPreferences.Editor? = null
    var img_drawable: ImageView? = null
    var img_parent: RelativeLayout? = null
    var onCallFragment: CallFragmentInterface? = null
    var prefs: SharedPreferences? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_one, container, false)
        this.editor = requireActivity().getSharedPreferences("MY_PREFS_NAME", 0).edit()
        this.prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", 0)
        this._indestryArr = getDbHandler(
            requireActivity()
        ).listOfIndustries
        this.onCallFragment = activity as CallFragmentInterface
        this.img_parent = view.findViewById<View>(R.id.img_parent) as RelativeLayout
        val spin = view.findViewById<View>(R.id.spinner) as Spinner
        val edt_companyName = view.findViewById<View>(R.id.edt_companyName) as EditText
        val edt_tagLine = view.findViewById<View>(R.id.edt_tagLine) as EditText
        edt_companyName.setText(prefs?.getString("companyName", null))
        edt_tagLine.setText(prefs?.getString("tagLine", null))
        hideKeyBoard()
        val aa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(requireActivity(), R.layout.simple_spinner_item,
                _indestryArr as List<Any?>
            )
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spin.adapter = aa
        spin.setOnTouchListener(View.OnTouchListener { v, event ->
            this@FragmentStepOne.hideKeyBoard()
            false
        })
        (view.findViewById<View>(R.id.btn_Next) as RelativeLayout).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View) {
                val _companyName = edt_companyName.text.toString()
                val _tagLine = edt_tagLine.text.toString()
                if (_companyName.isEmpty()) {
                    this@FragmentStepOne.showDialogCompany()
                } else if ((this@FragmentStepOne._indestryName == "")) {
                    Toast.makeText(
                        requireActivity(),
                        requireActivity().resources.getString(
                            R.string.txt_indestry
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    editor?.putString("indestryName", this@FragmentStepOne._indestryName)
                    editor?.putString("companyName", _companyName)
                    editor?.putString("tagLine", _tagLine)
                    editor?.commit()
                    onCallFragment!!.onCallFragment("step2", "")
                }
            }
        })
        spin.onItemSelectedListener = this
        for (i in _indestryArr.indices) {
            if ((_indestryArr[i] == prefs?.getString("indestryName", null))) {
                spin.setSelection(i)
            }
        }
        (view.findViewById<View>(R.id.lay_parent) as LinearLayout).setOnTouchListener(object :
            View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                this@FragmentStepOne.hideKeyBoard()
                return false
            }
        })
        (view.findViewById<View>(R.id.lay_scroll) as ScrollView).setOnTouchListener(object :
            View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                this@FragmentStepOne.hideKeyBoard()
                return false
            }
        })
        (view.findViewById<View>(R.id.lay_bellow) as RelativeLayout).setOnTouchListener(object :
            View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                this@FragmentStepOne.hideKeyBoard()
                return false
            }
        })
        edt_companyName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                editor?.putString("companyName", edt_companyName.text.toString())
                editor?.commit()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        edt_tagLine.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                editor?.putString("tagLine", edt_tagLine.text.toString())
                editor?.commit()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        this.img_drawable = view.findViewById<View>(R.id.img_drawable) as ImageView
        this.danim = img_drawable!!.drawable as AnimationDrawable
        danim!!.start()
        (view.findViewById<View>(R.id.txtIndestry) as TextView).setTypeface(
            Constants.getTextTypeface(
                requireActivity()
            )
        )
        (view.findViewById<View>(R.id.txtConpanyName) as TextView).setTypeface(
            Constants.getTextTypeface(
                requireActivity()
            )
        )
        (view.findViewById<View>(R.id.txtTagLine) as TextView).setTypeface(
            Constants.getTextTypeface(
                requireActivity()
            )
        )
        (view.findViewById<View>(R.id.txtNext) as TextView).setTypeface(
            Constants.getTextTypeface(
                requireActivity()
            )
        )
        val textHi = view.findViewById<View>(R.id.auto_fit_edit_text) as AutoResizeTextView
        val textCon = view.findViewById<View>(R.id.auto_fit_edit_text1) as AutoResizeTextView
        img_parent!!.post(object : Runnable {
            override fun run() {
                textHi.layoutParams.width = img_parent!!.height / 2
                textHi.layoutParams.height = img_parent!!.height / 5
                textHi.text = requireActivity().resources.getString(R.string.txtHi)
                textHi.setTypeface(
                    Typeface.createFromAsset(
                        requireActivity().assets,
                        "akifont4.ttf"
                    )
                )
                textHi.setTextColor(ViewCompat.MEASURED_STATE_MASK)
                textHi.textSize = 800.0f
                textHi.gravity = 3
                textHi.setMinTextSize(10.0f)
                textCon.layoutParams.width = img_parent!!.height / 2
                textCon.layoutParams.height = img_parent!!.height / 2
                textCon.text =
                    requireActivity().resources.getString(R.string.txtDesigner)
                textCon.setTypeface(
                    Typeface.createFromAsset(
                        requireActivity().assets,
                        "akifont4.ttf"
                    )
                )
                textCon.setTextColor(ViewCompat.MEASURED_STATE_MASK)
                textCon.textSize = 600.0f
                textCon.gravity = 3
                textCon.setMinTextSize(10.0f)
            }
        })
        return view
    }

    private fun hideKeyBoard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                view.windowToken,
                0
            )
        }
    }

    private fun showDialogCompany() {
        val dialog1 = Dialog(requireActivity() /*, 16974132*/)
        dialog1.requestWindowFeature(1)
        dialog1.setCancelable(false)
        dialog1.setContentView(R.layout.designer_dialog)
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        (dialog1.findViewById<View>(R.id.heater) as TextView).typeface =
            Constants.getHeaderTypeface(
                requireActivity()
            )
        val text = dialog1.findViewById<View>(R.id.txt_free) as TextView
        text.text = requireActivity().resources.getString(R.string.txtform1)
        text.typeface = Constants.getTextTypeface(requireActivity())
        val btn_ok = dialog1.findViewById<View>(R.id.btn_ok) as Button
        btn_ok.typeface = Constants.getTextTypeface(requireActivity())
        btn_ok.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                dialog1.dismiss()
            }
        })
        dialog1.show()
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, arg1: View, position: Int, id: Long) {
        this._indestryName = _indestryArr[position]
        editor!!.putString("indestryName", this._indestryName)
        editor!!.commit()
        hideKeyBoard()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            danim!!.stop()
            this.img_parent = null
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}
