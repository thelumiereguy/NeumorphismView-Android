package com.thelumiereguy.neumorphicview.utils

import android.graphics.Rect
import android.graphics.RectF
import android.view.View

fun Rect.rectFify(): RectF = RectF(this)

val View.boundsRectF
    get():RectF {
        return RectF(x, y, x + measuredWidth, y + measuredHeight)
    }