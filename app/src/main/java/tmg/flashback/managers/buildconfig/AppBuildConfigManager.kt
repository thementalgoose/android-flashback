package tmg.flashback.managers.buildconfig

import android.os.Build
import tmg.flashback.BuildConfig
import tmg.flashback.device.managers.BuildConfigManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppBuildConfigManager @Inject constructor() : BuildConfigManager {

    override val versionCode: Int
        get() = BuildConfig.VERSION_CODE

    override val versionName: String
        get() = BuildConfig.VERSION_NAME

    override val applicationId: String
        get() = BuildConfig.APPLICATION_ID

    override val isMonetThemeSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    override val isRuntimeNotificationsSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    override val isEmulator: Boolean
        get() = (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.contains("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override val brand: String
        get() = Build.BRAND

    override val hardware: String
        get() = Build.HARDWARE

    override val board: String
        get() = Build.BOARD

    override val fingerprint: String
        get() = Build.FINGERPRINT

    override val model: String
        get() = Build.MODEL

    override val manufacturer: String
        get() = Build.MANUFACTURER

    override val product: String
        get() = Build.PRODUCT

    override val device: String
        get() = Build.DEVICE
}