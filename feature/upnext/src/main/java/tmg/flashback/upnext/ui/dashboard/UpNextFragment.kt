package tmg.flashback.upnext.ui.dashboard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.upnext.BuildConfig
import tmg.flashback.upnext.R
import tmg.flashback.upnext.databinding.FragmentUpNextBinding
import tmg.flashback.upnext.ui.timezone.TimezoneAdapter
import tmg.utilities.extensions.observe

class UpNextFragment: BaseFragment<FragmentUpNextBinding>() {

    private val viewModel: UpNextViewModel by viewModel()

    private var upNextAdapter: UpNextBreakdownAdapter? = null
    private lateinit var timezoneAdapter: TimezoneAdapter

    @Suppress("RedundantNullableReturnType")
    private val tickReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (BuildConfig.DEBUG) {
                Log.i("Flashback", "Broadcast Receiver tick for time update")
            }
            viewModel.inputs.refresh()
        }
    }

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
            upNextAdapter?.list = it
        }

        observe(viewModel.outputs.data) { schedule ->
            val trackLayout = TrackLayout.values().firstOrNull { it.circuitId == schedule.circuitId } ?: TrackLayout.getOverride(schedule.season, schedule.title)
            binding.track.setImageResource(trackLayout?.icon ?: R.drawable.circuit_unknown)
            if (context != null && schedule.flag != null) {
                binding.flag.setImageResource(requireContext().getFlagResourceAlpha3(schedule.flag))
            }
            if (schedule.season == 0) {
                binding.title.text = schedule.title
                binding.subtitle.text = schedule.subtitle
            } else {
                binding.title.text = "${schedule.season} ${schedule.title}"
                binding.subtitle.text = schedule.subtitle
            }
        }

        observe(viewModel.outputs.timezones) {
            timezoneAdapter.list = it
        }
    }

    override fun onPause() {
        super.onPause()
        if (tickReceiver != null) {
            context?.unregisterReceiver(tickReceiver)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.inputs.refresh()
        context?.registerReceiver(tickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    /**
     * Refresh called externally from the fragment
     */
    fun refresh() {
        viewModel.inputs.refresh()
    }
}