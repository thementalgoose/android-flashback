package tmg.flashback.statistics.ui.overview.driver.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.statistics.R
import tmg.flashback.ui.base.BaseFragment
import tmg.flashback.statistics.databinding.FragmentDriverBinding
import tmg.flashback.statistics.databinding.ViewSharedRefreshIndicatorBinding
import tmg.flashback.statistics.ui.overview.driver.season.DriverSeasonFragment
import tmg.flashback.statistics.ui.overview.driver.summary.DriverSummaryAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.viewUrl

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

        adapter = DriverSummaryAdapter(
                openUrl = viewModel.inputs::openUrl,
                seasonClicked = viewModel.inputs::openSeason
        )
        binding.dataList.adapter = adapter
        binding.dataList.layoutManager = LinearLayoutManager(context)
//        binding.progress.invisible()

        val x = ViewSharedRefreshIndicatorBinding.inflate(layoutInflater)
        binding.swipeRefresh.addView(x.root)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.inputs.refresh()
        }

        observe(viewModel.outputs.showLoading) {
            if (it) {
//                binding.progress.visible()
                binding.swipeRefresh.isRefreshing = false
            } else {
                binding.swipeRefresh.isRefreshing = false
//                binding.progress.invisible()
            }
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openSeason) { (driverId, season) ->
            val bundle = DriverSeasonFragment.bundle(season, driverId, driverName)
            findNavController().navigate(R.id.goToDriverSeason, bundle)
        }

        observeEvent(viewModel.outputs.openUrl) {
            viewUrl(it)
        }
    }

    companion object {

        private const val keyDriverId: String = "driverId"
        private const val keyDriverName: String = "driverName"

        fun bundle(driverId: String, driverName: String): Bundle {
            return bundleOf(
                keyDriverId to driverId,
                keyDriverName to driverName
            )
        }

        @Deprecated("Should be accessed via. a NavGraph")
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