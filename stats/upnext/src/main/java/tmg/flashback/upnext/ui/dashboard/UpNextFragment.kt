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
import tmg.flashback.ui.base.BaseFragment
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.upnext.BuildConfig
import tmg.flashback.upnext.R
import tmg.flashback.upnext.databinding.FragmentUpNextBinding
import tmg.flashback.upnext.ui.timezone.TimezoneAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.views.show

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
            val trackLayout = TrackLayout.getTrack(schedule.circuitId, schedule.season, schedule.raceName)
            binding.track.setImageResource(trackLayout?.icon ?: R.drawable.circuit_unknown)
            if (context != null) {
                binding.flag.setImageResource(requireContext().getFlagResourceAlpha3(schedule.countryISO))
            }
            if (schedule.season == 0) {
                binding.title.text = schedule.raceName
                binding.subtitle.text = schedule.circuitName
            } else {
                binding.title.text = "${schedule.season} ${schedule.raceName}"
                binding.subtitle.text = schedule.circuitName
            }
        }

        observe(viewModel.outputs.remainingDays) {
            binding.startsIn.show(it != 0)
            binding.startsIn.text = resources.getQuantityString(R.plurals.dashboard_up_next_starts_in, it, it)
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