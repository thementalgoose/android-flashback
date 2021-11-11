package tmg.flashback.managers

import android.content.Context
import tmg.flashback.prefs.manager.SharedPreferenceManager

class AppPreferencesManager(context: Context): SharedPreferenceManager(context) {

    override val prefsKey: String
        get() = "Flashback"

}