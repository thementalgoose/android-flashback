package tmg.flashback.di

import androidx.appcompat.app.AppCompatActivity
import org.threeten.bp.LocalDate
import tmg.flashback.statistics.databinding.FragmentBottomSheetNotificationsOnboardingBinding
import tmg.flashback.statistics.ui.dashboard.onboarding.OnboardingNotificationBottomSheetFragment
import tmg.flashback.statistics.ui.overview.constructor.ConstructorActivity
import tmg.flashback.statistics.ui.overview.driver.DriverActivity
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.flashback.statistics.ui.race.RaceData
import tmg.flashback.statistics.ui.search.SearchActivity
import tmg.flashback.statistics.ui.shared.tyres.TyresBottomSheetFragment
import tmg.flashback.stats.di.StatsNavigator
import tmg.flashback.ui.navigation.ActivityProvider

@Deprecated("This is temporary whilst migrating to Jetpack Compose")
class StatsNavigatorImpl(
    private val activityProvider: ActivityProvider
): StatsNavigator {

    val activity: AppCompatActivity?
        get() = (activityProvider.activity as? AppCompatActivity)

    override fun goToSearch() {
        val activity = activityProvider.activity ?: return
        val intent = SearchActivity.intent(activity)
        activity.startActivity(intent)
    }

    override fun goToRace(
        season: Int,
        round: Int,
        circuitId: String,
        defaultToRace: Boolean,
        country: String,
        raceName: String,
        trackName: String,
        countryISO: String,
        date: LocalDate
    ) {
        val activity = activity ?: return

        val raceData = RaceData(
            season = season,
            round = round,
            circuitId = circuitId,
            defaultToRace = defaultToRace,
            country = country,
            raceName = raceName,
            trackName = trackName,
            countryISO = countryISO,
            date = date
        )
        val intent = RaceActivity.intent(activity, raceData = raceData)
        activity.startActivity(intent)
    }

    override fun goToTyreOverview(season: Int) {
        val activity = activity ?: return
        val fragment = TyresBottomSheetFragment.instance(season)
        fragment.show(activity.supportFragmentManager, "TYRES")
    }

    override fun goToNotificationOnboarding() {
        val activity = activity ?: return
        val fragment = OnboardingNotificationBottomSheetFragment()
        fragment.show(activity.supportFragmentManager, "ONBOARDING")
    }

    override fun goToDriverOverview(driverId: String, driverName: String) {
        val activity = activity ?: return

        val intent = DriverActivity.intent(
            context = activity,
            driverId = driverId,
            driverName = driverName
        )
        activity.startActivity(intent)
    }

    override fun goToConstructorOverview(constructorId: String, constructorName: String) {
        val activity = activity ?: return

        val intent = ConstructorActivity.intent(
            context = activity,
            constructorId = constructorId,
            constructorName = constructorName
        )
        activity.startActivity(intent)
    }
}