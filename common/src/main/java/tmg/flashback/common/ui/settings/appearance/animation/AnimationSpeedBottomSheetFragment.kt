package tmg.flashback.common.ui.settings.appearance.animation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.common.databinding.FragmentBottomSheetAnimationSpeedBinding
import tmg.flashback.ui.model.AnimationSpeed
import tmg.flashback.ui.base.BaseBottomSheetFragment
import tmg.flashback.ui.bottomsheet.BottomSheetAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class AnimationSpeedBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetAnimationSpeedBinding>() {

    private val viewModel: AnimationSpeedViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Settings Appearance Animation Speed Picker")
    }

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetAnimationSpeedBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BottomSheetAdapter(
                itemClicked = {
                    viewModel.inputs.selectAnimationSpeed(AnimationSpeed.values()[it.id])
                }
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.animationSpeedPreference) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.animationSpeedUpdated) {
            dismiss()
        }
    }
}