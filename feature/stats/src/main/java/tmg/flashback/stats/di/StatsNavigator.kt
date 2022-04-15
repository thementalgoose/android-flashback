package tmg.flashback.stats.di

import org.threeten.bp.LocalDate

@Deprecated("This is temporary whilst migrating to Jetpack Compose")
interface StatsNavigator {
    fun goToRace(
        season: Int,
        round: Int,
        circuitId: String,
        defaultToRace: Boolean,
        country: String,
        raceName: String,
        trackName: String,
        countryISO: String,
        date: LocalDate
    )

    fun goToDriverOverview(
        driverId: String,
        driverName: String
    )

    fun goToConstructorOverview(
        constructorId: String,
        constructorName: String
    )
}