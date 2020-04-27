package tmg.flashback.extensions

import android.view.View
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.show

fun View.show(value: Boolean, isGone: Boolean = true) {
    if (value) {
        show()
    } else {
        if (isGone) gone() else invisible()
    }
}