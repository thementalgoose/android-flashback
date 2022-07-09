package tmg.flashback

import android.content.Context
import android.content.Intent
import tmg.flashback.configuration.services.RemoteConfigService
import tmg.flashback.debug.DebugActivity

class DebugController(
    private val remoteConfigService: RemoteConfigService
) {

    fun goToDebugActivity(context: Context) {
        context.startActivity(Intent(context, DebugActivity::class.java))
    }
}