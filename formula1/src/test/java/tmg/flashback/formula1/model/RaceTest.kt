package tmg.flashback.formula1.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.formula1.model.QualifyingType.Q1
import tmg.flashback.formula1.model.QualifyingType.Q2
import tmg.flashback.formula1.model.QualifyingType.Q3
import tmg.utilities.extensions.toEnum

internal class RaceTest {

    @Test
    fun `drivers and constructor initialises to list of all info and qualifying data`() {
        val constructor1 = Constructor.model(id = "constructor1")
        val constructor2 = Constructor.model(id = "constructor2")
        val driver1 = DriverConstructor.model(driver = Driver.model(id = "driver1"), constructor = constructor1)
        val driver2 = DriverConstructor.model(driver = Driver.model(id = "driver2"), constructor = constructor1)
        val driver3 = DriverConstructor.model(driver = Driver.model(id = "driver2"), constructor = constructor2)
        val driver4 = DriverConstructor.model(driver = Driver.model(id = "driver3"), constructor = constructor2)

        val model = Race.model(
            qualifying = listOf(QualifyingRound.model(results = listOf(
                QualifyingResult.model(
                    driver = driver1
                ),
                QualifyingResult.model(
                    driver = driver2
                ),
                QualifyingResult.model(
                    driver = driver1
                ),
                QualifyingResult.model(
                    driver = driver3
                )
            ))),
            race = listOf(RaceResult.model(
                driver = driver4
            ))
        )

        assertEquals(listOf(constructor1, constructor2), model.constructors)
        assertEquals(listOf(driver1, driver2, driver3, driver4), model.drivers)
    }

    @ParameterizedTest(name = "has({1}) with data containing {0} returns {1}")
    @CsvSource(
        "Q1|Q2|Q3,Q1,true",
        "Q1|Q2|Q3,Q2,true",
        "Q1|Q2|Q3,Q3,true",
        "Q1|Q2|Q3,Q1,true",
        "Q1|Q2|Q3,Q2,true",
        "Q1|Q2|Q3,Q3,true",
        "Q1,Q1,true",
        "Q1,Q2,false",
        "Q1,Q3,false"
    )
    fun `has qualifying type with data loaded returns true`(input: String, query: QualifyingType, expectedResult: Boolean) {
        val inputQualifying = input.split("|").map { it.toEnum<QualifyingType>()!! }
        val model = Race.model(qualifying = inputQualifying.map { qualiType ->
            QualifyingRound.model(label = qualiType)
        })

        assertEquals(expectedResult, model.has(query))
    }

    @Test
    fun `has data returns true if qualifying is not empty`() {
        val model = Race.model(
            qualifying = listOf(QualifyingRound.model()),
            race = emptyList()
        )

        assertEquals(true, model.hasData)
    }

    @Test
    fun `has data returns true if race is not empty`() {
        val model = Race.model(
            qualifying = emptyList(),
            race = listOf(RaceResult.model())
        )

        assertEquals(true, model.hasData)
    }

    @Test
    fun `has data returns true if qualifying and race is not empty`() {
        val model = Race.model(
            qualifying = listOf(QualifyingRound.model()),
            race = listOf(RaceResult.model())
        )

        assertEquals(true, model.hasData)
    }

    @Test
    fun `has data returns false if qualifying and race are empty`() {
        val model = Race.model(
            qualifying = emptyList(),
            race = emptyList()
        )

        assertEquals(false, model.hasData)
    }

    @Test
    fun `q1 fastest lap`() {
        val fastest = LapTime.model(hours = 1, mins = 2, seconds = 3, milliseconds = 101)

        val driver1 = Driver.model(id = "1")
        val driver2 = Driver.model(id = "2")
        val model = Race.model(qualifying = listOf(
            QualifyingRound(
                Q1, 1, listOf(
                QualifyingResult.model(
                    driver = DriverConstructor.model(driver = driver1),
                    lapTime = fastest
                ),
                QualifyingResult.model(
                    driver = DriverConstructor.model(driver = driver2),
                    lapTime = LapTime.model(hours = 1, mins = 2, seconds = 3, milliseconds = 102)
                )
            ))
        ))

        assertEquals(fastest, model.q1FastestLap)
    }

    @Test
    fun `q2 fastest lap`() {
        val fastest = LapTime.model(hours = 1, mins = 2, seconds = 3, milliseconds = 13)

        val driver1 = Driver.model(id = "1")
        val driver2 = Driver.model(id = "2")
        val model = Race.model(qualifying = listOf(
            QualifyingRound(
                Q2, 1, listOf(
                QualifyingResult.model(
                    driver = DriverConstructor.model(driver = driver1),
                    lapTime = fastest
                ),
                QualifyingResult.model(
                    driver = DriverConstructor.model(driver = driver2),
                    lapTime = LapTime.model(hours = 1, mins = 2, seconds = 3, milliseconds = 102)
                )
            ))
        ))

        assertEquals(fastest, model.q2FastestLap)
    }

    @Test
    fun `q3 fastest lap`() {
        val fastest = LapTime.model(hours = 1, mins = 2, seconds = 3, milliseconds = 101)

        val driver1 = Driver.model(id = "1")
        val driver2 = Driver.model(id = "2")
        val model = Race.model(qualifying = listOf(
            QualifyingRound(Q3, 1, listOf(
                QualifyingResult.model(
                    driver = DriverConstructor.model(driver = driver1),
                    lapTime = fastest
                ),
                QualifyingResult.model(
                    driver = DriverConstructor.model(driver = driver2),
                    lapTime = LapTime.model(hours = 1, mins = 2, seconds = 3, milliseconds = 102)
                )
            ))
        ))

        assertEquals(fastest, model.q3FastestLap)
    }

    @Test
    fun `driver overview with all driver values`() {
        val driver = Driver.model(id = "mDriver")
        val q1 = QualifyingResult.model(
            driver = DriverConstructor.model(driver = driver),
            position = 2
        )
        val q2 = QualifyingResult.model(
            driver = DriverConstructor.model(driver = driver),
            position = 3
        )
        val q3 = QualifyingResult.model(
            driver = DriverConstructor.model(driver = driver),
            position = 4
        )
        val qSprint = SprintRaceResult.model(
            driver = DriverConstructor.model(driver = driver),
        )
        val race = RaceResult.model(
            driver = DriverConstructor.model(driver = driver)
        )

        val model = Race.model(
            qualifying = listOf(
                QualifyingRound.model(Q1, 1, listOf(q1)),
                QualifyingRound.model(Q2, 2, listOf(q2)),
                QualifyingRound.model(Q3, 3, listOf(q3)),
            ),
            sprint = listOf(qSprint),
            race = listOf(
                race
            )
        )

        val expectedDriverOverview = RaceDriverOverview.model(
            driver = DriverConstructor.model(driver = driver),
            q1 = q1,
            q2 = q2,
            q3 = q3,
            qSprint = qSprint,
            race = race
        )

        assertEquals(expectedDriverOverview, model.driverOverview(driver.id))
    }

    @Test
    fun `driver overview with unknown driver id returns null`() {
        val driver = Driver.model(id = "mDriver")
        val q1 = QualifyingResult.model(
            driver = DriverConstructor.model(driver = driver),
            position = 2
        )
        val q2 = QualifyingResult.model(
            driver = DriverConstructor.model(driver = driver),
            position = 3
        )
        val q3 = QualifyingResult.model(
            driver = DriverConstructor.model(driver = driver),
            position = 4
        )
        val qSprint = SprintRaceResult.model(
            driver = DriverConstructor.model(driver = driver),
        )
        val race = RaceResult.model(
            driver = DriverConstructor.model(driver = driver)
        )

        val model = Race.model(
            qualifying = listOf(
                QualifyingRound.model(Q1, 1, listOf(q1)),
                QualifyingRound.model(Q2, 2, listOf(q2)),
                QualifyingRound.model(Q3, 3, listOf(q3)),
            ),
            sprint = listOf(qSprint),
            race = listOf(
                race
            )
        )

        assertNull(model.driverOverview("UNKNOWN"))
    }

    @Test
    fun `driver overview with all no sprint driver`() {
        val driver = Driver.model(id = "mDriver")
        val q1 = QualifyingResult.model(
            driver = DriverConstructor.model(driver = driver),
            position = 2
        )
        val q2 = QualifyingResult.model(
            driver = DriverConstructor.model(driver = driver),
            position = 3
        )
        val q3 = QualifyingResult.model(
            driver = DriverConstructor.model(driver = driver),
            position = 4
        )
        val qSprint = SprintRaceResult.model(
            driver = DriverConstructor.model(driver = Driver.model()),
        )
        val race = RaceResult.model(
            driver = DriverConstructor.model(driver = driver)
        )

        val model = Race.model(
            qualifying = listOf(
                QualifyingRound.model(Q1, 1, listOf(q1)),
                QualifyingRound.model(Q2, 2, listOf(q2)),
                QualifyingRound.model(Q3, 3, listOf(q3))
            ),
            sprint = listOf(
                qSprint
            ),
            race = listOf(
                race
            )
        )

        val expectedDriverOverview = RaceDriverOverview.model(
            driver = DriverConstructor.model(driver = driver),
            q1 = q1,
            q2 = q2,
            q3 = q3,
            qSprint = null,
            race = race
        )

        assertEquals(expectedDriverOverview, model.driverOverview(driver.id))
    }

    @Test
    fun `constructor standings combines points based`() {

        val constructor1 = Constructor.model(id = "1")
        val constructor2 = Constructor.model(id = "2")

        val model = Race.model(
            qualifying = emptyList(),
            race = listOf(
                RaceResult.model(
                    driver = DriverConstructor.model(constructor = constructor1),
                    points = 2.0
                ),
                RaceResult.model(
                    driver = DriverConstructor.model(constructor = constructor2),
                    points = 4.0
                ),
                RaceResult.model(
                    driver = DriverConstructor.model(constructor = constructor1),
                    points = 3.0
                ),
                RaceResult.model(
                    driver = DriverConstructor.model(constructor = constructor2),
                    points = 9.0
                )
            )
        )
        val expected = listOf(
            RaceConstructorStandings.model(points = 13.0, constructor = constructor2),
            RaceConstructorStandings.model(points = 5.0, constructor = constructor1)
        )

        assertEquals(expected, model.constructorStandings)
    }

    @Test
    fun `constructor standings combines points based including sprint qualifying`() {

        val constructor1 = Constructor.model(id = "1")
        val constructor2 = Constructor.model(id = "2")

        val model = Race.model(
            qualifying = emptyList(),
            sprint = listOf(
                SprintRaceResult.model(
                    driver = DriverConstructor.model(constructor = constructor1),
                    points = 2.0
                )
            ),
            race = listOf(
                RaceResult.model(
                    driver = DriverConstructor.model(constructor = constructor1),
                    points = 2.0
                ),
                RaceResult.model(
                    driver = DriverConstructor.model(constructor = constructor2),
                    points = 4.0
                ),
                RaceResult.model(
                    driver = DriverConstructor.model(constructor = constructor1),
                    points = 3.0
                ),
                RaceResult.model(
                    driver = DriverConstructor.model(constructor = constructor2),
                    points = 9.0
                )
            )
        )
        val expected = listOf(
            RaceConstructorStandings.model(points = 13.0, constructor = constructor2),
            RaceConstructorStandings.model(points = 5.0 + 2.0, constructor = constructor1)
        )

        assertEquals(expected, model.constructorStandings)
    }
}