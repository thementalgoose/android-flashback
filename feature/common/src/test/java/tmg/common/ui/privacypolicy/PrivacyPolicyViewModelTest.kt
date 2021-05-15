package tmg.common.ui.privacypolicy

import org.junit.jupiter.api.Test
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

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