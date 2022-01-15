package tmg.flashback.statistics.ui.search.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.ui.base.BaseBottomSheetFragment
import tmg.flashback.ui.bottomsheet.BottomSheetAdapter
import tmg.flashback.statistics.databinding.FragmentBottomSheetCategoryBinding
import tmg.flashback.statistics.ui.search.SearchCategory
import tmg.flashback.statistics.ui.search.SearchFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.toEnum

class CategoryBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetCategoryBinding>() {

    private val viewModel: CategoryViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

    private var searchCategory: SearchCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchCategory = it.getInt(keyCategory).toEnum<SearchCategory>()
        }
    }

    override fun inflateView(inflater: LayoutInflater) = FragmentBottomSheetCategoryBinding
        .inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BottomSheetAdapter(
            itemClicked = {
                viewModel.inputs.clickCategory(SearchCategory.values()[it.id])
            }
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.categories) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.categorySelected) {
            val result = bundleOf(
                SearchFragment.keySearchCategory to it.ordinal
            )
            setFragmentResult(SearchFragment.keySearchCategory, result)
            dismiss()
        }

        viewModel.inputs.initList(searchCategory)
    }

    companion object {

        private const val keyCategory = "keyCategory"

        fun instance(selectedCategory: SearchCategory?): CategoryBottomSheetFragment {
            val arguments = bundleOf(
                keyCategory to (selectedCategory?.ordinal ?: -1)
            )
            return CategoryBottomSheetFragment()
                .apply {
                    this.arguments = arguments
                }
        }
    }
}