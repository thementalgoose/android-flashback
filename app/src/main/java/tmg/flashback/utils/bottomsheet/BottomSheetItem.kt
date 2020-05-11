package tmg.flashback.utils.bottomsheet

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomSheetItem(
    val id: Int,
    @DrawableRes val image: Int,
    @StringRes val text: Int
)