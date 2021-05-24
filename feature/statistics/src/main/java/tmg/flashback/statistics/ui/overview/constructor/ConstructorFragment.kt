package tmg.flashback.statistics.ui.overview.constructor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.flashback.statistics.databinding.FragmentConstructorBinding
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ConstructorFragment: BaseFragment<FragmentConstructorBinding>() {

    private val viewModel: ConstructorViewModel by viewModel()

    private lateinit var constructorId: String
    private lateinit var constructorName: String
    private lateinit var adapter: ConstructorSummaryAdapter

    override fun inflateView(inflater: LayoutInflater) =
            FragmentConstructorBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            constructorId = it.getString(keyConstructorId)!!
            constructorName = it.getString(keyConstructorName)!!

            viewModel.inputs.setup(constructorId)
        }

        logScreenViewed("Constructor Overview", mapOf(
                "constructor_id" to constructorId,
                "constructor_name" to constructorName
        ))

        binding.titleExpanded.text = constructorName
        binding.titleCollapsed.text = constructorName

        binding.swipeRefresh.isEnabled = false
        adapter = ConstructorSummaryAdapter(
                openUrl = viewModel.inputs::openUrl,
                openSeason = viewModel.inputs::openSeason
        )
        binding.dataList.adapter = adapter
        binding.dataList.layoutManager = LinearLayoutManager(context)

        binding.back.setOnClickListener {
            activity?.finish()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openUrl) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }

        observeEvent(viewModel.outputs.openSeason) {

        }
    }

    companion object {
        private const val keyConstructorId: String = "constructorId"
        private const val keyConstructorName: String = "constructorName"

        fun instance(constructorId: String, constructorName: String): ConstructorFragment {
            return ConstructorFragment().apply {
                arguments = bundleOf(
                        keyConstructorId to constructorId,
                        keyConstructorName to constructorName
                )
            }
        }
    }
}