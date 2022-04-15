package tmg.flashback.regulations.extensions

import tmg.flashback.regulations.domain.hasFormatInfoFor

fun Int.hasFormatInfo(): Boolean {
    return hasFormatInfoFor(this)
}