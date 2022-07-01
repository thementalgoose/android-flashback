package tmg.flashback

import android.content.Context
import android.widget.Toast
import tmg.flashback.configuration.services.RemoteConfigService
import tmg.flashback.ui2.dashboard.list.ListItem

class DebugController(
    private val remoteConfigService: RemoteConfigService
) {

    val listItem: ListItem.Button? = null

    fun goToDebugActivity(context: Context) {
        /* No op */
    }
}