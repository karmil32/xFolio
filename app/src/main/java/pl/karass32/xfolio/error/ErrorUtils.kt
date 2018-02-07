package pl.karass32.xfolio.error

import android.content.Context

/**
 * Created by karas on 07.02.2018.
 */

class ErrorUtils {

    companion object {
        fun getErrorString(context: Context?, errorEvent: ErrorEvent) = if (errorEvent.getErrorResource() == 0) {
            null
        } else {
            context?.getString(errorEvent.getErrorResource())
        }
    }
}