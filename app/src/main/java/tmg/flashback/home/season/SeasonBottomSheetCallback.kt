package tmg.flashback.home.season

import android.util.Log
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