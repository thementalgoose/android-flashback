package tmg.flashback.privacypolicy.contract

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.navigation.Screen

internal class PrivacyPolicyNavigationComponentTest {

    @Test
    fun `privacy policy`() {
        assertEquals("privacy_policy", Screen.Settings.PrivacyPolicy.route)
    }
}