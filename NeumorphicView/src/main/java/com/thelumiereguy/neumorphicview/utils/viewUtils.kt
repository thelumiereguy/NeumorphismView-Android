package com.thelumiereguy.neumorphicview.utils

import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import com.thelumiereguy.neumorphicview.views.NeumorphicConstraintLayout

fun Rect.rectFify(): RectF = RectF(this)

val View.boundsRectF
    get():RectF {
        return RectF(x, y, x + measuredWidth, y + measuredHeight)
    }

fun View.updateNeumorphicLayoutParams(block: NeumorphicConstraintLayout.LayoutParams.() -> Unit) {
    if(layoutParams is  NeumorphicConstraintLayout.LayoutParams) {
        layoutParams = (layoutParams as NeumorphicConstraintLayout.LayoutParams).apply(block)
    }
}
