package tmg.flashback.admin.lockout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.repo.db.stats.DataDB
import tmg.flashback.repo.models.AppBanner
import tmg.flashback.repo.models.AppLockout
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.test
import kotlin.coroutines.coroutineContext

@ExperimentalCoroutinesApi
class LockoutViewModelTest: BaseTest() {

    lateinit var sut: LockoutViewModel

    private val mockTitle: String = "testingTitle"
    private val mockMessage: String = "testingMessage"
    private val mockLinkText: String = "linkText"
    private val mockLinkDestination: String = "linkDestination"
    private val appLockoutWithLink = AppLockout(
        show = true,
        title = mockTitle,
        message = mockMessage,
        linkText = mockLinkText,
        link = mockLinkDestination,
        version = null
    )
    private val appLockout = AppLockout(
        show = true,
        title = mockTitle,
        message = mockMessage,
        linkText = null,
        link = null,
        version = null
    )


    private val mockDataDB: DataDB = mock()

    @Test
    fun `LockoutViewModel all content is initially loaded`() {
        TODO("Implement this")
    }

    @Test
    fun `LockoutViewModel return home is fired when app message disappears`() {
        TODO("Implement this")
    }

    @Test
    fun `LockoutViewModel show link is not displayed if link is missing from response`() {
        TODO("Implement this")
    }

    @Test
    fun `LockoutViewModel click link opens link to correct location`() {
        TODO("Implement this")
    }


    @AfterEach
    fun tearDown() {

        reset(mockDataDB)
    }
}