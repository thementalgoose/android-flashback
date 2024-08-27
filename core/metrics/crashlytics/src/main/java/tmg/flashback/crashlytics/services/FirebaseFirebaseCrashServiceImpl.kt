package tmg.flashback.crashlytics.services

import android.os.Build
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import tmg.flashback.crashlytics.BuildConfig
import tmg.flashback.crashlytics.model.FirebaseKey
import tmg.flashback.device.managers.BuildConfigManager
import javax.inject.Inject

internal class FirebaseFirebaseCrashServiceImpl @Inject constructor(
    private val buildConfigManager: BuildConfigManager
): FirebaseCrashService {

    companion object {
        private const val TAG = "Crashlytics"
    }

    private val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override fun initialise(
        enableCrashReporting: Boolean,
        deviceUuid: String,
        extraKeys: Map<FirebaseKey, String>
    ) {
        val instance = FirebaseCrashlytics.getInstance()

        instance.setCrashlyticsCollectionEnabled(enableCrashReporting)
        if (enableCrashReporting) {
            Log.i("Crashlytics", "Enabling crashlytics")
        }
        else {
            Log.i("Crashlytics", "Disabling crashlytics")
        }

        instance.setCustomKey(FirebaseKey.Emulator, buildConfigManager.isEmulator)
        instance.setCustomKey(FirebaseKey.Debug, isDebug)

        instance.setUserId(deviceUuid)
        instance.setCustomKey(FirebaseKey.DeviceUuid, deviceUuid)
        instance.setCustomKey(FirebaseKey.Brand, Build.BRAND)
        instance.setCustomKey(FirebaseKey.Hardware, Build.HARDWARE)
        instance.setCustomKey(FirebaseKey.Board, Build.BOARD)
        instance.setCustomKey(FirebaseKey.Fingerprint, Build.FINGERPRINT)
        instance.setCustomKey(FirebaseKey.Model, Build.MODEL)
        instance.setCustomKey(FirebaseKey.Manufacturer, Build.MANUFACTURER)
        instance.setCustomKey(FirebaseKey.Product, Build.PRODUCT)
        instance.setCustomKey(FirebaseKey.Device, Build.DEVICE)

        extraKeys.forEach { (key, value) ->
            instance.setCustomKey(key, value)
        }
    }

    override fun setCustomKey(key: FirebaseKey, value: String) {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }

    override fun logError(msg: String) {
        FirebaseCrashlytics.getInstance().log(msg)
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "Log error '$msg'")
        }
    }

    override fun logInfo(msg: String) {
        FirebaseCrashlytics.getInstance().log(msg)
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Log info '$msg'")
        }
    }

    override fun logException(error: Exception, context: String) {
        FirebaseCrashlytics.getInstance().log(error.message ?: "Exception error $error")
        FirebaseCrashlytics.getInstance().recordException(error)
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "Log Exception '${error.message}'")
            error.printStackTrace()
        }
    }

    private fun FirebaseCrashlytics.setCustomKey(key: FirebaseKey, value: Boolean) =
        setCustomKey(key, if (value) "true" else "false")

    private fun FirebaseCrashlytics.setCustomKey(key: FirebaseKey, value: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Custom Key: ${key.label} = $value")
        }
        this.setCustomKey(key.label, value)
    }
}