package tmg.flashback.managers

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.prefs.manager.SharedPreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesManager @Inject constructor(
    @ApplicationContext context: Context
): SharedPreferenceManager(context) {

    override val prefsKey: String
        get() = "Flashback"

}