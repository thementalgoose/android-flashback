package tmg.flashback.settings.planned

import androidx.annotation.StringRes

data class PlannedItems(
    @StringRes val title: Int,
    val isDone: Boolean
)