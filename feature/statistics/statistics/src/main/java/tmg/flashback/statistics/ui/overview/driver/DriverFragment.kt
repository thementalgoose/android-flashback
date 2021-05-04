package tmg.flashback.statistics.ui.overview.driver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.shared.ui.base.BaseFragment
import tmg.flashback.statistics.databinding.FragmentDriverSeasonBinding
import tmg.flashback.statistics.ui.overview.driver.summary.DriverSummaryAdapter
import tmg.utilities.extensions.observe

class DriverFragment: BaseFragment<FragmentDriverSeasonBinding>() {

    private lateinit var adapter: DriverSummaryAdapter

    private val viewModel: DriverViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val driverId: String = it.getString(keyDriverId)!!
            viewModel.inputs.setup(driverId)
        }
    }

    override fun inflateView(inflater: LayoutInflater) =
        FragmentDriverSeasonBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DriverSummaryAdapter(
                openUrl = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    startActivity(intent)
                }
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.list) {
            adapter.list = it
        }
    }

    companion object {

        private const val keyDriverId: String = "keyDriverId"

        fun instance(driverId: String): DriverFragment {
            val fragment = DriverFragment()
            val bundle = Bundle().apply {
                putString(keyDriverId, driverId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}