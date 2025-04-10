package tmg.flashback.device.usecases

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.ActivityProvider
import javax.inject.Inject
import androidx.core.net.toUri

class OpenPlayStoreUseCase @Inject constructor(
    private val topActivityProvider: ActivityProvider,
    private val buildConfigManager: BuildConfigManager
) {

    private val appPackageName: String
        get() = buildConfigManager.applicationId

    fun openPlaystore() {
        try {
            topActivityProvider.activity?.startActivity(
                Intent(Intent.ACTION_VIEW, "market://details?id=$appPackageName".toUri())
            )
        } catch (e : ActivityNotFoundException) {
            topActivityProvider.activity?.startActivity(
                Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$appPackageName".toUri())
            )
        }
    }
}