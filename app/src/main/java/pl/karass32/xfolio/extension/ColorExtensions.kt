package pl.karass32.xfolio.extension

import android.support.v4.content.ContextCompat
import android.view.View
import pl.karass32.xfolio.base.BaseFragment

/**
 * Created by karas on 18.01.2018.
 */

fun View.getColor(colorRes: Int) = ContextCompat.getColor(this.context, colorRes)
fun BaseFragment.getCompatColor(colorRes: Int) = ContextCompat.getColor(appContext, colorRes)
