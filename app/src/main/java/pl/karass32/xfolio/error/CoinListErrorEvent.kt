package pl.karass32.xfolio.error

import androidx.annotation.StringRes
import pl.karass32.xfolio.R

/**
 * Created by karas on 07.02.2018.
 */

enum class CoinListErrorEvent(@StringRes private val resourceId: Int) : ErrorEvent {
    NONE(0),
    NO_SERVER_CONNECTION(R.string.error_no_server_connection);

    override fun getErrorResource() = resourceId
}