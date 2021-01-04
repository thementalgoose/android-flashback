package tmg.flashback.ui.utils.bottomsheet

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.ui.utils.StringHolder

data class BottomSheetItem(
    val id: Int,
    @DrawableRes
    val image: Int? = null,
    val text: StringHolder
)