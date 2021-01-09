package tmg.flashback.firebase.base

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import tmg.flashback.firebase.converters.fromDate
import tmg.flashback.firebase.crash.FirebaseCrashManager
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.jvm.Throws

class GivenBlock<T,O>(
        private val model: T,
        private val isDebug: Boolean,
        private val crashManager: FirebaseCrashManager?
) {

    var convert: ((item: T) -> O)? = null

    private var shouldAttemptConversion: Boolean = true
    private var validModel: O? = null

    fun notNull(resolve: (T) -> Any?) {
        if (resolve(model) == null) {
            shouldAttemptConversion = false
        }
    }

    fun validTime(resolve: (T) -> String?) {
        val timeString = resolve(model)
        if (timeString == null) {
            shouldAttemptConversion = false
        } else {
            try {
                fromTime(timeString)
            } catch (e: DateTimeParseException) {
                e.throwIfDebug()
                crashManager?.logError(e, "Validity check: Invalid time found for $model")
                shouldAttemptConversion = false
            }
        }
    }

    fun validDate(resolve: (T) -> String?) {
        val dateString = resolve(model)
        if (dateString == null) {
            shouldAttemptConversion = false
        } else {
            try {
                fromDate(dateString)
            } catch (e: DateTimeParseException) {
                e.throwIfDebug()
                crashManager?.logError(e, "Validity check: Invalid date found for $model")
                shouldAttemptConversion = false
            }
        }
    }

    // Valid date
    // Valid time
//    fun notNull(resolve: (T) -> Any?) {
//        if (resolve(model) == null) {
//            shouldAttemptConversion = false
//        }
//    }

    val result: O?
        get() {
            if (!shouldAttemptConversion) {
                return null
            }
            return try {
                convert?.invoke(model)
            }
            catch (e: RuntimeException) {
                e.throwIfDebug()
                crashManager?.logError(e, "Failed at runtime to convert model $model")
                null
            }
            catch (e: Exception) {
                e.throwIfDebug()
                crashManager?.logError(e, "Failed to convert model $model. Something went very wrong")
                null
            }
        }

    private fun <T: Exception> T.throwIfDebug() {
        if (isDebug) {
            throw this
        }
    }

    //region Pattern

    /**
     * Attempt a date parsing
     * @throws DateTimeParseException
     */
    @Throws(DateTimeParseException::class)
    fun fromDate(dateString: String): LocalDate {
        val validPatterns = listOf(
                "yyyy-M-d",
                "yyyy-MM-dd"
        )
        return validPatterns
                .mapNotNull { pattern ->
                    try {
                        LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern))
                    } catch (e: RuntimeException) {
                        null
                    }
                }
                .firstOrNull() ?: throw DateTimeParseException("Failed to parse date string $dateString", "", 0)
    }
    @JvmName("fromDateNullable")
    fun fromDate(dateString: String?): LocalDate? {
        if (dateString == null) {
            return null
        }
        return try {
            return fromDate(dateString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Attempt a time parsing
     * @throws DateTimeParseException
     */
    @Throws(DateTimeParseException::class)
    fun fromTime(timeString: String): LocalTime {
        val validPatterns = listOf(
                "HH:mm:ss'Z'",
                "HH:mm:ss"
        )
        return validPatterns
                .mapNotNull { pattern ->
                    try {
                        LocalTime.parse(timeString, DateTimeFormatter.ofPattern(pattern))
                    } catch (e: RuntimeException) {
                        null
                    }
                }
                .firstOrNull() ?: throw DateTimeParseException("Failed to parse time string $timeString", "", 0)
    }
    @JvmName("fromTimeNullable")
    fun fromTime(timeString: String?): LocalTime? {
        if (timeString == null) {
            return null
        }
        return try {
            return fromTime(timeString)
        } catch (e: Exception) {
            null
        }
    }

    //endregion
}