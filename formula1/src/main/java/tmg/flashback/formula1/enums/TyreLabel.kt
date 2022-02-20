package tmg.flashback.formula1.enums

import androidx.annotation.StringRes

data class TyreLabel(
    val tyre: Tyre,
    @StringRes
    val label: Int
)