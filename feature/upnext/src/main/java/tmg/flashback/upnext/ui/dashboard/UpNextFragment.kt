package tmg.flashback.upnext.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.upnext.R
import tmg.flashback.upnext.databinding.FragmentUpNextBinding
import tmg.flashback.upnext.ui.timezone.TimezoneAdapter
import tmg.utilities.extensions.observe

class UpNextFragment: BaseFragment<FragmentUpNextBinding>() {

    private val viewModel: UpNextViewModel by viewModel()

    private lateinit var upNextAdapter: UpNextBreakdownAdapter
    private lateinit var timezoneAdapter: TimezoneAdapter

    override fun inflateView(inflater: LayoutInflater) = FragmentUpNextBinding
        .inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upNextAdapter = UpNextBreakdownAdapter()
        binding.content.layoutManager = LinearLayoutManager(context)
        binding.content.adapter = upNextAdapter

        timezoneAdapter = TimezoneAdapter()
        binding.timezone.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.timezone.adapter = timezoneAdapter

        observe(viewModel.outputs.content) {
            upNextAdapter.list = it
        }

        observe(viewModel.outputs.data) { schedule ->
            val trackLayout = TrackLayout.values().firstOrNull { it.circuitId == schedule.circuitId } ?: TrackLayout.getOverride(schedule.season, schedule.title)
            binding.track.setImageResource(trackLayout?.icon ?: R.drawable.circuit_unknown)
            if (context != null && schedule.flag != null) {
                binding.flag.setImageResource(requireContext().getFlagResourceAlpha3(schedule.flag))
            }
            binding.title.text = schedule.title
            binding.subtitle.text = schedule.subtitle
        }

        observe(viewModel.outputs.timezones) {
            timezoneAdapter.list = it
        }
    }

    fun refresh() {
        viewModel.inputs.refresh()
    }
}