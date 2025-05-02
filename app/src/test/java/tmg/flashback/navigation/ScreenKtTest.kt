package tmg.flashback.navigation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.navigation.constants.Deeplink

class ScreenKtTest {

    @ParameterizedTest
    @CsvSource(
        "Search,Search",
        "Rss,Rss"
    )
    fun `deeplink routes line up with screen routes`(deeplinkRoute: Deeplink, expectedScreen: Screen) {
        assertEquals(expectedScreen.route, deeplinkRoute.route)
    }
}