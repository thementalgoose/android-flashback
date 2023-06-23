package tmg.flashback.crashlytics.services

import android.os.Build
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import tmg.flashback.crashlytics.BuildConfig
import tmg.flashback.device.managers.BuildConfigManager
import javax.inject.Inject

internal class FirebaseCrashService @Inject constructor(
    private val buildConfigManager: BuildConfigManager
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

        instance.setCustomKey(keyEmulator, buildConfigManager.isEmulator)
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
}