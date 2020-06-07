package com.thelumiereguy.neumorphicview.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.thelumiereguy.neumorphicview.R
import com.thelumiereguy.neumorphicview.utils.rectFify
import kotlin.math.roundToInt


class NeumorphicCardView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

    val TAG = "NeumorphicCardView"


    private val horizontalPadding: Float
    private val verticalPadding: Float
    private val cardRadius: Float

    private val highlightDx: Float
    private val highlightDy: Float
    private val highlightRadius: Float
    private val shadowDx: Float
    private val shadowDy: Float
    private val shadowRadius: Float
    private val highlightColor: Int
    private val shadowColor: Int
    private val enableStroke: Boolean
    private val strokeWidth: Float
    private val strokeColor: Int
    private val enablePreview: Boolean

    private var backgroundPaintColor: Int = Color.WHITE

    init {
        setWillNotDraw(false)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        setBackgroundColor(Color.TRANSPARENT)
        val customAttributes =
            context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.NeumorphicCardView, 0, 0
            )
        with(customAttributes) {
            horizontalPadding = getDimension(R.styleable.NeumorphicCardView_horizontalPadding, 0F)
            verticalPadding = getDimension(R.styleable.NeumorphicCardView_verticalPadding, 0F)
            cardRadius = getFloat(R.styleable.NeumorphicCardView_cardRadius, 0F)
            highlightDx = getDimension(R.styleable.NeumorphicCardView_highlightDx, 0F)
            highlightDy = getDimension(R.styleable.NeumorphicCardView_highlightDy, 0F)
            highlightRadius = getDimension(R.styleable.NeumorphicCardView_highlightRadius, 0F)
            shadowDx = getDimension(R.styleable.NeumorphicCardView_shadowDx, 0F)
            shadowDy = getDimension(R.styleable.NeumorphicCardView_shadowDy, 0F)
            shadowRadius = getDimension(R.styleable.NeumorphicCardView_shadowRadius, 0F)
            highlightColor = getColor(R.styleable.NeumorphicCardView_highlightColor, Color.TRANSPARENT)
            shadowColor = getColor(R.styleable.NeumorphicCardView_shadowColor, Color.TRANSPARENT)
            strokeColor = getColor(R.styleable.NeumorphicCardView_stroke_color, Color.TRANSPARENT)
            backgroundPaintColor = getColor(R.styleable.NeumorphicCardView_neu_backgroundColor, Color.WHITE)
            strokeWidth = getDimension(R.styleable.NeumorphicCardView_stroke_width, 0F)
            enableStroke = getBoolean(R.styleable.NeumorphicCardView_enableStroke, false)
            enablePreview = getBoolean(R.styleable.NeumorphicCardView_enable_preview, false)
            recycle()
        }
    }

    private val enableShadow: Boolean by lazy { shadowRadius > 0F || shadowDx > 0F || shadowDy > 0F }
    private val enableHighlight: Boolean by lazy { highlightRadius > 0F || highlightDx > 0F || highlightDy > 0F }


    private var backgroundRect =
        Rect(
            0,
            0,
            0,
            0
        )

    private val neumorphicPaint by lazy {
        Paint().apply {
            color = backgroundPaintColor
            this.setShadowLayer(
                shadowRadius, shadowDx,
                shadowDy,
                shadowColor
            )
        }
    }

    private val strokePaint by lazy {
        Paint().apply {
            color = strokeColor
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = this@NeumorphicCardView.strokeWidth
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!enablePreview && isInEditMode) {
            return
        }
        canvas?.let {
            val backgroundRectF = backgroundRect.rectFify()
            drawHighlights(it, backgroundRectF)
            drawShadows(it, backgroundRectF)
            drawStroke(it, backgroundRectF)
            clearPaint()
        }
    }

    private fun drawStroke(
        canvas: Canvas,
        childRect: RectF
    ) {
        if (enableStroke) {
            updateStrokePaint()
            canvas.drawRoundRect(
                childRect,
                cardRadius,
                cardRadius,
                strokePaint
            )
        }
    }

    private fun drawShadows(
        canvas: Canvas,
        childRect: RectF
    ) {
        if (enableShadow) {
            updateShadowPaint()
            canvas.drawRoundRect(
                childRect,
                cardRadius,
                cardRadius,
                neumorphicPaint
            )
        }
    }

    private fun drawHighlights(
        canvas: Canvas,
        childRect: RectF
    ) {
        if (enableHighlight) {
            updateHighlightPaint()
            canvas.drawRoundRect(
                childRect,
                cardRadius,
                cardRadius,
                neumorphicPaint
            )
        }
    }

    private fun updateStrokePaint() {
        strokePaint.apply {
            color = strokeColor
            strokeWidth = strokeWidth
        }
    }


    private fun updateShadowPaint() {
        neumorphicPaint.apply {
            color = backgroundPaintColor
            this.setShadowLayer(
                shadowRadius,
                shadowDx,
                shadowDy,
                shadowColor
            )
        }
    }

    private fun updateHighlightPaint() {
        neumorphicPaint.apply {
            color = backgroundPaintColor
            this.setShadowLayer(
                highlightRadius,
                highlightDx,
                highlightDy,
                highlightColor
            )
        }
    }


    private fun clearPaint() {
        neumorphicPaint.clearShadowLayer()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            if (childCount > 0) {
                val child = getChildAt(0)
                child.measure(0, 0)
                backgroundRect = backgroundRect.apply {
                    this.left = child.left
                    this.top = child.top
                    this.right = child.right
                    this.bottom = child.bottom
                }
                val newHeight = backgroundRect.height() + verticalPadding * 2
                val newWidth = backgroundRect.width() + horizontalPadding * 2
                setMeasuredDimension(
                    newWidth.roundToInt(),
                    newHeight.roundToInt()
                )
            }
        }
    }
}