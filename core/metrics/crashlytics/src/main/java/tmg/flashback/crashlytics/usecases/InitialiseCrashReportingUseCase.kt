package tmg.flashback.crashlytics.usecases

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.crashlytics.services.CrashService
import tmg.flashback.device.repository.PrivacyRepository
import java.util.Locale
import javax.inject.Inject

class InitialiseCrashReportingUseCase @Inject constructor(
    private val privacyRepository: PrivacyRepository,
    private val crashService: CrashService
) {
    fun initialise(
        deviceUdid: String,
        appOpenedCount: Int,
        appFirstOpened: LocalDate
    ) {
        crashService.initialise(
            enableCrashReporting = privacyRepository.crashReporting,
            deviceUdid = deviceUdid,
            appOpenedCount = appOpenedCount,
            appFirstOpened = appFirstOpened.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH))
        )
    }
}