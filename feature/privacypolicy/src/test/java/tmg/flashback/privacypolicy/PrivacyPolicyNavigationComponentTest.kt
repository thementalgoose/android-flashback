package tmg.flashback.privacypolicy

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.navigation.Screen

internal class PrivacyPolicyNavigationComponentTest {

    @Test
    fun `privacy policy`() {
        assertEquals("privacy_policy", tmg.flashback.navigation.Screen.Settings.PrivacyPolicy.route)
    }
}