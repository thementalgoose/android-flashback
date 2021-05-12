package tmg.crash_reporting.repository

import tmg.core.device.repository.SharedPreferenceRepository

class CrashRepository(
    private val sharedPreferenceRepository: SharedPreferenceRepository
) {
    companion object {
        private const val keyCrashReporting: String = "CRASH_REPORTING"
        private const val keyShakeToReport: String = "SHAKE_TO_REPORT"
    }

    var isEnabled: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyCrashReporting, true)
        set(value) = sharedPreferenceRepository.save(keyCrashReporting, value)

    var shakeToReport: Boolean
        get() = sharedPreferenceRepository.getBoolean(keyShakeToReport, true)
        set(value) = sharedPreferenceRepository.save(keyShakeToReport, value)
}