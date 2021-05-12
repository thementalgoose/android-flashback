package tmg.common.ui.privacypolicy

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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