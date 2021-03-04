package tmg.flashback.core.ui

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment: BottomSheetDialogFragment() {

    val behavior: BottomSheetBehavior<*>
        get() {
            val dialog = dialog as BottomSheetDialog
            return dialog.behavior
        }
}