package tmg.flashback.firebase

import android.os.Build
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import tmg.flashback.repo.db.CrashManager
import tmg.flashback.repo.pref.PrefCustomisationDB
import tmg.flashback.repo.pref.PrefDeviceDB
import java.lang.Exception

class FirebaseCrashManager(
        private val prefsDB: PrefDeviceDB,
        private val isProd: Boolean
): CrashManager {

    private val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override val enableCrashlytics: Boolean
        get() = prefsDB.crashReporting || !isProd

    private val keyDebug: String = "debug"
    private val keyEmulator: String = "emulator"
    private val keyDeviceUuid: String = "uuid"
    private val keyModel: String = "model"
    private val keyManufacturer: String = "manufacturer"
    private val keyProduct: String = "product"
    private val keyDevice: String = "device"

    override fun initialise() {

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(enableCrashlytics)
        if (enableCrashlytics) {
            Log.i("Flashback", "Enabling crashlytics")
        }
        else {
            Log.i("Flashback", "Disabling crashlytics")
        }

        FirebaseCrashlytics.getInstance().setCustomKey(keyEmulator, isEmulator)
        FirebaseCrashlytics.getInstance().setCustomKey(keyDebug, isDebug)

        FirebaseCrashlytics.getInstance().setUserId(prefsDB.deviceUdid)
        FirebaseCrashlytics.getInstance().setCustomKey(keyDeviceUuid, prefsDB.deviceUdid)
        FirebaseCrashlytics.getInstance().setCustomKey(keyModel, Build.MODEL)
        FirebaseCrashlytics.getInstance().setCustomKey(keyManufacturer, Build.MANUFACTURER)
        FirebaseCrashlytics.getInstance().setCustomKey(keyProduct, Build.PRODUCT)
        FirebaseCrashlytics.getInstance().setCustomKey(keyDevice, Build.DEVICE)
    }

    override fun log(msg: String) {
        if (enableCrashlytics) {
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
        if (enableCrashlytics) {
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