package tmg.flashback.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import tmg.flashback.repo.db.CrashReporter
import tmg.flashback.repo.db.PrefsDB
import java.lang.Exception

class CrashReporterFirebase(
    private val prefsDB: PrefsDB,
    val context: Context
): CrashReporter {

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    private val keyDeviceUuid: String = "uuid"
    private val keyModel: String = "model"
    private val keyManufacturer: String = "manufacturer"
    private val keyProduct: String = "product"
    private val keyDevice: String = "device"

    override fun initialise() {
        val shouldDisable = !prefsDB.crashReporting

        if (BuildConfig.DEBUG || shouldDisable) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
            Log.i("Flashback", "Disabling crashlytics")
        }
        else {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
            Log.i("Flashback", "Enabling crashlytics")
        }

        FirebaseCrashlytics.getInstance().setCustomKey(keyDeviceUuid, prefsDB.deviceUdid)
        FirebaseCrashlytics.getInstance().setCustomKey(keyModel, android.os.Build.MODEL)
        FirebaseCrashlytics.getInstance().setCustomKey(keyManufacturer, android.os.Build.MANUFACTURER)
        FirebaseCrashlytics.getInstance().setCustomKey(keyProduct, android.os.Build.PRODUCT)
        FirebaseCrashlytics.getInstance().setCustomKey(keyDevice, android.os.Build.DEVICE)
    }

    override fun log(msg: String) {
        if (prefsDB.crashReporting) {
            Log.i("Flashback", "Logging INFO to crashlytics \"${msg}\"")
            FirebaseCrashlytics.getInstance().log("INFO $msg")
        }
    }

    override fun logError(error: Exception, context: String) {
        if (BuildConfig.DEBUG) {
            Log.e("Flashback", "Logging ERROR to crashlytics \"${context}\"")
            error.printStackTrace()
        }
        if (prefsDB.crashReporting) {
            FirebaseCrashlytics.getInstance().log("ERROR ${error.message}")
            FirebaseCrashlytics.getInstance().recordException(error)
        }
    }
}