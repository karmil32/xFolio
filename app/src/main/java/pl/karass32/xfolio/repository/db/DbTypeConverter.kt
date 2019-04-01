package pl.karass32.xfolio.repository.db

import androidx.room.TypeConverter
import java.math.BigDecimal

/**
 * Created by karas on 20.02.2018.
 */
class DbTypeConverter {

    @TypeConverter
    fun toBigDecimal(value: Long?) : BigDecimal? {
        return if (value == null) null else BigDecimal(value).scaleByPowerOfTen(-7)
    }
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?) : Long? {
        return if (value == null) null else value.scaleByPowerOfTen(7).toLong()
    }
}