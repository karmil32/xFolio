package pl.karass32.xfolio.extension

import android.support.v4.content.ContextCompat
import android.view.View

/**
 * Created by karas on 18.01.2018.
 */

fun View.getColor(colorRes: Int) = ContextCompat.getColor(this.context, colorRes)
