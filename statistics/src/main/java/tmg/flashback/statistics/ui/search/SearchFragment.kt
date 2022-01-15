package tmg.flashback.statistics.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.FragmentSearchBinding
import tmg.flashback.statistics.ui.circuit.CircuitActivity
import tmg.flashback.statistics.ui.overview.constructor.ConstructorActivity
import tmg.flashback.statistics.ui.overview.driver.DriverActivity
import tmg.flashback.statistics.ui.race.RaceActivity
import tmg.flashback.statistics.ui.race.RaceData
import tmg.flashback.statistics.ui.search.category.CategoryBottomSheetFragment
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.toEnum
import tmg.utilities.extensions.views.closeKeyboard

class SearchFragment: BaseFragment<FragmentSearchBinding>(), FragmentResultListener {

    private val viewModel: SearchViewModel by viewModel()

    var isLoading: Boolean = false
        set(value) {
            if (value) {
                binding.dataList.alpha = 0.5f
                binding.swipeRefresh.isRefreshing = true
            } else {
                binding.swipeRefresh.isRefreshing = false
                binding.dataList.alpha = 1.0f
            }
            field = value
        }

    private lateinit var adapter: SearchAdapter

    override fun inflateView(inflater: LayoutInflater) = FragmentSearchBinding
        .inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("Search")

        adapter = SearchAdapter(
            itemClicked = {
                stopInputFocus()
                viewModel.inputs.clickItem(it)
            }
        )
        binding.dataList.adapter = adapter
        binding.dataList.layoutManager = LinearLayoutManager(context)

        // Buttons / Inputs
        binding.back.setOnClickListener {
            activity?.finish()
        }
        binding.type.setOnClickListener {
            viewModel.inputs.openCategory()
        }
        binding.textInput.doOnTextChanged { text, _, _, _ ->
            viewModel.inputs.inputSearch(text.toString())
            binding.textClear.isEnabled = !text.isNullOrEmpty()
            binding.textClear.alpha = if (text.isNullOrEmpty()) 0.5f else 1.0f
        }
        binding.textClear.setOnClickListener {
            binding.textInput.setText("")
            stopInputFocus()
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.inputs.refresh()
        }

        // Clear input
        binding.textInput.setText("")

        // Listen for category callback
        parentFragmentManager.setFragmentResultListener(
            keySearchCategory,
            viewLifecycleOwner,
            this
        )

        observeEvent(viewModel.outputs.openCategoryPicker) {
            val instance = CategoryBottomSheetFragment.instance(it)
            stopInputFocus()
            instance.show(parentFragmentManager, "CATEGORY")
        }

        observe(viewModel.outputs.selectedCategory) {
            when (it) {
                null -> binding.type.setText(R.string.search_category_hint)
                else -> binding.type.setText(it.label)
            }
        }

        observe(viewModel.outputs.isLoading) {
            isLoading = it
        }

        observe(viewModel.outputs.results) {
            isLoading = false
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openLink) { searchItem ->
            context?.let { context ->
                val intent: Intent? = when (searchItem) {
                    is SearchItem.Circuit -> {
                        CircuitActivity.intent(
                            context = context,
                            circuitId = searchItem.circuitId,
                            circuitName = searchItem.name
                        )
                    }
                    is SearchItem.Constructor -> {
                        ConstructorActivity.intent(
                            context = context,
                            constructorId = searchItem.constructorId,
                            constructorName = searchItem.name
                        )
                    }
                    is SearchItem.Driver -> {
                        DriverActivity.intent(
                            context = context,
                            driverId = searchItem.driverId,
                            driverName = searchItem.name
                        )
                    }
                    is SearchItem.Race -> {
                        RaceActivity.intent(
                            context = context,
                            raceData = RaceData(
                                season = searchItem.season,
                                round = searchItem.round,
                                circuitId = searchItem.circuitId,
                                defaultToRace = true,
                                country = searchItem.country,
                                raceName = searchItem.raceName,
                                trackName = searchItem.circuitName,
                                countryISO = searchItem.countryISO,
                                date = searchItem.date,
                            )
                        )
                    }
                    else -> null
                }

                if (intent != null) {
                    startActivity(intent)
                }
            }
        }
    }

    private fun stopInputFocus() {
        binding.textInput.closeKeyboard()
        binding.textInput.isFocusableInTouchMode = false
        binding.textInput.isFocusable = false
        binding.textInput.isFocusableInTouchMode = true
        binding.textInput.isFocusable = true
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