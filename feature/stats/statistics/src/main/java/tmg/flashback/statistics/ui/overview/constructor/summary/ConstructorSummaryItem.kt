package tmg.flashback.statistics.ui.overview.constructor.summary

import androidx.annotation.*
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType
import tmg.flashback.formula1.model.ConstructorOverviewDriverStanding
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem

sealed class ConstructorSummaryItem(
        @LayoutRes val layoutId: Int
) {
    data class Header(
            val constructorName: String,
            val constructorColor: Int,
            val constructorNationality: String,
            val constructorNationalityISO: String,
            val constructorWikiUrl: String?
    ): ConstructorSummaryItem(
            R.layout.view_constructor_summary_header
    )

    data class Stat(
            @AttrRes
            val tint: Int = R.attr.contentSecondary,
            @DrawableRes
            val icon: Int,
            @StringRes
            val label: Int,
            val value: String
    ): ConstructorSummaryItem(
            R.layout.view_overview_stat
    )

    data class History(
        val pipe: PipeType,
        val season: Int,
        val isInProgress: Boolean,
        val championshipPosition: Int?,
        val points: Double,
        @ColorInt
        val colour: Int,
        val drivers: List<ConstructorOverviewDriverStanding>
    ): ConstructorSummaryItem(
            R.layout.view_constructor_summary_history
    )

    data class ErrorItem(
            val item: SyncDataItem
    ): ConstructorSummaryItem(item.layoutId)

    object Loading: ConstructorSummaryItem(R.layout.view_loading_podium)

    object ListHeader: ConstructorSummaryItem(R.layout.view_constructor_summary_list_header)
}

fun MutableList<ConstructorSummaryItem>.addError(syncDataItem: SyncDataItem) {
    this.add(
        ConstructorSummaryItem.ErrorItem(
            syncDataItem
        )
    )
}