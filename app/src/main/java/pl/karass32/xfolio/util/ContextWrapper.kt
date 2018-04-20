package pl.karass32.xfolio.util

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*


class ContextWrapper(base: Context) : android.content.ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, newLocale: Locale): ContextWrapper {
            var newContext = context

            val res = newContext.resources
            val configuration = res.configuration

            configuration.setLocale(newLocale)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration.locales = localeList
            }

            newContext = newContext.createConfigurationContext(configuration)

            return ContextWrapper(newContext)
        }
    }
}