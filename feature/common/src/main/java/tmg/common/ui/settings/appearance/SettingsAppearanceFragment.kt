package tmg.common.ui.settings.appearance

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.ui.settings.appearance.animation.AnimationSpeedBottomSheetFragment
import tmg.common.ui.settings.appearance.nightmode.NightModeBottomSheetFragment
import tmg.common.ui.settings.appearance.theme.ThemeBottomSheetFragment
import tmg.core.ui.navigation.NavigationProvider
import tmg.core.ui.settings.SettingsFragment
import tmg.utilities.extensions.observeEvent

class SettingsAppearanceFragment: SettingsFragment<SettingsAppearanceViewModel>(),
    FragmentResultListener {

    override val viewModel: SettingsAppearanceViewModel by viewModel()

    private val navigationProvider: NavigationProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Appearance")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listen for category callback
        parentFragmentManager.setFragmentResultListener(
            requestKey,
            viewLifecycleOwner,
            this
        )

        observeEvent(viewModel.outputs.openNightMode) {
            val themeBottomSheetFragment = NightModeBottomSheetFragment()
            themeBottomSheetFragment.show(parentFragmentManager, "NIGHT_MODE")
        }

        observeEvent(viewModel.outputs.openTheme) {
            val themeBottomSheetFragment = ThemeBottomSheetFragment()
            themeBottomSheetFragment.show(parentFragmentManager, "THEME")
        }

        observeEvent(viewModel.outputs.openAnimationSpeed) {
            val animationSpeedBottomSheetFragment = AnimationSpeedBottomSheetFragment()
            animationSpeedBottomSheetFragment.show(parentFragmentManager, "ANIMTION")
        }
    }

    companion object {
        val requestKey: String = "themeKey"
        val bundleKey: String = "resetApp"
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        activity?.let { activity ->
            when (requestKey) {
                requestKey -> {
                    if (result.getBoolean(bundleKey)) {
                        activity.finishAffinity()
                        startActivity(navigationProvider.relaunchAppIntent(activity))
                    }
                }
            }
        }
    }
}