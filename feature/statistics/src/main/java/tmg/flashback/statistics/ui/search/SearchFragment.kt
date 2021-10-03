package tmg.flashback.statistics.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.FragmentSearchBinding
import tmg.flashback.statistics.ui.search.category.CategoryBottomSheetFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.toEnum

class SearchFragment: BaseFragment<FragmentSearchBinding>(), FragmentResultListener {

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var adapter: SearchAdapter

    override fun inflateView(inflater: LayoutInflater) = FragmentSearchBinding
        .inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("Search")

        binding.swipeRefresh.isEnabled = false

        binding.back.setOnClickListener {
            activity?.finish()
        }

        adapter = SearchAdapter()
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        binding.type.setOnClickListener {
            viewModel.inputs.openCategory()
        }
        // Listen for category callback
        parentFragmentManager.setFragmentResultListener(
            keySearchCategory,
            viewLifecycleOwner,
            this
        )

        observeEvent(viewModel.outputs.openCategoryPicker) {
            val instance = CategoryBottomSheetFragment.instance(it)
            instance.show(parentFragmentManager, "CATEGORY")
        }

        observe(viewModel.outputs.selectedCategory) {
            when (it) {
                null -> binding.type.setText(R.string.search_category_hint)
                else -> binding.type.setText(it.label)
            }
        }

        observe(viewModel.outputs.results) {
            adapter.list = it
        }
    }

    companion object {
        const val keySearchCategory = "searchCategory"
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            keySearchCategory -> {
                val category = result.getInt(keySearchCategory).toEnum<SearchCategory>()
                if (category != null) {
                    viewModel.inputs.inputCategory(category)
                }
            }
        }
    }
}