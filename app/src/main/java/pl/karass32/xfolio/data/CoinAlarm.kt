package pl.karass32.xfolio.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import java.math.BigDecimal
import java.math.RoundingMode

@Entity(tableName = "coin_alarms", primaryKeys = ["coin_symbol", "alarm_type"])
data class CoinAlarm(@ColumnInfo(name = "coin_symbol") val coinSymbol: String,
                     @ColumnInfo(name = "alarm_type") val alarmType: Int,
                     @ColumnInfo(name = "alarm_value") private var _alarmValue: BigDecimal,
                     @ColumnInfo(name = "is_enabled") var isEnabled: Boolean) {

    var alarmValue: BigDecimal? = null
        get() = _alarmValue.let { if (it > BigDecimal("1")) it.setScale(2, RoundingMode.HALF_UP) else it.setScale(7, RoundingMode.HALF_UP) }
}