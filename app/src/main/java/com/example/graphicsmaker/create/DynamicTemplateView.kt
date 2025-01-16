package com.example.graphicsmaker.create

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.os.AsyncTask
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.graphicsmaker.R

class DynamicTemplateView : AppCompatImageView {
    var danim: AnimationDrawable? = null
    private var mContext: Context? = null
    private var mHeight = 0
    private var mTemplateInfo: TemplateInfo? = null
    private var mWidth = 0
    private var updateViewAsync: UpdateViewAsync? = UpdateViewAsync()
    private var viewAsyncState = ViewAsyncState.INITIALIZED

    internal inner class UpdateViewAsync : AsyncTask<Void?, Void?, Bitmap?>() {

        override fun onPostExecute(bitmap: Bitmap?) {
            super.onPostExecute(bitmap)
            if (bitmap != null) {
                this@DynamicTemplateView.setPadding(0, 0, 0, 0)
                this@DynamicTemplateView.setImageBitmap(bitmap)
            } else {
                this@DynamicTemplateView.setImageResource(R.drawable.no_image)
            }
            this@DynamicTemplateView.viewAsyncState = ViewAsyncState.COMPLETE
            danim!!.stop()
        }

        override fun onCancelled() {
            this@DynamicTemplateView.viewAsyncState = ViewAsyncState.DETACHEDFROMWINDOW
        }

        override fun doInBackground(vararg params: Void?): Bitmap? {
            this@DynamicTemplateView.viewAsyncState = ViewAsyncState.ISRUNNING
            try {
                return ViewTemplateCanvasFinal(
                    this@DynamicTemplateView.mContext,
                    (this@DynamicTemplateView.mWidth * 2).toFloat(),
                    (this@DynamicTemplateView.mHeight * 2).toFloat(),
                    mWidth.toFloat(),
                    mHeight.toFloat(),
                    this,
                    true,
                    160
                )
                    .getTemplateBitmap(mTemplateInfo!!.templatE_ID)
            }
            catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            catch (e2: Error) {
                e2.printStackTrace()
                return null
            }
        }
    }

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )

    fun setViewParams(context: Context?, width: Int, height: Int, templateInfo: TemplateInfo?) {
        this.mContext = context
        this.mWidth = width
        this.mHeight = height
        this.mTemplateInfo = templateInfo
    }

    fun startLoadingView() {
        if (this.viewAsyncState != ViewAsyncState.ISRUNNING && this.viewAsyncState != ViewAsyncState.COMPLETE) {
            setImageResource(R.drawable.anim)
            this.danim = drawable as AnimationDrawable
            danim!!.start()
            this.updateViewAsync = UpdateViewAsync()
            updateViewAsync!!.execute(*arrayOfNulls<Void>(0))
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (this.viewAsyncState != ViewAsyncState.ISRUNNING && this.viewAsyncState != ViewAsyncState.COMPLETE) {
            setImageResource(R.drawable.anim)
            this.danim = drawable as AnimationDrawable
            danim!!.start()
            this.updateViewAsync = UpdateViewAsync()
            updateViewAsync!!.execute(*arrayOfNulls<Void>(0))
            val mMargin = this.mWidth / 4
            setPadding(mMargin, mMargin, mMargin, mMargin)
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (this.updateViewAsync != null) {
            updateViewAsync!!.cancel(true)
//            updateViewAsync!!.onCancelled()
        }
        this.viewAsyncState = ViewAsyncState.CANCELLED
    }
}
