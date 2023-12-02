package tmg.flashback.device.usecases

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.strings.R.string
import tmg.flashback.device.ActivityProvider
import java.lang.RuntimeException
import javax.inject.Inject

class OpenLocationUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val topActivityProvider: ActivityProvider,
    private val openUrlUseCase: OpenUrlUseCase,
) {

    fun openLocation(lat: Double, lng: Double, name: String) {
        val geoIntent = "geo:0,0?q=${lat},${lng} (${name})"
        val googleMapsLink = "https://google.com/maps/search/?api=1&query=$lat,$lng"
        when {
            isPackageInstalled(PACKAGE_GOOGLE_MAPS) -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoIntent))
                    .apply { setPackage(PACKAGE_GOOGLE_MAPS) }
                val chooser = Intent.createChooser(intent, context.getString(string.intent_chooser_open_maps))
                topActivityProvider.activity?.startActivity(chooser)
            }
            isPackageInstalled(PACKAGE_WAZE) -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoIntent))
                    .apply { setPackage(PACKAGE_WAZE) }
                val chooser = Intent.createChooser(intent, context.getString(string.intent_chooser_open_maps))
                topActivityProvider.activity?.startActivity(chooser)
            }
            else -> openUrlUseCase.openUrl(googleMapsLink)
        }
    }

    private fun isPackageInstalled(packageName: String): Boolean = try {
        context.packageManager.getApplicationInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    } catch (e: RuntimeException) {
        false
    }

    companion object {
        const val PACKAGE_GOOGLE_MAPS = "com.google.android.apps.maps"
        const val PACKAGE_WAZE = "com.waze"
    }
}