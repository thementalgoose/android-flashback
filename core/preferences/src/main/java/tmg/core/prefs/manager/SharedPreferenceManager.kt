package tmg.core.prefs.manager

import android.content.Context
import android.content.SharedPreferences

abstract class SharedPreferenceManager(
    private val context: Context
): PreferenceManager {
    /**
     * Specify the preference key
     */
    abstract val prefsKey: String

    open val mode: Int = Context.MODE_PRIVATE

    private val sharedPrefs: SharedPreferences
        get() = context.getSharedPreferences(prefsKey, mode)

    private val editor: SharedPreferences.Editor
        get() = sharedPrefs.edit()

    //region Saving methods

    override fun save(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    override fun save(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    override fun save(key: String, value: Long) {
        editor.putLong(key, value).apply()
    }

    override fun save(key: String, value: Float) {
        editor.putFloat(key, value).apply()
    }

    override fun save(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    override fun save(key: String, value: Set<String>) {
        editor.putStringSet(key, value).apply()
    }

    override fun getInt(key: String, value: Int): Int {
        return sharedPrefs.getInt(key, value)
    }

    override fun getString(key: String, value: String?): String? {
        return sharedPrefs.getString(key, value)
    }

    override fun getLong(key: String, value: Long): Long {
        return sharedPrefs.getLong(key, value)
    }

    override fun getFloat(key: String, value: Float): Float {
        return sharedPrefs.getFloat(key, value)
    }

    override fun getBoolean(key: String, value: Boolean): Boolean {
        return sharedPrefs.getBoolean(key, value)
    }

    override fun getSet(key: String, value: Set<String>): MutableSet<String> {
        return sharedPrefs.getStringSet(key, value) ?: mutableSetOf()
    }

    //endregion
}