package tmg.flashback.home.season

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import kotlinx.android.synthetic.main.bottom_sheet_season.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseBottomSheetFragment
import tmg.flashback.bottomSheetFastScrollDuration
import tmg.flashback.extensions.dimensionPx
import tmg.flashback.minimumSupportedYear
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent


class SeasonBottomSheetFragment: BaseBottomSheetFragment() {

    private lateinit var adapter: SeasonListAdapter
    private var callback: SeasonRequestedCallback? = null

    private val viewModel: SeasonViewModel by viewModel()

    override fun layoutId(): Int = R.layout.bottom_sheet_season

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        if(dialog.window != null) {
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        return dialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SeasonRequestedCallback) {
            callback = context
        }
    }

    override fun initViews() {
        adapter = SeasonListAdapter(
            featureToggled = {
                viewModel.inputs.toggleHeader(it)
            },
            favouriteToggled = {
                viewModel.inputs.toggleFavourite(it)
            },
            seasonClicked = {
                viewModel.inputs.clickSeason(it)
            }
        )
        optionsList.adapter = adapter
        optionsList.layoutManager = LinearLayoutManager(context)

        fastscroller.setupWithRecyclerView(optionsList, {
            when (val item = adapter.list[it]) {
                SeasonListItem.Top -> null
                is SeasonListItem.Season -> {
                    when (item.fixed) {
                        HeaderType.FAVOURITED -> FastScrollItemIndicator.Icon(R.drawable.ic_bottom_sheet_favourite)
                        HeaderType.ALL -> {
                            if (item.season != minimumSupportedYear) {
                                FastScrollItemIndicator.Text("${item.season.toString().substring(2, 3)}0")
                            }
                            else {
                                FastScrollItemIndicator.Icon(R.drawable.ic_bottom_sheet_start)
                            }
                        }
                    }
                }
                is SeasonListItem.Header -> {
                    when (item.type) {
                        HeaderType.FAVOURITED -> FastScrollItemIndicator.Icon(R.drawable.ic_bottom_sheet_favourite)
                        HeaderType.ALL -> null
                    }
                }
            }
        })
        fastscrollerThumb.setupWithFastScroller(fastscroller)

        fastscroller.translationX = requireContext().dimensionPx(R.dimen.bottomSheetFastScrollWidth)

        behavior.addBottomSheetCallback(SeasonBottomSheetCallback(
            expanded = { expand() },
            collapsed = { collapse() }
        ))

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.showSeasonEvent) {
            callback?.seasonRequested(it)
            dismiss()
        }
    }

    private fun expand() {
        fastscroller.animate()
            .translationX(0.0f)
            .setDuration(bottomSheetFastScrollDuration.toLong())
            .start()
        adapter.setToggle(true)
    }

    private fun collapse() {
        fastscroller.animate()
            .translationX(requireContext().dimensionPx(R.dimen.bottomSheetFastScrollWidth))
            .setDuration(bottomSheetFastScrollDuration.toLong())
            .start()
        adapter.setToggle(false)
    }


}