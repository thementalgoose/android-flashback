package tmg.flashback.googleanalytics.manager

import android.os.Bundle
import tmg.flashback.googleanalytics.UserProperty
import tmg.flashback.googleanalytics.services.FirebaseAnalyticsService
import tmg.flashback.device.repository.PrivacyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsManager @Inject constructor(
    private val privacyRepository: PrivacyRepository,
    private val firebaseAnalyticsService: FirebaseAnalyticsService
) {
    var enabled: Boolean
        get() = privacyRepository.analytics
        set(value) {
            privacyRepository.analytics = value
        }

    fun initialise(userId: String) {
        firebaseAnalyticsService.setUserId(userId)
        firebaseAnalyticsService.setAnalyticsCollectionEnabled(enabled)
    }

    fun logEvent(key: String, params: Map<String, String> = emptyMap()) {
        if (enabled) {
            if (params.isNotEmpty()) {
                val bundle = Bundle().apply {
                    for (x in params) {
                        putString(x.key, x.value)
                    }
                }
                firebaseAnalyticsService.logEvent(key, bundle)
            }
            else {
                firebaseAnalyticsService.logEvent(key)
            }
        }
    }

    fun setUserProperty(property: UserProperty, value: String) {
        if (enabled) {
            firebaseAnalyticsService.setProperty(property.key, value)
        }
    }

    fun viewScreen(screenName: String, params: Map<String, String> = emptyMap(), clazz: Class<*>? = null) {
        if (enabled) {
            firebaseAnalyticsService.logViewScreen(screenName, params, clazz)
        }
    }
}