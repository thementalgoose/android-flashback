package tmg.flashback.ui.base

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.DisplayType
import tmg.flashback.navigation.ActivityProvider
import tmg.flashback.ui.permissions.RationaleBottomSheetFragment
import tmg.flashback.ui.permissions.RationaleBottomSheetFragmentCallback
import tmg.flashback.ui.permissions.RationaleType
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), RationaleBottomSheetFragmentCallback {

    @Inject
    lateinit var styleManager: StyleManager
    @Inject
    lateinit var activityProvider: ActivityProvider
    @Inject
    lateinit var analyticsManager: AnalyticsManager
    @Inject
    protected lateinit var permissionManager: PermissionManager

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    /**
     * Should we use the translucent variant of the theme or not
     */
    open val themeType: DisplayType = DisplayType.TRANSLUCENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher = registerForActivityResult(RequestPermission(), permissionManager.activityContract)
        setTheme(themeRes)
    }

    protected val themeRes: Int
        @StyleRes
        get() = styleManager.getStyleResource()

    /**
     * Logging screen analytics
     */
    fun logScreenViewed(name: String, params: Map<String, String> = mapOf()) {
        analyticsManager.viewScreen(name, params, this::class.java)
    }

    fun requestPermission(rationaleType: RationaleType) {
        when {
            ContextCompat.checkSelfPermission(this, rationaleType.permission) == PERMISSION_GRANTED -> {
                permissionManager.activityContract.onActivityResult(true)
            }
            shouldShowRequestPermissionRationale(rationaleType.permission) -> {
                RationaleBottomSheetFragment.instance(rationaleType).show(supportFragmentManager, "RATIONALE")
            }
            else -> {
                requestPermissionLauncher.launch(rationaleType.permission)
            }
        }
    }

    override fun rationaleCancelClicked(rationaleType: RationaleType) {
        // Do nothing
    }

    override fun rationaleConfirmClicked(rationaleType: RationaleType) {
        requestPermissionLauncher.launch(rationaleType.permission)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            activityProvider.onWindowFocusObtained(this)
        }
    }
}