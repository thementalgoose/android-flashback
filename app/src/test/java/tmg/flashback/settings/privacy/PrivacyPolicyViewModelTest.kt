package tmg.flashback.settings.privacy

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test

class PrivacyPolicyViewModelTest: BaseTest() {

    private lateinit var sut: PrivacyPolicyViewModel

    private fun initSUT() {
        sut = PrivacyPolicyViewModel(testScopeProvider)
    }

    @Test
    fun `PrivacyPolicyViewModel goBack fires back event`() {

        initSUT()

        sut.inputs.clickBack()

        sut.outputs.goBack.test {
            assertEventFired()
        }
    }
}