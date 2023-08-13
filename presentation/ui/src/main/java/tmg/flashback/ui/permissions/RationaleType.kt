package tmg.flashback.ui.permissions

import android.Manifest
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import tmg.flashback.ui.R

@Parcelize
sealed class RationaleType(
    val permission: String,
    @StringRes
    val description: Int,
    @StringRes
    val title: Int = R.string.permissions_rationale_title,
    @RawRes
    val raw: Int? = null
): Parcelable {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    data object RuntimeNotifications: RationaleType(
        permission = Manifest.permission.POST_NOTIFICATIONS,
        description = R.string.permissions_rationale_runtime_notifications_description,
        raw = R.raw.notifications
    )
}
