package tmg.f1stats.prefs

import android.content.Context
import org.threeten.bp.Year
import org.threeten.bp.ZoneId
import tmg.f1stats.currentYear
import tmg.f1stats.repo.db.PrefsDB
import tmg.utilities.utils.SharedPreferencesUtils

class SharedPrefsDB(val context: Context) : PrefsDB {

    private val prefsKey: String = "F1Stats"
    private val keySelectedYear: String = "SELECTED_YEAR"

    override var selectedYear: Int
        get() = getInt(keySelectedYear, currentYear)
        set(value) {
            value.save(keySelectedYear)
        }

    //region Utils

    private fun Int.save(key: String) {
        SharedPreferencesUtils.save(context, key, this, prefsKey)
    }

    private fun getInt(key: String, default: Int = -1): Int {
        val value = SharedPreferencesUtils.getInt(context, key, prefsKey)
        return if (value == -1) default
        else value
    }

    //endregion
}