package pl.karass32.xfolio.repository.db

import android.arch.persistence.room.TypeConverter
import java.math.BigDecimal

/**
 * Created by karas on 20.02.2018.
 */
class DbTypeConverter {

    @TypeConverter
    fun toBigDecimal(value: String?) : BigDecimal? {
        return if (value == null) null else BigDecimal(value)
    }
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?) : String? {
        return if (value == null) null else value.toString()
    }
}