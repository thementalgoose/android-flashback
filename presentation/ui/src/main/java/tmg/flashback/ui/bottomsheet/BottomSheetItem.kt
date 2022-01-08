package tmg.flashback.ui.bottomsheet

import androidx.annotation.DrawableRes
import tmg.utilities.models.StringHolder

data class BottomSheetItem(
    val id: Int,
    @DrawableRes
    val image: Int? = null,
    val text: StringHolder
)