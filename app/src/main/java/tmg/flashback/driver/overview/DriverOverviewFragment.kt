package tmg.flashback.driver.overview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_driver_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseFragment
import tmg.flashback.driver.season.DriverSeasonFragment
import tmg.flashback.repo.models.stats.DriverOverview
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class DriverOverviewFragment: BaseFragment() {

    private lateinit var adapter: DriverOverviewAdapter

    private val viewModel: DriverOverviewViewModel by viewModel()

    override fun layoutId(): Int = R.layout.fragment_driver_season

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        val driverId: String = bundle.getString(keyDriverId)!!
        viewModel.inputs.setup(driverId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DriverOverviewAdapter(
            openUrl = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.list) {
            adapter.list = it
        }
    }

    companion object {

        private const val keyDriverId: String = "keyDriverId"

        fun instance(driverId: String): DriverOverviewFragment {
            val fragment = DriverOverviewFragment()
            val bundle = Bundle().apply {
                putString(keyDriverId, driverId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}