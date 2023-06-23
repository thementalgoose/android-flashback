package tmg.flashback.newrelic.services

import android.content.Context
import com.newrelic.agent.android.FeatureFlag

interface NewRelicService {
    fun start(applicationContext: Context)
    fun enableFeature(flag: FeatureFlag)
    fun disableFeature(flag: FeatureFlag)
    fun setFeature(flag: FeatureFlag, isEnabled: Boolean)
    fun setSessionAttribute(key: String, value: String)
    fun setSessionAttribute(key: String, value: Boolean)
    fun setSessionAttribute(key: String, value: Double)
    fun logEvent(
        eventName: String,
        attributes: Map<String, Any>
    )
}