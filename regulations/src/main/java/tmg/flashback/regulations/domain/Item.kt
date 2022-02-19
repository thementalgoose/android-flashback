package tmg.flashback.regulations.domain

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.regulations.R
import tmg.utilities.models.StringHolder

internal sealed class Item(
    @LayoutRes
    val layoutId: Int
) {
    data class Tyres(
        val tyreAllocation: List<TyreAllocation>
    ): Item(R.layout.view_format_tyres)

    data class Stat(
        @StringRes
        val label: Int,
        val value: StringHolder = StringHolder(""),
        @DrawableRes
        val icon: Int
    ): Item(R.layout.view_format_stat)

    data class ProgressBar(
        val label: StringHolder,
        val initial: Int,
        val final: Int,
        val progress: Int
    ): Item(R.layout.view_format_progress_bar)

    object Spacer: Item(R.layout.view_format_spacer)

    data class Determines(
        @StringRes
        val from: Int,
        @StringRes
        val to: Int,
    ): Item(R.layout.view_format_led_to)

    data class Text(
        @StringRes
        val label: Int
    ): Item(R.layout.view_format_text)

    data class Collapsible(
        @StringRes
        val label: Int,
        val expanded: Boolean
    ): Item(R.layout.view_format_collapsible)

    data class Header(
        @StringRes
        val label: Int
    ): Item(R.layout.view_format_header)

    data class SubHeader(
        @StringRes
        val label: Int
    ): Item(R.layout.view_format_subheader)
}