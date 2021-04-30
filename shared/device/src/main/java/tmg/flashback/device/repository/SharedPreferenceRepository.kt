package tmg.flashback.device.repository

import android.content.Context
import tmg.utilities.prefs.SharedPrefManager

class SharedPreferenceRepository(context: Context): SharedPrefManager(context) {

    override val prefsKey: String = "Flashback"
}