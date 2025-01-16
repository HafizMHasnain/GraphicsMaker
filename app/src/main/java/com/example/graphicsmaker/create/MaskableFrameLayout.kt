package com.example.graphicsmaker.create

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import com.example.graphicsmaker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MaskableFrameLayout : FrameLayout {
    private var scaleFactor = 1.0f
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val gestureDetector = GestureDetector(context, GestureListener())

    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var translationXValue = 0f
    private var translationYValue = 0f

    private val dragJob = SupervisorJob()
    private val dragScope = CoroutineScope(Dispatchers.Main + dragJob)

    private var isLocked = false // Lock state
    private lateinit var lockIcon: ImageView


    private fun init() {
        // Add lock icon
        lockIcon = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }
            visibility = View.GONE
        }
        addView(lockIcon)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Detect gestures
        gestureDetector.onTouchEvent(event)

        // If locked, prevent interaction
        if (isLocked) return true

        // Handle scaling and dragging
        scaleGestureDetector.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.rawX
                lastTouchY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                if (!scaleGestureDetector.isInProgress) {
                    val dx = event.rawX - lastTouchX
                    val dy = event.rawY - lastTouchY

                    lastTouchX = event.rawX
                    lastTouchY = event.rawY

                    dragScope.launch {
                        handleDrag(dx, dy)
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                dragScope.coroutineContext.cancelChildren()
            }
        }
        return true
    }

    private suspend fun handleDrag(dx: Float, dy: Float) = withContext(Dispatchers.Default) {
        translationXValue += dx
        translationYValue += dy
        withContext(Dispatchers.Main) {
            ViewCompat.setTranslationX(this@MaskableFrameLayout, translationXValue)
            ViewCompat.setTranslationY(this@MaskableFrameLayout, translationYValue)
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (isLocked) return false
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.5f, 3.0f)

            ViewCompat.setScaleX(this@MaskableFrameLayout, scaleFactor)
            ViewCompat.setScaleY(this@MaskableFrameLayout, scaleFactor)
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            toggleLock()
            return true
        }
    }

    private fun toggleLock() {
        isLocked = !isLocked
        flashLockIcon(if (isLocked) R.drawable.ic_lock else R.drawable.ic_unlock)
    }

    private fun flashLockIcon(iconRes: Int) {
        lockIcon.setImageResource(iconRes)
        lockIcon.visibility = View.VISIBLE
        lockIcon.alpha = 1f
        lockIcon.animate()
            .alpha(0f)
            .setDuration(1000)
            .withEndAction { lockIcon.visibility = View.GONE }
            .start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dragJob.cancel()
    }


    private val mHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    //Mask props
    var drawableMask: Drawable? = null
        private set
    private var mFinalMask: Bitmap? = null
    private var currentMode: PorterDuff.Mode? = null

    //Drawing props
    private var mPaint: Paint? = null
    private var mPorterDuffXferMode: PorterDuffXfermode? = null

    constructor(context: Context?) : super(context!!){
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        construct(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        construct(context, attrs)
        init()
    }

    private fun construct(context: Context, attrs: AttributeSet?) {
//        mHandler = Handler(Looper.getMainLooper())
        isDrawingCacheEnabled = true
        setLayerType(LAYER_TYPE_SOFTWARE, null) //Only works for software layers
        mPaint = createPaint(false)
        val theme = context.theme
        if (theme != null) {
            val a = theme.obtainStyledAttributes(
                attrs,
                R.styleable.MaskableLayout,
                0, 0
            )
            try {
                //Load the mask if specified in xml
                initMask(loadMask(a))
                //Load the mode if specified in xml
                mPorterDuffXferMode = getModeFromInteger(
                    a.getInteger(R.styleable.MaskableLayout_porterduffxfermode, 0)
                )
                initMask(drawableMask)
                //Check antiAlias
                if (a.getBoolean(R.styleable.MaskableLayout_anti_aliasing, false)) {
                    //Recreate paint with anti aliasing enabled
                    //This can take a performance hit.
                    mPaint = createPaint(true)
                }
            } finally {
                if (a != null) {
                    a.recycle()
                }
            }
        } else {
            log("Couldn't load theme, mask in xml won't be loaded.")
        }
        registerMeasure()
    }

    private fun createPaint(antiAliasing: Boolean): Paint {
        val output = Paint(Paint.ANTI_ALIAS_FLAG)
        output.isAntiAlias = antiAliasing
        output.setXfermode(mPorterDuffXferMode)
        return output
    }

    //Mask functions
    private fun loadMask(a: TypedArray): Drawable? {
        val drawableResId = a.getResourceId(R.styleable.MaskableLayout_mask, -1)
        if (drawableResId == -1) {
            return null
        }
        return AppCompatResources.getDrawable(context, drawableResId)
    }

    private fun initMask(input: Drawable?) {
        if (input != null) {
            drawableMask = input
            (drawableMask as? AnimationDrawable)?.callback = this
        } else {
            log("Are you sure you don't want to provide a mask ?")
        }
    }

    private fun makeBitmapMask(drawable: Drawable?): Bitmap? {
        if (drawable != null) {
            if (measuredWidth > 0 && measuredHeight > 0) {
                val mask = Bitmap.createBitmap(
                    measuredWidth, measuredHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(mask)
                drawable.setBounds(0, 0, measuredWidth, measuredHeight)
                drawable.draw(canvas)
                return mask
            } else {
                log("Can't create a mask with height 0 or width 0. Or the layout has no children and is wrap content")
                return null
            }
        } else {
            log("No bitmap mask loaded, view will NOT be masked !")
        }
        return null
    }

    fun setMask(drawableRes: Int) {
        val res = resources
        if (res != null) {
            setMask(res.getDrawable(drawableRes))
        } else {
            log("Unable to load resources, mask will not be loaded as drawable")
        }
    }

    fun setMask(input: Drawable?) {
        initMask(input)
        swapBitmapMask(makeBitmapMask(drawableMask))
        invalidate()
    }

    fun setPorterDuffXferMode(mode: PorterDuff.Mode?) {
        this.mPorterDuffXferMode = if (mode != null) PorterDuffXfermode(mode) else null
        currentMode = mode
//        this.mPorterDuffXferMode = PorterDuffXfermode(mode)
    }

    fun getPorterDuffXferMode(): PorterDuff.Mode {
        return currentMode?:PorterDuff.Mode.DST_IN
    }

    //Once the size has changed we need to remake the mask.
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setSize(w, h)
    }

    private fun setSize(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            if (drawableMask != null) {
                //Remake the 9patch
                swapBitmapMask(makeBitmapMask(drawableMask))
            }
        } else {
            log("Width and height must be higher than 0")
        }
    }

    //Drawing
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mFinalMask != null && mPaint != null) {
            mPaint!!.setXfermode(mPorterDuffXferMode)
            canvas.drawBitmap(mFinalMask!!, 0.0f, 0.0f, mPaint)
            mPaint!!.setXfermode(null)
        } else {
            log("Mask or paint is null ...")
        }
    }

    //Once inflated we have no height or width for the mask. Wait for the layout.
    private fun registerMeasure() {
        val treeObserver = this@MaskableFrameLayout.viewTreeObserver
        if (treeObserver != null && treeObserver.isAlive) {
            treeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    var aliveObserver = treeObserver
                    if (!aliveObserver.isAlive) {
                        aliveObserver = this@MaskableFrameLayout.viewTreeObserver
                    }
                    if (aliveObserver != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            aliveObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            aliveObserver.removeGlobalOnLayoutListener(this)
                        }
                    } else {
                        log("GlobalLayoutListener not removed as ViewTreeObserver is not valid")
                    }
                    swapBitmapMask(makeBitmapMask(drawableMask))
                }
            })
        }
    }

    //Logging
    private fun log(message: String) {
        Log.d(TAG, message)
    }

    //Animation
    override fun invalidateDrawable(dr: Drawable) {
        if (dr != null) {
            initMask(dr)
            swapBitmapMask(makeBitmapMask(dr))
            invalidate()
        }
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, whn: Long) {
        if (who != null && what != null) {
            mHandler.postAtTime(what, whn)
        }
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        if (who != null && what != null) {
            mHandler.removeCallbacks(what)
        }
    }

    private fun swapBitmapMask(newMask: Bitmap?) {
        if (newMask != null) {
            if (mFinalMask != null && !mFinalMask!!.isRecycled) {
                mFinalMask!!.recycle()
            }
            mFinalMask = newMask
        }
    }

    //Utils
    private fun getModeFromInteger(index: Int): PorterDuffXfermode {
        var mode: PorterDuff.Mode? = null
        when (index) {
            MODE_ADD -> {
                mode = PorterDuff.Mode.ADD
                mode = PorterDuff.Mode.CLEAR
            }

            MODE_CLEAR -> mode = PorterDuff.Mode.CLEAR
            MODE_DARKEN -> mode = PorterDuff.Mode.DARKEN
            MODE_DST -> mode = PorterDuff.Mode.DST
            MODE_DST_ATOP -> mode = PorterDuff.Mode.DST_ATOP
            MODE_DST_IN -> mode = PorterDuff.Mode.DST_IN
            MODE_DST_OUT -> mode = PorterDuff.Mode.DST_OUT
            MODE_DST_OVER -> mode = PorterDuff.Mode.DST_OVER
            MODE_LIGHTEN -> mode = PorterDuff.Mode.LIGHTEN
            MODE_MULTIPLY -> mode = PorterDuff.Mode.MULTIPLY
            MODE_OVERLAY -> {
                mode = PorterDuff.Mode.OVERLAY
                mode = PorterDuff.Mode.SCREEN
            }

            MODE_SCREEN -> mode = PorterDuff.Mode.SCREEN
            MODE_SRC -> mode = PorterDuff.Mode.SRC
            MODE_SRC_ATOP -> mode = PorterDuff.Mode.SRC_ATOP
            MODE_SRC_IN -> mode = PorterDuff.Mode.SRC_IN
            MODE_SRC_OUT -> mode = PorterDuff.Mode.SRC_OUT
            MODE_SRC_OVER -> mode = PorterDuff.Mode.SRC_OVER
            MODE_XOR -> mode = PorterDuff.Mode.XOR
            else -> mode = PorterDuff.Mode.DST_IN
        }
        log("Mode is $mode")
        return PorterDuffXfermode(mode)
    }

    companion object {
        //Constants



        private const val TAG = "MaskableFrameLayout"

        private const val MODE_ADD = 0
        private const val MODE_CLEAR = 1
        private const val MODE_DARKEN = 2
        private const val MODE_DST = 3
        private const val MODE_DST_ATOP = 4
        private const val MODE_DST_IN = 5
        private const val MODE_DST_OUT = 6
        private const val MODE_DST_OVER = 7
        private const val MODE_LIGHTEN = 8
        private const val MODE_MULTIPLY = 9
        private const val MODE_OVERLAY = 10
        private const val MODE_SCREEN = 11
        private const val MODE_SRC = 12
        private const val MODE_SRC_ATOP = 13
        private const val MODE_SRC_IN = 14
        private const val MODE_SRC_OUT = 15
        private const val MODE_SRC_OVER = 16
        private const val MODE_XOR = 17
    }

}
