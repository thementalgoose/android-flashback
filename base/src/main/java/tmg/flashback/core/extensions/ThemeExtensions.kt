package tmg.flashback.core.extensions

import android.content.Context
import tmg.flashback.core.enums.Theme
import tmg.utilities.extensions.isInDayMode

fun Theme.isLightMode(context: Context): Boolean {
    return this == Theme.DAY || (this == Theme.AUTO && context.isInDayMode())
}