package tmg.flashback.repo.models.stats

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDate

class HistoryTest {

    private lateinit var mockHistoryRound1: HistoryRound
    private lateinit var mockHistoryRound2: HistoryRound

    private val previous = LocalDate.now().minusDays(1)
    private val now = LocalDate.now()
    private val future = LocalDate.now().plusDays(1)

    @ParameterizedTest
    @CsvSource(
            "previous,previous,2",
            "previous,now,1",
            "previous,future,1",
            "now,now,0",
            "now,future,0",
            "future,future,0"
    )
    fun `History completed test returns all items that have happened in the past`(mockState1: String, mockState2: String, expectedRoundsFound: Int) {

        initMockStates(mockState1, mockState2)

        val mock = History(2020, null, listOf(mockHistoryRound1, mockHistoryRound2))

        assertEquals(expectedRoundsFound, mock.completed)
    }

    @ParameterizedTest
    @CsvSource(
            "previous,previous,0",
            "previous,now,1",
            "previous,future,1",
            "now,now,2",
            "now,future,2",
            "future,future,2"
    )
    fun `History upcoming test returns all items that will happen in the future`(mockState1: String, mockState2: String, expectedRoundsFound: Int) {

        initMockStates(mockState1, mockState2)

        val mock = History(2020, null, listOf(mockHistoryRound1, mockHistoryRound2))

        assertEquals(expectedRoundsFound, mock.upcoming)
    }

    @ParameterizedTest
    @CsvSource(
            "previous,previous,0",
            "previous,now,1",
            "previous,future,0",
            "now,now,2",
            "now,future,1",
            "future,future,0"
    )
    fun `History scheduled today returns all items that are scheduled today`(mockState1: String, mockState2: String, expectedRoundsFound: Int) {

        initMockStates(mockState1, mockState2)

        val mock = History(2020, null, listOf(mockHistoryRound1, mockHistoryRound2))

        assertEquals(expectedRoundsFound, mock.scheduledToday)
    }

    private fun initMockStates(mockState1: String, mockState2: String) {
        mockHistoryRound1 = HistoryRound(
                date = getDate(mockState1),
                season = 2020, round = 1, raceName = "test", circuitId = "test", circuitName = "test", country = "test", countryISO = "test", hasQualifying = false, hasResults = false
        )
        mockHistoryRound2 = HistoryRound(
                date = getDate(mockState2),
                season = 2020, round = 2, raceName = "test", circuitId = "test", circuitName = "test", country = "test", countryISO = "test", hasQualifying = false, hasResults = false
        )
    }
    private fun getDate(mockState: String): LocalDate {
        return when (mockState) {
            "previous" -> previous
            "now" -> now
            "future" -> future
            else -> throw Exception("Test error, mock state is invalid")
        }
    }
}