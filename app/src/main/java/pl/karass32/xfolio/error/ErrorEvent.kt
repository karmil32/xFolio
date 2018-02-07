package pl.karass32.xfolio.error

import android.support.annotation.StringRes

/**
 * Created by karas on 07.02.2018.
 */

interface ErrorEvent {

    @StringRes
    fun getErrorResource(): Int
}