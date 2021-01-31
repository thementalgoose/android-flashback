package tmg.flashback.ui.settings.privacy

import org.junit.jupiter.api.Test
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test

internal class PrivacyPolicyViewModelTest: BaseTest() {

    private lateinit var sut: PrivacyPolicyViewModel

    private fun initSUT() {
        sut = PrivacyPolicyViewModel()
    }

    @Test
    fun `goBack fires back event`() {

        initSUT()

        sut.inputs.clickBack()

        sut.outputs.goBack.test {
            assertEventFired()
        }
    }
}