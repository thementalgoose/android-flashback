package tmg.flashback.ui2.dashboard.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.DebugController
import tmg.flashback.R
import tmg.flashback.RssNavigationComponent
import tmg.flashback.databinding.FragmentDashboardListBinding
import tmg.flashback.regulations.ui.FormatOverviewActivity
import tmg.flashback.statistics.ui.dashboard.onboarding.OnboardingNotificationBottomSheetFragment
import tmg.flashback.ui.base.BaseFragment
import tmg.flashback.ui.navigation.ApplicationNavigationComponent
import tmg.flashback.ui2.dashboard.DashboardNavigationCallback
import tmg.flashback.ui.settings.SettingsAllActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.lifecycle.viewInflateBinding

class ListFragment: BaseFragment() {

    private val viewModel: ListViewModel by viewModel()
    private val binding by viewInflateBinding(FragmentDashboardListBinding::inflate)

    private val applicationNavigationComponent: ApplicationNavigationComponent by inject()
    private val debugController: DebugController by inject()
    private val rssNavigationComponent: RssNavigationComponent by inject()

    private var adapter: ListAdapter? = null
    private val dashboardNavigationCallback: DashboardNavigationCallback?
        get() = parentFragment as? DashboardNavigationCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListAdapter(
                featureToggled = viewModel.inputs::toggleHeader,
                favouriteToggled = viewModel.inputs::toggleFavourite,
                seasonClicked = viewModel.inputs::clickSeason,
                setDefaultClicked = viewModel.inputs::clickSetDefaultSeason,
                clearDefaultClicked = viewModel.inputs::clickClearDefaultSeason,
                buttonClicked = {
                     when (it) {
                         "rss" -> viewModel.inputs.clickRss()
                         "settings" -> viewModel.inputs.clickSettings()
                         "contact" -> viewModel.inputs.clickContact()
                         "debug_list_item" -> {
                             context?.let { debugController.goToDebugActivity(it) }
                         }
                         "dark_mode" -> {
                            viewModel.inputs.toggleDarkMode()
                         }
                     }
                },
                showFormatInfo = { season ->
                    context?.let { context ->
                        val intent = FormatOverviewActivity.intent(context, season)
                        context.startActivity(intent)
                    }
                },
                featureBannerClicked = {
                    viewModel.inputs.clickFeatureBanner(it)
                }
        )
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter

        observe(viewModel.outputs.list) {
            adapter?.list = it
        }

        observeEvent(viewModel.outputs.showSeasonEvent) { season ->
            dashboardNavigationCallback?.let {
                it.seasonSelected(season)
                it.closeSeasonList()
            }
        }

        observeEvent(viewModel.outputs.openSettings) {
            context?.let {
                startActivity(Intent(it, SettingsAllActivity::class.java))
            }
        }

        observeEvent(viewModel.outputs.openRss) {
            context?.let {
                rssNavigationComponent.rss()
            }
        }

        observeEvent(viewModel.outputs.openContact) {
            context?.let {
                startActivity(applicationNavigationComponent.aboutAppIntent(it))
            }
        }

        observeEvent(viewModel.outputs.openNotificationsOnboarding) {
            OnboardingNotificationBottomSheetFragment()
                .show(parentFragmentManager, "FEATURE_BANNER_ONBOARDING")
        }

        setFragmentResultListener("FEATURE_BANNER_ONBOARDING") { _, _ ->
            Snackbar.make(binding.root, getString(R.string.feature_banner_notifications_finished), 4000).show()
        }

        observeEvent(viewModel.outputs.defaultSeasonUpdated) {
            when (it) {
                null -> Snackbar
                        .make(binding.list, getString(R.string.dashboard_season_list_default_banner_automatic), 4000)
                        .show()
                else -> Snackbar
                        .make(binding.list, getString(R.string.dashboard_season_list_default_banner_user, it), 4000)
                        .setAction(R.string.dashboard_season_list_default_banner_revert) {
                            viewModel.inputs.clickClearDefaultSeason()
                        }
                        .show()
            }
        }
    }

    fun refresh() {
        viewModel.inputs.refresh()
    }
}