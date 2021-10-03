package tmg.flashback.statistics.ui.search

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.data.db.stats.SearchRepository
import tmg.flashback.data.models.stats.History
import tmg.flashback.data.models.stats.HistoryRound
import tmg.flashback.data.models.stats.SearchCircuit
import tmg.flashback.data.models.stats.SearchConstructor
import tmg.flashback.data.models.stats.SearchDriver
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.test

internal class SearchViewModelTest: BaseTest() {

    private val mockSearchRepository: SearchRepository = mockk(relaxed = true)

    private lateinit var sut: SearchViewModel

    private fun initSUT() {
        sut = SearchViewModel(mockSearchRepository)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockSearchRepository.allCircuits() } returns flow {
            emit(
                listOf(
                    SearchCircuit(
                        id = "circuitId",
                        country = "country",
                        countryISO = "countryISO",
                        locationLat = 0.0,
                        locationLng = 1.0,
                        location = "location",
                        name = "name",
                        wikiUrl = "wikiUrl"
                    )
                )
            )
        }
        every { mockSearchRepository.allConstructors() } returns flow {
            emit(
                listOf(
                    SearchConstructor(
                        id = "constructorId",
                        name = "name",
                        nationality = "nationality",
                        nationalityISO = "nationalityISO",
                        wikiUrl = "wikiUrl",
                        colour = 0
                    )
                )
            )
        }
        every { mockSearchRepository.allDrivers() } returns flow {
            emit(
                listOf(
                    SearchDriver(
                        id = "driverId",
                        firstName = "firstName",
                        lastName = "lastName",
                        image = "image",
                        nationality = "nationality",
                        nationalityISO = "nationalityISO",
                        dateOfBirth = LocalDate.of(1995, 10, 12),
                        wikiUrl = "wikiUrl"
                    )
                )
            )
        }
        every { mockSearchRepository.allRaces() } returns flow {
            emit(
                listOf(
                    History(
                        season = 2020,
                        winner = null,
                        rounds = listOf(
                            HistoryRound(
                                date = LocalDate.of(2020, 1, 1),
                                season = 2020,
                                round = 1,
                                raceName = "raceName",
                                circuitId = "circuitId",
                                circuitName = "circuitName",
                                country = "country",
                                countryISO = "countryISO",
                                hasQualifying = true,
                                hasResults = true
                            )
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `search results for drivers are mapped properly`() = coroutineTest {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.DRIVER)
        advanceUntilIdle()
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.Driver(
                    driverId = "driverId",
                    name = "firstName lastName",
                    nationality = "nationality",
                    nationalityISO = "nationalityISO",
                    imageUrl = "image"
                )
            ))
        }
    }

    @Test
    fun `search results for constructors are mapped properly`() = coroutineTest {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.CONSTRUCTOR)
        advanceUntilIdle()
        sut.outputs.results.test {
            assertValueAt(listOf(
                SearchItem.Constructor(
                    constructorId = "constructorId",
                    name = "name",
                    nationality = "nationality",
                    nationalityISO = "nationalityISO",
                    colour = 0
                )
            ), 1)
        }
    }

    @Test
    fun `search results for circuit are mapped properly`() = coroutineTest {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.CIRCUIT)
        sut.outputs.results.test {
            assertValueAt(listOf(
                SearchItem.Circuit(
                    circuitId = "circuitId",
                    name = "name",
                    nationality = "country",
                    nationalityISO = "countryISO",
                    location = "location"
                )
            ), 1)
        }
    }

    @Test
    fun `search results for race are mapped properly`() = coroutineTest {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.RACE)
        sut.outputs.results.test {
            assertValue(listOf(
                SearchItem.Race(
                    raceId = "2020-1",
                    season = 2020,
                    round = 1,
                    raceName = "raceName",
                    country = "country",
                    countryISO = "countryISO",
                    circuitName = "circuitName",
                    date = LocalDate.of(2020, 1, 1)
                )
            ))
        }
    }

    @Test
    fun `search results filter items out`() = coroutineTest {
        every { mockSearchRepository.allConstructors() } returns flow {
            emit(
                listOf(
                    SearchConstructor(
                        id = "constructorId",
                        name = "name",
                        nationality = "nationality",
                        nationalityISO = "nationalityISO",
                        wikiUrl = "wikiUrl",
                        colour = 0
                    ),
                    SearchConstructor(
                        id = "constructorId",
                        name = "test",
                        nationality = "nationality",
                        nationalityISO = "nationalityISO",
                        wikiUrl = "wikiUrl",
                        colour = 0
                    )
                )
            )
        }
        initSUT()
        sut.inputs.inputCategory(SearchCategory.CONSTRUCTOR)
        sut.inputs.inputSearch("test")
        sut.outputs.results.test {
            assertValueAt(listOf(
                SearchItem.Constructor(
                    constructorId = "constructorId",
                    name = "test",
                    nationality = "nationality",
                    nationalityISO = "nationalityISO",
                    colour = 0
                )
            ), 1)
        }
    }

    @Test
    fun `open category fires open category event with input category`() = coroutineTest {
        initSUT()
        sut.inputs.inputCategory(SearchCategory.CONSTRUCTOR)
        sut.inputs.openCategory()
        sut.outputs.openCategoryPicker.test {
            assertDataEventValue(SearchCategory.CONSTRUCTOR)
        }
    }

    @Test
    fun `open category fires open category event with null default`() = coroutineTest {
        initSUT()
        sut.inputs.openCategory()
        sut.outputs.openCategoryPicker.test {
            assertDataEventValue(null)
        }
    }

    @Test
    fun `clicking an item fires open link event`() = coroutineTest {
        val input = SearchItem.Constructor("", "", "", "", 0)
        initSUT()
        sut.inputs.clickItem(input)
        sut.outputs.openLink.test {
            assertDataEventValue(input)
        }
    }
}