package tmg.flashback.ui.settings.customisation.animation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_bottom_sheet_theme.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.ui.BaseBottomSheetFragment
import tmg.flashback.core.ui.bottomsheet.BottomSheetAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class AnimationSpeedBottomSheetFragment: BaseBottomSheetFragment() {

    private val viewModel: AnimationSpeedViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

    override fun layoutId() = R.layout.fragment_bottom_sheet_animation_speed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BottomSheetAdapter(
                itemClicked = {
                    viewModel.inputs.selectAnimationSpeed(AnimationSpeed.values()[it.id])
                }
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.animationSpeedPreference) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.animationSpeedUpdated) {
            dismiss()
        }
    }
}