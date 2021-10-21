package tmg.flashback.statistics.ui.overview.driver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.flashback.statistics.databinding.FragmentDriverBinding
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonActivity
import tmg.flashback.statistics.ui.overview.driver.summary.DriverSummaryAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class DriverFragment: BaseFragment<FragmentDriverBinding>() {

    private lateinit var adapter: DriverSummaryAdapter

    private lateinit var driverId: String
    private lateinit var driverName: String
    private val viewModel: DriverViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            driverId = it.getString(keyDriverId)!!
            driverName = it.getString(keyDriverName)!!
            viewModel.inputs.setup(driverId)
        }
    }

    override fun inflateView(inflater: LayoutInflater) =
            FragmentDriverBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("Driver Overview", mapOf(
                "driver_id" to driverId,
                "driver_name" to driverName,
        ))

        binding.titleExpanded.text = driverName
        binding.titleCollapsed.text = driverName

        binding.swipeRefresh.isEnabled = false
        adapter = DriverSummaryAdapter(
                openUrl = viewModel.inputs::openUrl,
                seasonClicked = viewModel.inputs::openSeason
        )
        binding.dataList.adapter = adapter
        binding.dataList.layoutManager = LinearLayoutManager(context)

        binding.back.setOnClickListener {
            activity?.finish()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openSeason) { (driverId, season) ->
            context?.let { context ->
                val intent = DriverSeasonActivity.intent(context, driverId, driverName, season)
                startActivity(intent)
            }
        }

        observeEvent(viewModel.outputs.openUrl) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }
    }

    companion object {

        private const val keyDriverId: String = "keyDriverId"
        private const val keyDriverName: String = "keyDriverName"

        fun instance(driverId: String, driverName: String): DriverFragment {
            return DriverFragment().apply {
                arguments = bundleOf(
                        keyDriverId to driverId,
                        keyDriverName to driverName
                )
            }
        }
    }
}