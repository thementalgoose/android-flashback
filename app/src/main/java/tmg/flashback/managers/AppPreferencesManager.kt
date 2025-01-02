package tmg.flashback.managers

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.BuildConfig
import tmg.flashback.prefs.manager.SharedPreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesManager @Inject constructor(
    @ApplicationContext context: Context,
): SharedPreferenceManager(context) {

    override val prefsKey: String
        get() = "Flashback"

    override fun save(key: String, value: Boolean) {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.save($key, $value)")
        }
        super.save(key, value)
    }

    override fun save(key: String, value: Float) {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.save($key, $value)")
        }
        super.save(key, value)
    }

    override fun save(key: String, value: Int) {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.save($key, $value)")
        }
        super.save(key, value)
    }

    override fun save(key: String, value: Long) {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.save($key, $value)")
        }
        super.save(key, value)
    }

    override fun save(key: String, value: Set<String>) {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.save($key, $value)")
        }
        super.save(key, value)
    }

    override fun save(key: String, value: String) {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.save($key, $value)")
        }
        super.save(key, value)
    }

    override fun getBoolean(key: String, value: Boolean): Boolean {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.getBoolean($key) = " + super.getBoolean(key, value))
        }
        return super.getBoolean(key, value)
    }

    override fun getFloat(key: String, value: Float): Float {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.getFloat($key) = " + super.getFloat(key, value))
        }
        return super.getFloat(key, value)
    }

    override fun getInt(key: String, value: Int): Int {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.getInt($key) = " + super.getInt(key, value))
        }
        return super.getInt(key, value)
    }

    override fun getLong(key: String, value: Long): Long {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.getLong($key) = " + super.getLong(key, value))
        }
        return super.getLong(key, value)
    }

    override fun getSet(key: String, value: Set<String>): MutableSet<String> {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.getSet($key) = " + super.getSet(key, value))
        }
        return super.getSet(key, value)
    }

    override fun getString(key: String, value: String?): String? {
        if (BuildConfig.DEBUG) {
            Log.d("Preferences", "prefs.getString($key) = " + super.getString(key, value))
        }
        return super.getString(key, value)
    }
}