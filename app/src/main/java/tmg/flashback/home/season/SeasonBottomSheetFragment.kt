package tmg.flashback.home.season

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
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


class SeasonBottomSheetFragment: BaseBottomSheetFragment() {

    private lateinit var adapter: SeasonListAdapter

    private val viewModel: SeasonViewModel by viewModel()

    override fun layoutId(): Int = R.layout.bottom_sheet_season

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState) as BottomSheetDialog
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

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        fastscroller.setupWithRecyclerView(optionsList, {
            when (val item = adapter.list[it]) {
                SeasonListItem.Top -> null
                is SeasonListItem.Season -> {
                    when (item.fixed) {
                        HeaderType.CURRENT -> FastScrollItemIndicator.Icon(R.drawable.ic_status_finished)
                        HeaderType.FAVOURITED -> FastScrollItemIndicator.Icon(R.drawable.ic_star_filled)
                        HeaderType.ALL -> {
                            if (item.season != minimumSupportedYear) {
                                FastScrollItemIndicator.Text("${item.season.toString().substring(2, 3)}0")
                            }
                            else {
                                FastScrollItemIndicator.Icon(R.drawable.ic_play)
                            }
                        }
                    }
                }
                is SeasonListItem.Header -> {
                    when (item.type) {
                        HeaderType.CURRENT -> FastScrollItemIndicator.Icon(R.drawable.ic_status_finished)
                        HeaderType.FAVOURITED -> FastScrollItemIndicator.Icon(R.drawable.ic_star_filled)
                        HeaderType.ALL -> null
                    }
                }
            }
        })
        fastscrollerThumb.setupWithFastScroller(fastscroller)

        fastscroller.translationX = requireContext().dimensionPx(R.dimen.bottomSheetFastScrollWidth)

        behavior.addBottomSheetCallback(SeasonBottomSheetCallback(
            expanded = {
                expand()
            },
            collapsed = {
                collapse()
            }
        ))
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

    class SeasonBottomSheetCallback(
        val expanded: () -> Unit,
        val collapsed: () -> Unit
    ): BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    Log.i("Flashback", "State collapsed")
                }
                BottomSheetBehavior.STATE_DRAGGING -> {
                    Log.i("Flashback", "State dragging")
                    collapsed()
                }
                BottomSheetBehavior.STATE_EXPANDED -> {
                    Log.i("Flashback", "State expanded")
                    expanded()
                }
                BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    Log.i("Flashback", "State half expanded")
                }
                BottomSheetBehavior.STATE_HIDDEN -> {
                    Log.i("Flashback", "State hidden")
                }
                BottomSheetBehavior.STATE_SETTLING -> {
                    Log.i("Flashback", "State settling")
                }
            }
        }

    }
}