package tmg.flashback.ui.dashboard.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class MenuButtonItem {
    data class Button(
        @StringRes
        val label: Int,
        @DrawableRes
        val icon: Int
    ): MenuButtonItem()

    data class Toggle(
        @StringRes
        val label: Int,
        @DrawableRes
        val icon: Int,
        val isEnabled: Boolean
    ): MenuButtonItem()
}