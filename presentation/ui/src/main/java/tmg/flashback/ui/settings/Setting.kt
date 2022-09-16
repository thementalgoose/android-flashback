package tmg.flashback.ui.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class Setting(
    val key: String
) {
    data class Heading(
        private val _key: String,
        @StringRes
        val title: Int,
        val isBeta: Boolean = false,
    ): Setting(
        key = _key
    ) {
        companion object
    }

    data class Section(
        private val _key: String,
        @StringRes
        val title: Int,
        @StringRes
        val subtitle: Int,
        @DrawableRes
        val icon: Int,
        val isEnabled: Boolean = true,
        val isBeta: Boolean = false
    ): Setting(
        key = _key
    ) {
        companion object
    }

    data class Pref(
        private val _key: String,
        @StringRes
        val title: Int,
        @StringRes
        val subtitle: Int?,
        val isEnabled: Boolean = true,
        val isBeta: Boolean = false,
    ): Setting(
        key = _key
    ) {
        companion object
    }

    data class Switch(
        private val _key: String,
        @StringRes
        val title: Int,
        @StringRes
        val subtitle: Int?,
        val isChecked: Boolean,
        val isEnabled: Boolean = true,
        val isBeta: Boolean = false,
    ): Setting(
        key = _key
    ) {
        companion object
    }
}