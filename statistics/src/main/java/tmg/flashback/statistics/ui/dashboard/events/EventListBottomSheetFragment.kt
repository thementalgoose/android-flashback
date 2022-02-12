package tmg.flashback.statistics.ui.dashboard.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.formula1.enums.EventType
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.FragmentBottomSheetEventListBinding
import tmg.flashback.statistics.extensions.label
import tmg.flashback.ui.base.BaseBottomSheetFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.toEnum

class EventListBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetEventListBinding>() {

    private val viewModel: EventListViewModel by viewModel()

    private val adapter: EventListAdapter = EventListAdapter()

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetEventListBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val season = it.getInt(keySeason)
            val eventType = it.getInt(keyEventType).toEnum() ?: EventType.OTHER

            viewModel.inputs.show(season, eventType)

            binding.bottomSheetTitle.setText(eventType.label)
            binding.subtitle.text = when (eventType) {
                EventType.TESTING -> getString(R.string.dashboard_season_event_testing)
                EventType.CAR_LAUNCH -> getString(R.string.dashboard_season_event_car_launches)
                EventType.OTHER -> getString(R.string.dashboard_season_event_other)
            }
        }

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.list) {
            adapter.list = it
        }
    }

    companion object {

        private const val keySeason = "season"
        private const val keyEventType = "eventType"

        fun instance(season: Int, eventType: EventType): EventListBottomSheetFragment {
            return EventListBottomSheetFragment().apply {
                arguments = bundleOf(
                    keySeason to season,
                    keyEventType to eventType.ordinal
                )
            }
        }
    }
}