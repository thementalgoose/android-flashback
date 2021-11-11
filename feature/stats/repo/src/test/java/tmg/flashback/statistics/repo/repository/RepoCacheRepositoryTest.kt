package tmg.flashback.statistics.repo.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.core.prefs.manager.PreferenceManager
import java.util.*

internal class RepoCacheRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: RepoCacheRepository

    @BeforeEach
    internal fun setUp() {
        every { mockPreferenceManager.getBoolean(keyInitialSync, false) } returns false
    }

    private fun initSUT() {
        sut = RepoCacheRepository(mockPreferenceManager)
    }

    @Test
    fun `initial sync reads value from preference manager`() {
        every { mockPreferenceManager.getBoolean(keyInitialSync, false) } returns true
        initSUT()
        assertTrue(sut.initialSync)
        verify {
            mockPreferenceManager.getBoolean(keyInitialSync, false)
        }
    }

    @Test
    fun `initial sync value saves to preference manager`() {
        initSUT()
        sut.initialSync = true
        verify {
            mockPreferenceManager.save(keyInitialSync, true)
        }
    }

    @Test
    fun `should sync current season returns true when key is not found`() {
        every { mockPreferenceManager.getString(keyLastSyncTime, null) } returns null
        initSUT()
        assertTrue(sut.shouldSyncCurrentSeason())
    }

    @Test
    fun `should sync current season returns true time is invalid`() {
        every { mockPreferenceManager.getString(keyLastSyncTime, null) } returns "invalid time"
        initSUT()

        assertTrue(sut.shouldSyncCurrentSeason())
    }

    @Test
    fun `should sync current season returns true mins difference is within 60 minutes`() {
        val time29MinsAgo = LocalDateTime.now().minusMinutes(59L)
        every { mockPreferenceManager.getString(keyLastSyncTime, null) } returns time29MinsAgo.format(localDateTimeFormatter)
        initSUT()

        assertFalse(sut.shouldSyncCurrentSeason())
    }

    @Test
    fun `should sync current season returns false mins difference is more than 60 minutes`() {
        val time31MinsAgo = LocalDateTime.now().minusMinutes(61L)
        every { mockPreferenceManager.getString(keyLastSyncTime, null) } returns time31MinsAgo.format(localDateTimeFormatter)
        initSUT()

        assertTrue(sut.shouldSyncCurrentSeason())
    }

    @Test
    fun `mark current season synced saves to pref manager`() {
        initSUT()

        val expected = LocalDateTime.now().format(localDateTimeFormatter)
        sut.markedCurrentSeasonSynchronised()
        verify {
            mockPreferenceManager.save(keyLastSyncTime, expected)
        }
    }

    @Test
    fun `seasons sync at least one returns value from preference manager`() {
        val source = mutableSetOf("2019", "2018")
        val expected = setOf(2019, 2018)
        every { mockPreferenceManager.getSet(keyPreviouslyDownloaded, any()) } returns source

        initSUT()
        assertEquals(expected, sut.seasonsSyncAtLeastOnce)
    }

    @Test
    fun `seasons sync at least one saves value to preference manager`() {
        val source = setOf(2019, 2018)
        val expected = mutableSetOf("2019", "2018")
        initSUT()

        sut.seasonsSyncAtLeastOnce = source
        verify {
            mockPreferenceManager.save(keyPreviouslyDownloaded, expected)
        }
    }

    companion object {
        private val localDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH)

        private const val keyLastSyncTime: String = "REPO_SEASON_LAST_SYNCED"
        private const val keyInitialSync: String = "REPO_INITIAL_SYNC"
        private const val keyPreviouslyDownloaded: String = "REPO_HAS_DOWNLOADED_AT_LEAST_ONCE"
    }
}