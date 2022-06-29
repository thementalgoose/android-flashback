package tmg.flashback.stats.ui.constructors.overview

import androidx.annotation.*
import tmg.flashback.formula1.model.ConstructorHistorySeasonDriver
import tmg.flashback.stats.ui.drivers.overview.PipeType

sealed class ConstructorOverviewModel(
    val key: String
) {
    data class Header(
        val constructorName: String,
        val constructorColor: Int,
        val constructorNationality: String,
        val constructorNationalityISO: String,
        val constructorWikiUrl: String?
    ): ConstructorOverviewModel(
        key = "header-top"
    )

    data class Stat(
        val isWinning: Boolean,
        @DrawableRes
        val icon: Int,
        @StringRes
        val label: Int,
        val value: String
    ): ConstructorOverviewModel(
        key = "stat-$label"
    )

    data class Message(
        @StringRes
        val label: Int,
        val args: List<Any>
    ): ConstructorOverviewModel(
        key = "message-${label}"
    )

    data class History(
        val pipe: PipeType,
        val season: Int,
        val isInProgress: Boolean,
        val championshipPosition: Int?,
        val points: Double,
        val drivers: List<ConstructorHistorySeasonDriver>
    ): ConstructorOverviewModel(
        key = "history-$season"
    )

    object NetworkError: ConstructorOverviewModel(
        key = "network-error"
    )
    object InternalError: ConstructorOverviewModel(
        key = "internal-error"
    )

    object Loading: ConstructorOverviewModel(
        key = "loading"
    )

    object ListHeader: ConstructorOverviewModel(
        key = "list-header"
    )

    companion object
}