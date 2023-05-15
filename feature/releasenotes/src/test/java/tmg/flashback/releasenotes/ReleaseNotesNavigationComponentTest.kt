package tmg.flashback.releasenotes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.navigation.Screen

internal class ReleaseNotesNavigationComponentTest {

    @Test
    fun `release notes`() {
        assertEquals("release_notes", Screen.ReleaseNotes.route)
    }
}