package tmg.flashback.navigation.constants

const val SCREEN_PARAM = "screen"

sealed class Deeplink(
    val route: String
) {
    data object Search: Deeplink("search")
    data object Rss: Deeplink("rss")
}