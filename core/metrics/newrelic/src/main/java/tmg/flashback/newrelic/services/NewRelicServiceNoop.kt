package tmg.flashback.newrelic.services

import android.content.Context
import android.util.Log
import com.newrelic.agent.android.FeatureFlag
import com.newrelic.agent.android.NewRelic
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewRelicServiceNoop @Inject constructor(): NewRelicService {
    override fun start(applicationContext: Context) {
        Log.i("NewRelic", "[Noop] Start")
        NewRelic.shutdown()
    }
    override fun enableFeature(flag: FeatureFlag) {
        Log.i("NewRelic", "[Noop] Enable feature ${flag.name}")
    }
    override fun disableFeature(flag: FeatureFlag) {
        Log.i("NewRelic", "[Noop] Disable feature ${flag.name}")
    }
    override fun setSessionAttribute(key: String, value: String) {
        Log.i("NewRelic", "[Noop] Session attribute $key $value")
    }
    override fun setSessionAttribute(key: String, value: Boolean) {
        Log.i("NewRelic", "[Noop] Session attribute $key $value")
    }
    override fun setSessionAttribute(key: String, value: Double) {
        Log.i("NewRelic", "[Noop] Session attribute $key $value")
    }
    override fun logEvent(eventName: String, attributes: Map<String, Any>) {
        Log.i("NewRelic", "[Noop] Log event $eventName with attributes $attributes")
    }
}