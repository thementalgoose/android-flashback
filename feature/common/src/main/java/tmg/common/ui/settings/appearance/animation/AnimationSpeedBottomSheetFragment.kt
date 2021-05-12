package tmg.common.ui.settings.appearance.animation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.model.AnimationSpeed
import tmg.core.ui.base.BaseBottomSheetFragment
import tmg.core.ui.bottomsheet.BottomSheetAdapter
import tmg.core.ui.databinding.FragmentBottomSheetAnimationSpeedBinding
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class AnimationSpeedBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetAnimationSpeedBinding>() {

    private val viewModel: AnimationSpeedViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

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