package com.thelumiereguy.neumorphicview.utils

import android.graphics.RectF
import android.view.View
import com.thelumiereguy.neumorphicview.views.NeumorphicConstraintLayout

val View.boundsRectF
    get():RectF {
        return RectF(x, y, x + measuredWidth, y + measuredHeight)
    }

fun View.updateNeumorphicLayoutParams(block: NeumorphicConstraintLayout.LayoutParams.() -> Unit) {
    if(layoutParams is  NeumorphicConstraintLayout.LayoutParams) {
        layoutParams = (layoutParams as NeumorphicConstraintLayout.LayoutParams).apply(block)
    }
    invalidate()
}
