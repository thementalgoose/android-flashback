package tmg.flashback.repo_firebase

import android.content.Context
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
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
        var builder = CrashlyticsCore.Builder()
        builder = builder.disabled(shouldDisable)
        if (shouldDisable) {
            Log.i("Flashback", "Disabling crashlytics")
        } else {
            Log.i("Flashback", "Enabling crashlytics")
        }
        Fabric.with(context, Crashlytics.Builder().core(builder.build()).build())

        Crashlytics.setString(keyDeviceUuid, prefsDB.deviceUdid)
        Crashlytics.setString(keyModel, android.os.Build.MODEL)
        Crashlytics.setString(keyManufacturer, android.os.Build.MANUFACTURER)
        Crashlytics.setString(keyProduct, android.os.Build.PRODUCT)
        Crashlytics.setString(keyDevice, android.os.Build.DEVICE)
    }

    override fun log(msg: String) {
        Log.i("Flashback", "Logging INFO to crashlytics \"${msg}\"")
        Crashlytics.log(Log.INFO, "Flashback", msg)
    }

    override fun logError(error: Exception, context: String) {
        Log.e("Flashback", "Logging ERROR to crashlytics \"${context}\"")
        Crashlytics.log(Log.ERROR, "Flashback", context)
        Crashlytics.logException(error)
    }

}