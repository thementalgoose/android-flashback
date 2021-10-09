package tmg.flashback

import android.content.Context
import android.content.Intent
import tmg.flashback.debug.DebugActivity
import tmg.flashback.ui.dashboard.list.ListItem

class DebugController {

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