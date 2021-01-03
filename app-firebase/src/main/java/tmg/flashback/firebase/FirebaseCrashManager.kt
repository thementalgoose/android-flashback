package tmg.flashback.firebase

import android.os.Build
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.pref.DeviceRepository
import java.lang.Exception

class FirebaseCrashManager(
        private val deviceRepository: DeviceRepository,
        private val isProd: Boolean
): CrashManager {

    private val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override var enableCrashReporting: Boolean
        get() = deviceRepository.crashReporting || !isProd
        set(value) {
            deviceRepository.crashReporting = value
        }

    private val keyDebug: String = "debug"
    private val keyEmulator: String = "emulator"
    private val keyDeviceUuid: String = "uuid"
    private val keyModel: String = "model"
    private val keyManufacturer: String = "manufacturer"
    private val keyProduct: String = "product"
    private val keyDevice: String = "device"
    private val keyAppFirstOpen: String = "appFirstOpen"
    private val keyAppOpenCount: String = "appFirstOpen"

    override fun initialise(appOpenedCount: Int?, appFirstOpened: String?) {

        val instance = FirebaseCrashlytics.getInstance()

        instance.setCrashlyticsCollectionEnabled(enableCrashReporting)
        if (enableCrashReporting) {
            Log.i("Flashback", "Enabling crashlytics")
        }
        else {
            Log.i("Flashback", "Disabling crashlytics")
        }

        instance.setCustomKey(keyEmulator, isEmulator)
        instance.setCustomKey(keyDebug, isDebug)

        instance.setUserId(deviceRepository.deviceUdid)
        instance.setCustomKey(keyDeviceUuid, deviceRepository.deviceUdid)
        instance.setCustomKey(keyModel, Build.MODEL)
        instance.setCustomKey(keyManufacturer, Build.MANUFACTURER)
        instance.setCustomKey(keyProduct, Build.PRODUCT)
        instance.setCustomKey(keyDevice, Build.DEVICE)

        appFirstOpened?.let {
            instance.setCustomKey(keyAppFirstOpen, it)
        }
        appOpenedCount?.let {
            instance.setCustomKey(keyAppOpenCount, it)
        }
    }

    override fun log(msg: String) {
        if (enableCrashReporting) {
            if (isDebug) {
                Log.i("Flashback", "Crashlytics \"${msg}\"")
            }
            FirebaseCrashlytics.getInstance().log("I/Crashlytics: $msg")
        }
    }

    override fun logError(error: Exception, context: String) {
        if (isDebug) {
            Log.e("Flashback", "Crashlytics \"${context}\"")
            error.printStackTrace()
        }
        if (enableCrashReporting) {
            FirebaseCrashlytics.getInstance().log("E/Crashlytics: ${error.message}")
            FirebaseCrashlytics.getInstance().recordException(error)
        }
    }

    private val isEmulator: Boolean
        get() = (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
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
}