package com.oscarvera.snail.util

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

object Configuration {

    const val spanCount = 2

    fun getSpacing(resources: Resources) : Int =  (25 * resources.displayMetrics.density).roundToInt()

}