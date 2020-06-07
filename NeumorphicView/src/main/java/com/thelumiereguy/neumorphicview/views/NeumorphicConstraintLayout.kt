package com.thelumiereguy.neumorphicview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import com.thelumiereguy.neumorphicview.R
import com.thelumiereguy.neumorphicview.utils.boundsRectF
import me.eugeniomarletti.renderthread.CanvasProperty
import me.eugeniomarletti.renderthread.RenderThread


class NeumorphicConstraintLayout : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, attributeSetId: Int) : super(
        context,
        attrs,
        attributeSetId
    ) {
        initAttributes(attrs)
    }

    private val shadowPaint by lazy {
        Paint()
    }

    private val strokePaint by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    }

    private var paintProperty: CanvasProperty<Paint>? = null
    private var leftProperty: CanvasProperty<Float>? = null
    private var rightProperty: CanvasProperty<Float>? = null
    private var bottomProperty: CanvasProperty<Float>? = null
    private var topProperty: CanvasProperty<Float>? = null
    private var rxProperty: CanvasProperty<Float>? = null
    private var ryProperty: CanvasProperty<Float>? = null

    private var enablePreview: Boolean = false

    private fun initAttributes(attrs: AttributeSet?) {
        val customAttributes =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.NeumorphicCardView, 0, 0
            )
        with(customAttributes) {
            enablePreview = getBoolean(R.styleable.NeumorphicCardView_enable_preview, false)
            recycle()
        }
    }

    init {
        setWillNotDraw(false)
//        setLayerType(View.LAYER_TYPE_HARDWARE, shadowPaint)
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!enablePreview) {
            return
        }
        canvas?.let {
            paintProperty = RenderThread.createCanvasProperty(canvas, shadowPaint, true)
            0.until(childCount).forEach { childIndex ->
                val childView = getChildAt(childIndex)
                val childRect = childView.boundsRectF
                val layoutParams = childView.layoutParams as LayoutParams
                if (layoutParams.backgroundPaintColor == Color.TRANSPARENT) {
                    throw IllegalArgumentException("Required attribute `layout_backgroundColor` not specified for child ${childView.javaClass.simpleName}")
                }
                paintProperty
                updateRectParams(childRect, layoutParams)
                drawHighlights(layoutParams, it, childRect)
                drawShadows(layoutParams, it, childRect)
                drawStroke(layoutParams, it, childRect)
                clearPaint()
            }
        }
    }

    private fun drawStroke(
        layoutParams: LayoutParams,
        canvas: Canvas,
        childRect: RectF
    ) {
        if (layoutParams.enableStroke) {
            updateStrokePaint(layoutParams)
            canvas.drawRoundRect(
                childRect,
                layoutParams.cardRadius,
                layoutParams.cardRadius,
                strokePaint
            )
        }
    }

    private fun drawShadows(
        layoutParams: LayoutParams,
        canvas: Canvas,
        childRect: RectF
    ) {
        if (layoutParams.enableShadow) {
            updateShadowPaint(layoutParams)
//            canvas.drawRoundRect(
//                childRect,
//                layoutParams.cardRadius,
//                layoutParams.cardRadius,
//                shadowPaint
//            )
            leftProperty = RenderThread.createCanvasProperty(canvas, childRect.left)
            topProperty = RenderThread.createCanvasProperty(canvas, childRect.top)
            rightProperty = RenderThread.createCanvasProperty(canvas, childRect.right)
            bottomProperty = RenderThread.createCanvasProperty(canvas, childRect.bottom)
            rxProperty = RenderThread.createCanvasProperty(canvas, layoutParams.cardRadius)
            ryProperty = RenderThread.createCanvasProperty(canvas, layoutParams.cardRadius)
            bottomProperty = RenderThread.createCanvasProperty(canvas, childRect.bottom)
            paintProperty?.let {
                RenderThread.drawRoundRect(canvas, leftProperty as CanvasProperty<Float>,
                    topProperty as CanvasProperty<Float>,
                    rightProperty as CanvasProperty<Float>,
                    bottomProperty as CanvasProperty<Float>,
                    rxProperty as CanvasProperty<Float>,
                    ryProperty as CanvasProperty<Float>,
                    it
                )
            }
        }
    }

    private fun drawHighlights(
        layoutParams: LayoutParams,
        canvas: Canvas,
        childRect: RectF
    ) {
        if (layoutParams.enableHighlight) {
            updateHighlightPaint(layoutParams)
//            leftProperty = RenderThread.createCanvasProperty(canvas, childRect.left)
//            topProperty = RenderThread.createCanvasProperty(canvas, childRect.top)
//            rightProperty = RenderThread.createCanvasProperty(canvas, childRect.right)
//            bottomProperty = RenderThread.createCanvasProperty(canvas, childRect.bottom)
//            rxProperty = RenderThread.createCanvasProperty(canvas, layoutParams.cardRadius)
//            ryProperty = RenderThread.createCanvasProperty(canvas, layoutParams.cardRadius)
//            bottomProperty = RenderThread.createCanvasProperty(canvas, childRect.bottom)
            paintProperty?.let {
                RenderThread.drawRoundRect(canvas, leftProperty as CanvasProperty<Float>,
                    topProperty as CanvasProperty<Float>,
                    rightProperty as CanvasProperty<Float>,
                    bottomProperty as CanvasProperty<Float>,
                    rxProperty as CanvasProperty<Float>,
                    ryProperty as CanvasProperty<Float>,
                    it
                )
            }
//            canvas.drawRoundRect(
//                childRect,
//                layoutParams.cardRadius,
//                layoutParams.cardRadius,
//                shadowPaint
//            )
        }
    }

    private fun clearPaint() {
        shadowPaint.clearShadowLayer()
    }


    private fun updateRectParams(childRect: RectF, layoutParams: LayoutParams) {
        childRect.apply {
            left -= layoutParams.horizontalPadding
            top -= layoutParams.verticalPadding
            right += layoutParams.horizontalPadding
            bottom += layoutParams.verticalPadding
        }
    }

    private fun updateStrokePaint(
        layoutParams: LayoutParams
    ) {
        strokePaint.apply {
            color = layoutParams.strokeColor
            strokeWidth = layoutParams.strokeWidth
        }
    }


    private fun updateShadowPaint(
        layoutParams: LayoutParams
    ) {
        shadowPaint.apply {
            color = layoutParams.backgroundPaintColor
            this.setShadowLayer(
                layoutParams.shadowRadius,
                layoutParams.shadowDx,
                layoutParams.shadowDy,
                layoutParams.shadowColor
            )
        }
    }

    private fun updateHighlightPaint(

        layoutParams: LayoutParams
    ) {
        shadowPaint.apply {
            color = layoutParams.backgroundPaintColor
            this.setShadowLayer(
                layoutParams.highlightRadius,
                layoutParams.highlightDx,
                layoutParams.highlightDy,
                layoutParams.highlightColor
            )
        }
    }


    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        val layoutParams = super.generateLayoutParams(attrs)
        return LayoutParams(context, layoutParams, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams;
    }


    class LayoutParams : ConstraintLayout.LayoutParams {

        var horizontalPadding: Float = 0F
        var verticalPadding: Float = 0F
        var cardRadius: Float = 0F
        var highlightDx: Float = 0F
        var highlightDy: Float = 0F
        var highlightRadius: Float = 0F
        var shadowDx: Float = 0F
        var shadowDy: Float = 0F
        var shadowRadius: Float = 0F
        var highlightColor: Int = Color.TRANSPARENT
        var shadowColor: Int = Color.TRANSPARENT
        var strokeWidth: Float = 0F
        var strokeColor: Int = Color.TRANSPARENT
        var backgroundPaintColor: Int = Color.WHITE
        val enableStroke: Boolean by lazy { strokeWidth > 0F }
        val enableShadow: Boolean by lazy { shadowRadius > 0F || shadowDx > 0F || shadowDy > 0F }
        val enableHighlight: Boolean by lazy { highlightRadius > 0F || highlightDx > 0F || highlightDy > 0F }

        constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
            initLayoutParams(context, attrs)
        }

        constructor(width: Int, height: Int) : super(width, height)

        constructor(
            context: Context,
            layoutParams: ConstraintLayout.LayoutParams,
            attrs: AttributeSet?
        ) : super(
            layoutParams
        ) {
            initLayoutParams(context, attrs)
        }


        private fun initLayoutParams(context: Context, attrs: AttributeSet?) {
            val customAttributes =
                context.obtainStyledAttributes(
                    attrs,
                    R.styleable.NeumorphicConstraintLayout_Layout
                )
            with(customAttributes) {
                horizontalPadding =
                    getDimension(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_horizontalPadding,
                        0F
                    )
                backgroundPaintColor =
                    getColor(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_backgroundColor,
                        Color.TRANSPARENT
                    )
                verticalPadding =
                    getDimension(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_verticalPadding,
                        0F
                    )
                cardRadius =
                    getFloat(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_cardRadius,
                        0F
                    )
                highlightDx = getDimension(
                    R.styleable.NeumorphicConstraintLayout_Layout_layout_highlightDx,
                    0F
                )
                highlightDy = getDimension(
                    R.styleable.NeumorphicConstraintLayout_Layout_layout_highlightDy,
                    0F
                )
                highlightRadius =
                    getDimension(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_highlightRadius,
                        0F
                    )
                shadowDx =
                    getDimension(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_shadowDx,
                        0F
                    )
                shadowDy =
                    getDimension(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_shadowDy,
                        0F
                    )
                shadowRadius =
                    getDimension(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_shadowRadius,
                        0F
                    )
                highlightColor =
                    getColor(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_highlightColor,
                        Color.TRANSPARENT
                    )
                shadowColor =
                    getColor(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_shadowColor,
                        Color.TRANSPARENT
                    )
                strokeColor =
                    getColor(
                        R.styleable.NeumorphicConstraintLayout_Layout_stroke_color,
                        Color.TRANSPARENT
                    )
                strokeWidth =
                    getDimension(
                        R.styleable.NeumorphicConstraintLayout_Layout_stroke_width,
                        0F
                    )
                recycle()
            }
        }

    }
}