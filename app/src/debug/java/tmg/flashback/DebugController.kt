package tmg.flashback

import android.content.Context
import android.content.Intent
import tmg.flashback.configuration.services.RemoteConfigService
import tmg.flashback.debug.DebugActivity
import tmg.flashback.ui2.dashboard.list.ListItem

class DebugController(
    private val remoteConfigService: RemoteConfigService
) {

    // TODO: Remove this once migration to Jetpack Compose is done!
    val compose: Boolean
        get() = remoteConfigService.getBoolean("dev_compose")

    val listItem: ListItem.Button?
        get() {
            return ListItem.Button(
                itemId = "debug_list_item",
                label = R.string.debug_list_item,
                icon = R.drawable.debug_list_item,
            )
        }

    fun goToDebugActivity(context: Context) {
        context.startActivity(Intent(context, DebugActivity::class.java))
    }
}