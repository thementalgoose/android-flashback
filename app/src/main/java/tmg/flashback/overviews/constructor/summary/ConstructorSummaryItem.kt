package tmg.flashback.overviews.constructor.summary

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.shared.SyncDataItem

sealed class ConstructorSummaryItem(
        @LayoutRes val layoutId: Int
) {
    data class Header(
            val constructorName: String,
            val constructorColor: Int,
            val constructorNationality: String,
            val constructorNationalityISO: String,
            val constructorWikiUrl: String
    ): ConstructorSummaryItem(
            R.layout.view_constructor_summary_header
    )

    data class Stat(
            @AttrRes
            val tint: Int = R.attr.f1TextSecondary,
            @DrawableRes
            val icon: Int,
            @StringRes
            val label: Int,
            val value: String
    ): ConstructorSummaryItem(
            R.layout.view_overview_stat
    )

    data class ErrorItem(
            val item: SyncDataItem
    ): ConstructorSummaryItem(item.layoutId)

    object Loading: ConstructorSummaryItem(R.layout.view_loading_podium)
}

fun MutableList<ConstructorSummaryItem>.addError(syncDataItem: SyncDataItem) {
    this.add(
            ConstructorSummaryItem.ErrorItem(
                    syncDataItem
            )
    )
}