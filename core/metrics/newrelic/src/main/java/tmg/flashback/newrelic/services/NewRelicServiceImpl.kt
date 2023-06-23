package tmg.flashback.newrelic.services

import android.content.Context
import android.util.Log
import com.newrelic.agent.android.FeatureFlag
import com.newrelic.agent.android.NewRelic
import com.newrelic.agent.android.logging.AgentLog
import tmg.flashback.analytics.BuildConfig
import tmg.flashback.analytics.R
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.device.repository.PrivacyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NewRelicServiceImpl @Inject constructor(
    private val privacyRepository: PrivacyRepository,
    private val deviceRepository: DeviceRepository
): NewRelicService {

    override fun start(applicationContext: Context) {
        val token = applicationContext.getString(R.string.new_relic_token)
        NewRelic
            .withApplicationToken(token)
            .withLogLevel(AgentLog.AUDIT)
            .withLoggingEnabled(BuildConfig.DEBUG)
            .withCrashReportingEnabled(privacyRepository.crashReporting)
            .withDeviceID(deviceRepository.deviceUdid)
            .apply {
                if (BuildConfig.DEBUG) {
                    this.withApplicationVersion("DEBUG")
                }
            }
            .start(applicationContext)
    }

    override fun enableFeature(flag: FeatureFlag) {
        NewRelic.enableFeature(flag)
    }

    override fun disableFeature(flag: FeatureFlag) {
        NewRelic.disableFeature(flag)
    }

    override fun setSessionAttribute(key: String, value: String) {
        NewRelic.setAttribute(key, value)
    }

    override fun setSessionAttribute(key: String, value: Boolean) {
        NewRelic.setAttribute(key, value)
    }

    override fun setSessionAttribute(key: String, value: Double) {
        NewRelic.setAttribute(key, value)
    }

    override fun logEvent(
        eventName: String,
        attributes: Map<String, Any>
    ) {
        NewRelic
            .recordCustomEvent("MobileEvent", eventName, attributes)
    }
}