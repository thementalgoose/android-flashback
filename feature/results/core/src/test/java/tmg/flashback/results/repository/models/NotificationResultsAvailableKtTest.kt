package tmg.flashback.results.repository.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable

internal class NotificationResultsAvailableKtTest {
    @Test
    fun `pref names for results available are distinct`() {
        assertEquals(NotificationResultsAvailable.values().size, NotificationResultsAvailable.values().distinctBy { it.prefKey }.size)
    }
}