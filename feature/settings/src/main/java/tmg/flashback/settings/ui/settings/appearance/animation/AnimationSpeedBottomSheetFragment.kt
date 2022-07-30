package tmg.flashback.settings.ui.settings.appearance.animation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.settings.databinding.FragmentBottomSheetAnimationSpeedBinding
import tmg.flashback.ui.base.BaseBottomSheetFragment
import tmg.flashback.ui.bottomsheet.BottomSheetAdapter
import tmg.flashback.ui.model.AnimationSpeed
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

@AndroidEntryPoint
internal class AnimationSpeedBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetAnimationSpeedBinding>() {

    private val viewModel: AnimationSpeedviewModel = viewModels()

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