package tmg.flashback.ui.settings.customisation

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.bottom_sheet_animation.*
import kotlinx.android.synthetic.main.bottom_sheet_theme.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.enums.Theme
import tmg.flashback.core.ui.settings.SettingsFragment
import tmg.flashback.ui.utils.bottomsheet.BottomSheetAdapter
import tmg.utilities.bottomsheet.BottomSheetFader
import tmg.utilities.extensions.expand
import tmg.utilities.extensions.hidden
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class SettingsCustomisationFragment: SettingsFragment() {

    private val viewModel: SettingsCustomisationViewModel by viewModel()

    private lateinit var themeBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var animationBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var themeAdapter: BottomSheetAdapter
    private lateinit var animationAdapter: BottomSheetAdapter

    override val prefClicked: (prefKey: String) -> Unit = { prefKey ->
        viewModel.inputs.preferenceClicked(prefKey, null)
    }
    override val prefSwitchClicked: (prefKey: String, newState: Boolean) -> Unit = { prefKey, newState ->
        viewModel.inputs.preferenceClicked(prefKey, newState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.outputs.settings) {
            adapter.list = it
        }

        observe(viewModel.outputs.themePreferences) {
            themeAdapter.list = it
        }
        observeEvent(viewModel.outputs.themeOpenPicker) {
            themeBottomSheet.expand()
        }
        observeEvent(viewModel.outputs.themeUpdated) {

        }

        observe(viewModel.outputs.animationSpeedPreference) {
            animationAdapter.list = it
        }
        observeEvent(viewModel.outputs.animationSpeedOpenPicker) {
            animationBottomSheet.expand()
        }
        observeEvent(viewModel.outputs.animationSpeedUpdated) {

        }

        observe(viewModel.outputs.animationSpeedPreference) {
            animationAdapter.list = it
        }

        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        setupBottomSheetTheme()
        setupBottomSheetAnimation()

        vBackground.setOnClickListener {
            themeBottomSheet.hidden()
            animationBottomSheet.hidden()
        }
    }

    private fun setupBottomSheetTheme() {
        themeAdapter = BottomSheetAdapter(
            itemClicked = {
                viewModel.inputs.selectTheme(Theme.values()[it.id])
            }
        )
        textList.adapter = themeAdapter
        textList.layoutManager = LinearLayoutManager(context)

        themeBottomSheet = BottomSheetBehavior.from(parentLayout)
        themeBottomSheet.isHideable = true
        themeBottomSheet.hidden()
        themeBottomSheet.addBottomSheetCallback(BottomSheetFader(vBackground, "theme"))
    }

    private fun setupBottomSheetAnimation() {
        animationAdapter = BottomSheetAdapter(
            itemClicked = {
                viewModel.inputs.selectAnimationSpeed(AnimationSpeed.values()[it.id])
            }
        )

        animationList.adapter = animationAdapter
        animationList.layoutManager = LinearLayoutManager(context)

        animationBottomSheet = BottomSheetBehavior.from(animationLayout)
        animationBottomSheet.isHideable = true
        animationBottomSheet.hidden()
        animationBottomSheet.addBottomSheetCallback(BottomSheetFader(vBackground, "animation"))
    }

}