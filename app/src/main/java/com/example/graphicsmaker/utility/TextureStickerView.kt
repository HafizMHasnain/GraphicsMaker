package com.example.graphicsmaker.utility
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.graphicsmaker.R
import kotlin.math.atan2
import kotlin.math.max


class TextureStickerView : RelativeLayout {
    private var stickerImageView: ImageView? = null
    private var scaleIcon: ImageView? = null
    private var rotateIcon: ImageView? = null
    private var flipIcon: ImageView? = null
    private var cancelIcon: ImageView? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        // Initialize sticker image
        stickerImageView = ImageView(context)
        stickerImageView!!.id = generateViewId()
        stickerImageView!!.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        stickerImageView!!.scaleType = ImageView.ScaleType.CENTER_INSIDE
        addView(stickerImageView)

        // Initialize scale icon
        scaleIcon = ImageView(context)
        scaleIcon!!.id = generateViewId()
        scaleIcon!!.setImageResource(R.drawable.sticker_scale) // Replace with your drawable
        val scaleParams = LayoutParams(100, 100)
        scaleParams.addRule(ALIGN_PARENT_END)
        scaleParams.addRule(ALIGN_PARENT_BOTTOM)
        scaleIcon!!.layoutParams = scaleParams
        addView(scaleIcon)

        // Initialize rotate icon
        rotateIcon = ImageView(context)
        rotateIcon!!.id = generateViewId()
        rotateIcon!!.setImageResource(R.drawable.rotate) // Replace with your drawable
        val rotateParams = LayoutParams(100, 100)
        rotateParams.addRule(ALIGN_PARENT_START)
        rotateParams.addRule(ALIGN_PARENT_BOTTOM)
        rotateIcon!!.layoutParams = rotateParams
        addView(rotateIcon)

        // Initialize flip icon
        flipIcon = ImageView(context)
        flipIcon!!.id = generateViewId()
        flipIcon!!.setImageResource(R.drawable.sticker_flip) // Replace with your drawable
        val flipParams = LayoutParams(100, 100)
        flipParams.addRule(ALIGN_PARENT_END)
        flipParams.addRule(ALIGN_PARENT_TOP)
        flipIcon!!.layoutParams = flipParams
        addView(flipIcon)

        // Initialize cancel icon
        cancelIcon = ImageView(context)
        cancelIcon!!.id = generateViewId()
        cancelIcon!!.setImageResource(R.drawable.sticker_delete1) // Replace with your drawable
        val cancelParams = LayoutParams(100, 100)
        cancelParams.addRule(ALIGN_PARENT_START)
        cancelParams.addRule(ALIGN_PARENT_TOP)
        cancelIcon!!.layoutParams = cancelParams
        addView(cancelIcon)

        // Setup listeners for interactions
        setupListeners()
    }

    private fun setupListeners() {
        scaleIcon!!.setOnTouchListener(object : OnTouchListener {
            private var initialX = 0f
            private var initialY = 0f
            private var initialWidth = 0
            private var initialHeight = 0

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = event.rawX
                        initialY = event.rawY
                        initialWidth = width
                        initialHeight = height
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val deltaX = event.rawX - initialX
                        val deltaY = event.rawY - initialY
                        val layoutParams = this@TextureStickerView.layoutParams as LayoutParams
                        layoutParams.width = max(
                            200.0,
                            (initialWidth + deltaX).toInt().toDouble()
                        )
                            .toInt()
                        layoutParams.height = max(
                            200.0,
                            (initialHeight + deltaY).toInt().toDouble()
                        )
                            .toInt()
                        this@TextureStickerView.layoutParams = layoutParams
                        return true
                    }
                }
                return false
            }
        })

        rotateIcon!!.setOnTouchListener(object : OnTouchListener {
            private var initialAngle = 0f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX - this@TextureStickerView.x
                        val dy = event.rawY - this@TextureStickerView.y
                        val angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                        this@TextureStickerView.rotation = angle - initialAngle
                        return true
                    }

                    MotionEvent.ACTION_DOWN -> {
                        initialAngle = this@TextureStickerView.rotation
                        return true
                    }
                }
                return false
            }
        })

        flipIcon!!.setOnClickListener { v: View? ->
            stickerImageView!!.scaleX = stickerImageView!!.scaleX * -1
        }

        cancelIcon!!.setOnClickListener { v: View? ->
            this@TextureStickerView.visibility =
                GONE
        }
    }

    fun setStickerImageResource(resId: Int) {
        stickerImageView!!.setImageResource(resId)
    }
}
