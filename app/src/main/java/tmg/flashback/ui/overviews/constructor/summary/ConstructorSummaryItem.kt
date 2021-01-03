package tmg.flashback.ui.overviews.constructor.summary

import androidx.annotation.*
import tmg.flashback.R
import tmg.flashback.ui.overviews.driver.summary.PipeType
import tmg.flashback.repo.models.stats.ConstructorOverviewDriverStanding
import tmg.flashback.ui.shared.sync.SyncDataItem

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

    data class History(
            val pipe: PipeType,
            val season: Int,
            val isInProgress: Boolean,
            val championshipPosition: Int,
            val points: Int,
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