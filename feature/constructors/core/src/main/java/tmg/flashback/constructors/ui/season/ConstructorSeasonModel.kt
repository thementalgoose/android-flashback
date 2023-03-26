package tmg.flashback.constructors.ui.season

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.formula1.model.ConstructorHistorySeasonDriver

sealed class ConstructorSeasonModel(
    val key: String
) {
    data class Header(
        val constructorName: String,
        val constructorPhotoUrl: String?,
        val constructorColor: Int,
        val constructorNationality: String,
        val constructorNationalityISO: String,
        val constructorWikiUrl: String?
    ): ConstructorSeasonModel(
        key = "header-top"
    )

    data class Message(
        @StringRes
        val label: Int,
        val args: List<Any>
    ): ConstructorSeasonModel(
        key = "message-${label}"
    )

    data class Stat(
        val isWinning: Boolean,
        @DrawableRes
        val icon: Int,
        @StringRes
        val label: Int,
        val value: String
    ): ConstructorSeasonModel(
        key = "stat-$label"
    )

    data class Driver(
        val data: ConstructorHistorySeasonDriver
    ): ConstructorSeasonModel(
        key = "driver-${data.driver.driver.id}"
    )

    object NetworkError: ConstructorSeasonModel(
        key = "network-error"
    )
    object InternalError: ConstructorSeasonModel(
        key = "internal-error"
    )
    object Loading: ConstructorSeasonModel(
        key = "loading"
    )

    companion object
}