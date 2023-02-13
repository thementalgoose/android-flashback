package tmg.flashback.privacypolicy

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.ui.navigation.Screen

internal class PrivacyPolicyNavigationComponentTest {

    @Test
    fun `privacy policy`() {
        assertEquals("privacy_policy", Screen.Settings.PrivacyPolicy.route)
    }
}