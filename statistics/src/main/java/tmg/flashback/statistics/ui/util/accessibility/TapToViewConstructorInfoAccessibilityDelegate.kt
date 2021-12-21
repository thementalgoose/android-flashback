package tmg.flashback.statistics.ui.util.accessibility

import android.view.View
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import tmg.flashback.statistics.R

class TapToViewConstructorInfoAccessibilityDelegate(
    private val constructorName: String
): AccessibilityDelegateCompat() {
    override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(host, info)
        info.addAction(
            AccessibilityNodeInfoCompat.AccessibilityActionCompat(
                AccessibilityNodeInfoCompat.ACTION_CLICK,
                host.context.getString(R.string.ab_tap_to_view_constructor_info, constructorName)
            ))
    }
}