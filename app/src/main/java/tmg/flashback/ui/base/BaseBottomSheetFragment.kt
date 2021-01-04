package tmg.flashback.ui.base

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import tmg.utilities.lifecycle.common.CommonBottomSheetFragment

abstract class BaseBottomSheetFragment: CommonBottomSheetFragment() {

    val behavior: BottomSheetBehavior<*>
        get() {
            val dialog = dialog as BottomSheetDialog
            return dialog.behavior
        }
}