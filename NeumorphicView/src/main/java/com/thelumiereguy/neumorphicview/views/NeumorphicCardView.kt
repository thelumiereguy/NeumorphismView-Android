package com.thelumiereguy.neumorphicview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
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

    private var backgroundPaintColor: Int = Color.parseColor("#262B2F")

    init {
        setBackgroundColor(Color.TRANSPARENT)
        val customAttributes =
            context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.NeumorphicCardView, 0, 0
            )
        with(customAttributes) {
            horizontalPadding = getDimension(R.styleable.NeumorphicCardView_horizontalPadding, 64F)
            verticalPadding = getDimension(R.styleable.NeumorphicCardView_verticalPadding, 64F)
            cardRadius = getFloat(R.styleable.NeumorphicCardView_cardRadius, 0F)
            highlightDx = getDimension(R.styleable.NeumorphicCardView_highlightDx, -24F)
            highlightDy = getDimension(R.styleable.NeumorphicCardView_highlightDy, -24F)
            highlightRadius = getDimension(R.styleable.NeumorphicCardView_highlightRadius, 48F)
            shadowDx = getDimension(R.styleable.NeumorphicCardView_shadowDx, 24F)
            shadowDy = getDimension(R.styleable.NeumorphicCardView_shadowDy, 24F)
            shadowRadius = getDimension(R.styleable.NeumorphicCardView_shadowRadius, 48F)
            highlightColor = getColor(R.styleable.NeumorphicCardView_highlightColor, Color.WHITE)
            shadowColor = getColor(R.styleable.NeumorphicCardView_shadowColor, Color.WHITE)
            strokeColor = getColor(R.styleable.NeumorphicCardView_stroke_color, Color.WHITE)
            strokeWidth = getDimension(R.styleable.NeumorphicCardView_stroke_width, 0F)
            enableStroke = getBoolean(R.styleable.NeumorphicCardView_enableStroke, false)
            enablePreview = getBoolean(R.styleable.NeumorphicCardView_enable_preview, false)
            recycle()
        }
        if (childCount > 0) {
            val child = getChildAt(0)
            val background = child.background
            if (background is ColorDrawable) {
                backgroundPaintColor = background.color
            }
        }
    }


    private var backgroundRect =
        Rect(
            0,
            0,
            0,
            0
        )


    private val highlightPaint by lazy {
        Paint().apply {
            color = backgroundPaintColor
            this.setShadowLayer(
                highlightRadius, highlightDx,
                highlightDy,
                highlightColor
            )
            isAntiAlias = true
        }
    }


    private val shadowPaint by lazy {
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
        if (!enablePreview) {
            return
        }
        canvas?.let {
            setLayerType(View.LAYER_TYPE_HARDWARE, highlightPaint);
            it.drawRoundRect(
                backgroundRect.rectFify(),
                cardRadius,
                cardRadius,
                highlightPaint
            )
            setLayerType(View.LAYER_TYPE_HARDWARE, shadowPaint);
            it.drawRoundRect(
                backgroundRect.rectFify(),
                cardRadius,
                cardRadius,
                shadowPaint
            )
            if (enableStroke && strokeWidth > 0F)
                it.drawRoundRect(
                    backgroundRect.rectFify(),
                    cardRadius,
                    cardRadius,
                    strokePaint
                )
        }
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