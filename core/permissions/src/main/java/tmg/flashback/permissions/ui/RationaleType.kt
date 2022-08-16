package tmg.flashback.permissions.ui

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import tmg.flashback.permissions.R

enum class RationaleType(
    @StringRes
    val description: Int,
    @StringRes
    val title: Int = R.string.permissions_title,
    @RawRes
    val raw: Int? = null
){
    RUNTIME_NOTIFICATIONS(
        description = R.string.permissions_rationale_notifications,
        raw = R.raw.notification
    )
}