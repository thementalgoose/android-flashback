package tmg.flashback.ui.base

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.content.ContextCompat
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.AppPermissions
import tmg.flashback.device.ActivityProvider
import tmg.flashback.ui.managers.PermissionManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.DisplayType
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.permissions.RationaleBottomSheetFragment
import tmg.flashback.ui.permissions.RationaleBottomSheetFragmentCallback
import tmg.flashback.ui.repository.ThemeRepository
import tmg.utilities.extensions.isInDayMode
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), RationaleBottomSheetFragmentCallback {

    @Inject
    lateinit var styleManager: StyleManager
    @Inject
    lateinit var activityProvider: ActivityProvider
    @Inject
    lateinit var permissionManager: PermissionManager
    @Inject
    lateinit var crashManager: CrashlyticsManager
    @Inject
    lateinit var themeRepository: ThemeRepository

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    /**
     * Should we use the translucent variant of the theme or not
     */
    open val themeType: DisplayType = DisplayType.TRANSLUCENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), permissionManager.activityContract)
        setTheme(themeRes)

        this.enableEdgeToEdge(
            statusBarStyle = when (themeRepository.nightMode) {
                NightMode.DEFAULT -> SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
                NightMode.DAY -> SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                NightMode.NIGHT -> SystemBarStyle.dark(Color.TRANSPARENT)
            },
            navigationBarStyle = when (themeRepository.nightMode) {
                NightMode.DEFAULT -> SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
                NightMode.DAY -> SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
                NightMode.NIGHT -> SystemBarStyle.dark(Color.TRANSPARENT)
            },
        )
    }

    protected val themeRes: Int
        @StyleRes
        get() = styleManager.getStyleResource()

    fun requestPermission(vararg rationaleType: AppPermissions.RuntimePermission) {
        val permissionWithState = rationaleType.toList().checkPermissions()
        val notGrantedPermissions = permissionWithState.filter { !it.value }.map { it.key }
        if (notGrantedPermissions.isEmpty()) {
            permissionManager.activityContract.onActivityResult(permissionWithState.map { it.key.permission to it.value }.toMap())
        } else {
            val permissionShouldShowRationale = notGrantedPermissions
                .associateWith { shouldShowRequestPermissionRationale(it.permission) }

            if (permissionShouldShowRationale.any { it.value }) {
                val permissionList = permissionShouldShowRationale.keys.map { it.permission }.toTypedArray()
                RationaleBottomSheetFragment.instance(permissionList).show(supportFragmentManager, "RATIONALE")
            } else {
                requestPermissionLauncher.launch(notGrantedPermissions.map { it.permission }.toTypedArray())
            }
        }
    }
    private fun List<AppPermissions.RuntimePermission>.checkPermissions(): Map<AppPermissions.RuntimePermission, Boolean> =
        this.associateWith {
            (ContextCompat.checkSelfPermission(this@BaseActivity, it.permission) == PERMISSION_GRANTED)
        }


    override fun rationaleCancelClicked(rationaleType: List<AppPermissions.RuntimePermission>) {
        // Do nothing
    }

    override fun rationaleConfirmClicked(rationaleType: List<AppPermissions.RuntimePermission>) {
        requestPermissionLauncher.launch(rationaleType.map { it.permission }.toTypedArray())
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            activityProvider.onWindowFocusObtained(this)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        crashManager.log("Configuration Change")
    }
}