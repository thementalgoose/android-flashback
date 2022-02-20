package tmg.flashback.statistics.ui.shared.tyres

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.formula1.enums.TyreLabel
import tmg.flashback.statistics.R

sealed class TyreItem(
    @LayoutRes
    val layoutId: Int
) {
    class Header(
        @StringRes
        val label: Int
    ): TyreItem(R.layout.view_tyre_header)

    class Item(
        val tyreLabel: TyreLabel
    ): TyreItem(R.layout.view_tyre_item)
}