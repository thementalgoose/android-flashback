package tmg.flashback.permissions

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.permissions.ui.RationaleBottomSheetFragment
import tmg.flashback.permissions.ui.RationaleType
import tmg.flashback.ui.navigation.ActivityProvider
import javax.inject.Inject

class PermissionNavigationComponent @Inject constructor(
    private val topActivityProvider: ActivityProvider
) {
    fun showRationale(rationaleType: RationaleType) {
        val appCompatActivity = topActivityProvider.activity as? AppCompatActivity ?: return
        RationaleBottomSheetFragment.instance(rationaleType).show(appCompatActivity.supportFragmentManager, "RATIONALE")
    }
}