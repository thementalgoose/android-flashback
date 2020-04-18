package tmg.flashback.utils

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible

const val bottomSheetAlpha = 1.0f

class BottomSheetFader(
    private val vBackground: View,
    private val id: String,
    private val callback: ((id: String) -> Unit)? = { }
) : BottomSheetBehavior.BottomSheetCallback() {

    var startColor: Int = 0
    var endColor: Int = 0

    init {
        vBackground.setOnClickListener {
            onStateChanged(vBackground, BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    //region BottomSheetBehavior.BottomSheetCallback

    override fun onSlide(view: View, percentage: Float) {
        when {
            percentage >= -1.0f -> {
                vBackground.visible()
                vBackground.alpha = (Math.abs(percentage + 1.0).toFloat() * bottomSheetAlpha)
            }
            percentage.isNaN() -> {
                vBackground.visible()
                vBackground.alpha = bottomSheetAlpha
            }
            else -> vBackground.gone()
        }
    }

    override fun onStateChanged(view: View, toState: Int) {
        if (toState == BottomSheetBehavior.STATE_HIDDEN) {
            vBackground.gone()
            callback?.invoke(id)
        }
        if (toState == BottomSheetBehavior.STATE_EXPANDED) {
            vBackground.visible()
        }
    }

    //endregion
}