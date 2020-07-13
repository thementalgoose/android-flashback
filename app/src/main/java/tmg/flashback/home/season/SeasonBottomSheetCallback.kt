package tmg.flashback.home.season

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SeasonBottomSheetCallback(
    val expanded: () -> Unit,
    val collapsed: () -> Unit
): BottomSheetBehavior.BottomSheetCallback() {
    override fun onSlide(bottomSheet: View, slideOffset: Float) {

    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        when (newState) {
            BottomSheetBehavior.STATE_COLLAPSED -> { }
            BottomSheetBehavior.STATE_DRAGGING -> {
                collapsed()
            }
            BottomSheetBehavior.STATE_EXPANDED -> {
                expanded()
            }
            BottomSheetBehavior.STATE_HALF_EXPANDED -> { }
            BottomSheetBehavior.STATE_HIDDEN -> { }
            BottomSheetBehavior.STATE_SETTLING -> { }
        }
    }

}