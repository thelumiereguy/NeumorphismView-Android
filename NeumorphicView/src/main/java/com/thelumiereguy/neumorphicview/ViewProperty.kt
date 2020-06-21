package com.thelumiereguy.neumorphicview

import com.thelumiereguy.neumorphicview.views.NeumorphicCardView
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ViewProperty<T>(private var defaultValue: T) : ReadWriteProperty<NeumorphicCardView, T> {

    override fun getValue(thisRef: NeumorphicCardView, property: KProperty<*>): T {
        return defaultValue
    }

    override fun setValue(thisRef: NeumorphicCardView, property: KProperty<*>, value: T) {
        defaultValue = value
        if (thisRef.isInDrawPhase) {
            thisRef.invalidate()
        }
    }
}