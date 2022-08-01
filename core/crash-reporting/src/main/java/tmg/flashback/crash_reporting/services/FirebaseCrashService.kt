package tmg.flashback.crash_reporting.services

import android.os.Build
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.scottyab.rootbeer.RootBeer
import tmg.flashback.crash_reporting.BuildConfig
import javax.inject.Inject

internal class FirebaseCrashService @Inject constructor(
    private val rootBeer: RootBeer
): CrashService {

    private val isDebug: Boolean
        get() = BuildConfig.DEBUG

    private val keyDebug: String = "debug"
    private val keyEmulator: String = "emulator"
    private val keyDeviceUuid: String = "uuid"
    private val keyModel: String = "model"
    private val keyManufacturer: String = "manufacturer"
    private val keyFingerprint: String = "fingerprint"
    private val keyBrand: String = "brand"
    private val keyBoard: String = "board"
    private val keyHardware: String = "hardware"
    private val keyProduct: String = "product"
    private val keyDevice: String = "device"
    private val keyAppFirstOpen: String = "appFirstOpen"
    private val keyAppOpenCount: String = "appOpenCount"
    private val keyRootDetection: String = "rooted"

    override fun initialise(
        enableCrashReporting: Boolean,
        deviceUdid: String,
        appFirstOpened: String,
        appOpenedCount: Int
    ) {
        val instance = FirebaseCrashlytics.getInstance()

        instance.setCrashlyticsCollectionEnabled(enableCrashReporting)
        if (enableCrashReporting) {
            Log.i("Crashlytics", "Enabling crashlytics")
        }
        else {
            Log.i("Crashlytics", "Disabling crashlytics")
        }

        instance.setCustomKey(keyEmulator, isEmulator)
        instance.setCustomKey(keyDebug, isDebug)

        instance.setUserId(deviceUdid)
        instance.setCustomKey(keyDeviceUuid, deviceUdid)
        instance.setCustomKey(keyBrand, Build.BRAND)
        instance.setCustomKey(keyHardware, Build.HARDWARE)
        instance.setCustomKey(keyBoard, Build.BOARD)
        instance.setCustomKey(keyFingerprint, Build.FINGERPRINT)
        instance.setCustomKey(keyModel, Build.MODEL)
        instance.setCustomKey(keyManufacturer, Build.MANUFACTURER)
        instance.setCustomKey(keyProduct, Build.PRODUCT)
        instance.setCustomKey(keyDevice, Build.DEVICE)
        instance.setCustomKey(keyRootDetection, rootBeer.isRooted)

        if (BuildConfig.DEBUG) {
            Log.i("Crashlytics", "$keyDeviceUuid -> $deviceUdid")
            Log.i("Crashlytics", "$keyBrand -> ${Build.BRAND}")
            Log.i("Crashlytics", "$keyHardware -> ${Build.HARDWARE}")
            Log.i("Crashlytics", "$keyBoard -> ${Build.BOARD}")
            Log.i("Crashlytics", "$keyFingerprint -> ${Build.FINGERPRINT}")
            Log.i("Crashlytics", "$keyModel -> ${Build.MODEL}")
            Log.i("Crashlytics", "$keyManufacturer -> ${Build.MANUFACTURER}")
            Log.i("Crashlytics", "$keyProduct -> ${Build.PRODUCT}")
            Log.i("Crashlytics", "$keyDevice -> ${Build.DEVICE}")
            Log.i("Crashlytics", "$keyRootDetection -> ${rootBeer.isRooted}")
        }

        appFirstOpened.let {
            instance.setCustomKey(keyAppFirstOpen, it)
        }
        appOpenedCount.let {
            instance.setCustomKey(keyAppOpenCount, it)
        }
    }

    override fun logError(msg: String) {
        FirebaseCrashlytics.getInstance().log(msg)
        if (BuildConfig.DEBUG) {
            Log.e("Crashlytics", "Log error $msg")
        }
    }

    override fun logInfo(msg: String) {
        FirebaseCrashlytics.getInstance().log(msg)
        if (BuildConfig.DEBUG) {
            Log.i("Crashlytics", "Log info $msg")
        }
    }

    override fun logException(error: Exception, context: String) {
        FirebaseCrashlytics.getInstance().log(error.message ?: "Exception error $error")
        FirebaseCrashlytics.getInstance().recordException(error)
        if (BuildConfig.DEBUG) {
            Log.i("Crashlytics", "Log Exception ${error.message}")
            error.printStackTrace()
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