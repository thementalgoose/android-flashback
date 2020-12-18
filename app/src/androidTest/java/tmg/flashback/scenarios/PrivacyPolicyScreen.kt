package tmg.flashback.scenarios

import tmg.flashback.R
import tmg.flashback.utils.EspressoUtils
import tmg.flashback.utils.EspressoUtils.assertTextDisplayed

fun privacyPolicyScreen(inside: PrivacyPolicyScreen.() -> Unit) {
    inside(PrivacyPolicyScreen)
}

object PrivacyPolicyScreen {

    fun checkTitle() = apply {
        assertTextDisplayed(R.string.privacy_policy_title)
    }
}