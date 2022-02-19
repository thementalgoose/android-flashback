package tmg.flashback.regulations.domain

import androidx.annotation.StringRes
import tmg.flashback.formula1.enums.Tyre

data class TyreAllocation(
    val tyre: Tyre,
    @StringRes
    val label: Int
)