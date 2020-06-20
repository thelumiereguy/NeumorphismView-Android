package com.thelumiereguy.neumorphicview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import com.thelumiereguy.neumorphicview.R
import com.thelumiereguy.neumorphicview.utils.boundsRectF


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

    private val neumorphicPaint by lazy {
        Paint()
    }

    private val strokePaint by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    }

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!enablePreview && isInEditMode) {
            return
        }
        canvas?.let {
            0.until(childCount).forEach { childIndex ->
                val childView = getChildAt(childIndex)
                val childRect = childView.boundsRectF
                val layoutParams = childView.layoutParams as LayoutParams
                if (layoutParams.neuBackgroundColor == Color.TRANSPARENT) {
                    throw IllegalArgumentException("Required attribute `layout_backgroundColor` not specified for child ${childView.javaClass.simpleName}")
                }
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
            canvas.drawRoundRect(
                childRect,
                layoutParams.cardRadius,
                layoutParams.cardRadius,
                neumorphicPaint
            )
        }
    }

    private fun drawHighlights(
        layoutParams: LayoutParams,
        canvas: Canvas,
        childRect: RectF
    ) {
        if (layoutParams.enableHighlight) {
            updateHighlightPaint(layoutParams)
            canvas.drawRoundRect(
                childRect,
                layoutParams.cardRadius,
                layoutParams.cardRadius,
                neumorphicPaint
            )
        }
    }

    private fun clearPaint() {
        neumorphicPaint.clearShadowLayer()
    }


    private fun updateRectParams(
        childRect: RectF,
        layoutParams: LayoutParams
    ) {
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
        neumorphicPaint.apply {
            color = layoutParams.neuBackgroundColor
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
        neumorphicPaint.apply {
            color = layoutParams.neuBackgroundColor
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
        var neuBackgroundColor: Int = Color.TRANSPARENT
        var enableStroke: Boolean = false
        var enableShadow: Boolean = false
        var enableHighlight: Boolean = false

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
                neuBackgroundColor =
                    getColor(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_neu_backgroundColor,
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

                enableStroke =
                    getBoolean(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_enableStroke,
                        false
                    )

                enableShadow =
                    getBoolean(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_enableShadow,
                        false
                    )

                enableHighlight =
                    getBoolean(
                        R.styleable.NeumorphicConstraintLayout_Layout_layout_enableHighlight,
                        false
                    )
                recycle()
            }
        }
    }

}