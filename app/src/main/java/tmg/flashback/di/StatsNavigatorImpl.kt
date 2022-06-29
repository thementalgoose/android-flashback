package tmg.flashback.di

import androidx.appcompat.app.AppCompatActivity
import org.threeten.bp.LocalDate
import tmg.flashback.statistics.databinding.FragmentBottomSheetNotificationsOnboardingBinding
import tmg.flashback.statistics.ui.circuit.CircuitActivity
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

    private val activity: AppCompatActivity?
        get() = (activityProvider.activity as? AppCompatActivity)

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
}