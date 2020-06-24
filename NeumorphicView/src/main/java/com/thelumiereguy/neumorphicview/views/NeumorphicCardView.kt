package com.thelumiereguy.neumorphicview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.thelumiereguy.neumorphicview.R
import com.thelumiereguy.neumorphicview.ViewProperty
import com.thelumiereguy.neumorphicview.utils.boundsRectF


class NeumorphicCardView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

    val TAG = "NeumorphicCardView"

    var neuBackgroundColor by ViewProperty(Color.TRANSPARENT)
    var horizontalPadding by ViewProperty(0F)
    var verticalPadding by ViewProperty(0F)
    var highlightDx by ViewProperty(0F)
    var highlightDy by ViewProperty(0F)
    var highlightRadius by ViewProperty(0F)
    var shadowDx by ViewProperty(0F)
    var shadowDy by ViewProperty(0F)
    var shadowRadius by ViewProperty(0F)
    var highlightColor by ViewProperty(Color.TRANSPARENT)
    var shadowColor by ViewProperty(Color.TRANSPARENT)
    var enableStroke by ViewProperty(false)
    var strokeWidth by ViewProperty(0F)
    var strokeColor by ViewProperty(Color.TRANSPARENT)
    var enablePreview by ViewProperty(false)
    var enableShadow by ViewProperty(false)
    var enableHighlight by ViewProperty(false)
    var cardRadius by ViewProperty(0F)

    var isInDrawPhase: Boolean = false

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
            highlightColor =
                getColor(R.styleable.NeumorphicCardView_highlightColor, Color.TRANSPARENT)
            shadowColor = getColor(R.styleable.NeumorphicCardView_shadowColor, Color.TRANSPARENT)
            strokeColor = getColor(R.styleable.NeumorphicCardView_stroke_color, Color.TRANSPARENT)
            neuBackgroundColor =
                getColor(R.styleable.NeumorphicCardView_neu_backgroundColor, Color.TRANSPARENT)
            strokeWidth = getDimension(R.styleable.NeumorphicCardView_stroke_width, 0F)
            enableStroke = getBoolean(R.styleable.NeumorphicCardView_enableStroke, false)
            enableShadow = getBoolean(R.styleable.NeumorphicCardView_enableShadow, false)
            enableHighlight = getBoolean(R.styleable.NeumorphicCardView_enableHighlight, false)
            enablePreview = getBoolean(R.styleable.NeumorphicCardView_enable_preview, false)
            recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!enablePreview && isInEditMode) {
            return
        }
        isInDrawPhase = true
        canvas?.let {
            updateRect()
            if (neuBackgroundColor == Color.TRANSPARENT) {
                throw IllegalArgumentException("Required attribute `neu_backgroundColor` not specified")
            }
            drawHighlights(it, backgroundRectF)
            drawShadows(it, backgroundRectF)
            drawStroke(it, backgroundRectF)
            clearPaint()
            drawBackground(it, backgroundRectF)
        }
    }

    /**
     * In case you just want the background
     */
    private fun drawBackground(
        canvas: Canvas,
        childRect: RectF
    ) {
        if (cardRadius > 0F || verticalPadding > 0F || horizontalPadding > 0F)
            canvas.drawRoundRect(
                childRect,
                cardRadius,
                cardRadius,
                neumorphicPaint
            )
    }

    private fun updateRect() {
        if (childCount > 0) {
            val child = getChildAt(0)
            val bounds = child.boundsRectF
            backgroundRectF.apply {
                top = bounds.top - verticalPadding
                left = bounds.left - horizontalPadding
                right = bounds.right + horizontalPadding
                bottom = bounds.bottom + verticalPadding
            }
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
            color = neuBackgroundColor
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
            color = neuBackgroundColor
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (childCount == 0) {
            val bounds = this.boundsRectF
            backgroundRectF.apply {
                top = bounds.top + verticalPadding
                left = bounds.left + horizontalPadding
                right = bounds.right - horizontalPadding
                bottom = bounds.bottom - verticalPadding
            }
        }
    }

    private var backgroundRectF =
        RectF(
            0f,
            0f,
            0f,
            0f
        )

    private val neumorphicPaint by lazy {
        Paint().apply {
            color = neuBackgroundColor
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

}