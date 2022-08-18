package tmg.flashback.formula1.extensions

import androidx.annotation.DrawableRes
import tmg.flashback.formula1.R

@get:DrawableRes
val Int.positionIcon: Int
    get() = when (this) {
        1 -> R.drawable.ic_p1
        2 -> R.drawable.ic_p2
        3 -> R.drawable.ic_p3
        4 -> R.drawable.ic_p4
        5 -> R.drawable.ic_p5
        6 -> R.drawable.ic_p6
        7 -> R.drawable.ic_p7
        8 -> R.drawable.ic_p8
        9 -> R.drawable.ic_p9
        10 -> R.drawable.ic_p10
        else -> R.drawable.ic_p11_plus
    }