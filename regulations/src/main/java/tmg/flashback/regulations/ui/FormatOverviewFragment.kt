package tmg.flashback.regulations.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.regulations.databinding.FragmentFormatOverviewBinding
import tmg.flashback.regulations.ui.items.ItemsAdapter
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.observe
import tmg.utilities.lifecycle.viewInflateBinding

internal class FormatOverviewFragment: BaseFragment() {

    private val viewModel: FormatOverviewViewModel by viewModel()
    private val binding by viewInflateBinding(FragmentFormatOverviewBinding::inflate)

    private lateinit var adapter: ItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.isEnabled = false

        adapter = ItemsAdapter(
            setSection = viewModel.inputs::setSection
        )
        binding.dataList.adapter = adapter
        binding.dataList.layoutManager = LinearLayoutManager(context)

        val season = arguments?.getInt(keySeason) ?: run {
            return
        }
        viewModel.inputs.init(season)

        observe(viewModel.outputs.items) {
            adapter.list = it
        }
    }

    companion object {

        private const val keySeason = "season"

        fun instance(season: Int): FormatOverviewFragment {
            return FormatOverviewFragment().apply {
                arguments = bundleOf(keySeason to season)
            }
        }
    }
}