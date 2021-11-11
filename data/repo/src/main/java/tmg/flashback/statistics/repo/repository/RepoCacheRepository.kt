package tmg.flashback.statistics.repo.repository

import org.threeten.bp.LocalDateTime
import org.threeten.bp.Period
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import org.threeten.bp.temporal.ChronoUnit
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.statistics.repo.constants.CacheTimeout
import java.util.*

class RepoCacheRepository(
    private val preferenceManager: PreferenceManager
): CacheRepository {

    override var initialSync: Boolean
        get() = preferenceManager.getBoolean(keyInitialSync, false)
        set(value) { preferenceManager.save(keyInitialSync, value) }

    override fun shouldSyncCurrentSeason(): Boolean {
        val result = preferenceManager.getString(keyLastSyncTime, null) ?: return true

        val time = try {
            LocalDateTime.parse(result, localDateTimeFormatter)
        } catch (e: DateTimeParseException) {
            /* Do nothing */
            return true
        }
        val minutesDifference: Long = ChronoUnit.MINUTES.between(time, LocalDateTime.now())
        if (minutesDifference > CacheTimeout.timeoutMinutes) {
            return true
        }
        return false
    }

    override fun markedCurrentSeasonSynchronised()  {
        preferenceManager.save(keyLastSyncTime, LocalDateTime.now().format(localDateTimeFormatter))
    }

    override var seasonsSyncAtLeastOnce: Set<Int>
        get() = preferenceManager.getSet(keyPreviouslyDownloaded, emptySet()).mapNotNull { it.toIntOrNull() }.toSet()
        set(value) {
            preferenceManager.save(keyPreviouslyDownloaded, value.map { it.toString() }.toSet())
        }

    companion object {
        private val localDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH)

        private const val keyLastSyncTime: String = "REPO_SEASON_LAST_SYNCED"
        private const val keyInitialSync: String = "REPO_INITIAL_SYNC"
        private const val keyPreviouslyDownloaded: String = "REPO_HAS_DOWNLOADED_AT_LEAST_ONCE"
    }
}