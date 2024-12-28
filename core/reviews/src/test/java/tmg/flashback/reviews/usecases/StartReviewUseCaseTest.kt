package tmg.flashback.reviews.usecases

import android.app.Activity
import com.google.android.play.core.review.ReviewInfo
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.ActivityProvider
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.reviews.manager.AppReviewManager
import tmg.flashback.reviews.repository.AppReviewRepository
import tmg.testutils.BaseTest

internal class StartReviewUseCaseTest: BaseTest() {

    private val mockActivityProvider: ActivityProvider = mockk(relaxed = true)
    private val mockAppReviewRepository: AppReviewRepository = mockk(relaxed = true)
    private val mockAppReviewManager: AppReviewManager = mockk(relaxed = true)
    private val mockCrashlyticsManager: CrashlyticsManager = mockk(relaxed = true)
    private val mockFirebaseAnalyticsManager: FirebaseAnalyticsManager = mockk(relaxed = true)

    private val fakeReviewInfo: ReviewInfo = mockk(relaxed = true)
    private val fakeActivity: Activity = mockk(relaxed = true)

    private lateinit var underTest: StartReviewUseCase

    private fun initUnderTest() {
        underTest = StartReviewUseCase(
            topActivityProvider = mockActivityProvider,
            appReviewRepository = mockAppReviewRepository,
            appReviewManager = mockAppReviewManager,
            crashlyticsManager = mockCrashlyticsManager,
            analyticsManager = mockFirebaseAnalyticsManager
        )
    }

    @BeforeEach
    fun setUp() {
        every { mockActivityProvider.activity } returns fakeActivity
        coEvery { mockAppReviewManager.requestReview() } returns fakeReviewInfo
        coEvery { mockAppReviewManager.launchReview(any(), fakeReviewInfo) } just Runs
    }

    @Test
    fun `if app review has been prompted before, do nothing`() = runTest {
        every { mockAppReviewRepository.hasPromptedForReview } returns true
        every { mockAppReviewRepository.sectionsSeen } returns AppSection.entries.toSet()

        initUnderTest()

        runBlocking {
            underTest.start()
        }

        verify(exactly = 0) {
            mockActivityProvider.activity
        }
        coVerify(exactly = 0) {
            mockAppReviewManager.requestReview()
            mockAppReviewManager.launchReview(any(), any())
        }
    }

    @Test
    fun `if all sections havent been seen, do nothing`() = runTest {
        every { mockAppReviewRepository.hasPromptedForReview } returns false
        every { mockAppReviewRepository.sectionsSeen } returns setOf(AppSection.HOME)

        initUnderTest()

        runBlocking {
            underTest.start()
        }

        verify(exactly = 0) {
            mockActivityProvider.activity
        }
        coVerify(exactly = 0) {
            mockAppReviewManager.requestReview()
            mockAppReviewManager.launchReview(any(), any())
        }
    }

    @Test
    fun `launch app review flow, making relevant crashlytics + analytics calls`() = runTest {
        every { mockAppReviewRepository.hasPromptedForReview } returns false
        every { mockAppReviewRepository.sectionsSeen } returns AppSection.entries.toSet()

        initUnderTest()

        runBlocking {
            underTest.start()
        }

        coVerify {
            mockCrashlyticsManager.log(any())
            mockAppReviewManager.requestReview()
            mockFirebaseAnalyticsManager.logEvent("review_prompt_request")
            mockAppReviewRepository.hasPromptedForReview = true
            mockAppReviewManager.launchReview(fakeActivity, fakeReviewInfo)
            mockFirebaseAnalyticsManager.logEvent("review_prompt_complete")
        }
    }
}