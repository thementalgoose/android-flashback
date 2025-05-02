package tmg.flashback.navigation

val Screen.route: String
    get() = when (this) {
        is Screen.Circuit -> Screen.Circuit.route
        is Screen.Constructor -> Screen.Constructor.route
        is Screen.Driver -> Screen.Driver.route
        Screen.ConstructorStandings -> "results/constructors"
        Screen.DriverStandings -> "results/drivers"
        Screen.Races -> "results/races"
        Screen.PrivacyPolicy -> "privacy_policy"
        Screen.Rss -> "rss"
        Screen.Settings -> "settings"
        Screen.ReactionGame -> "reaction"
        Screen.Search -> "search"
    }

val Screen.Circuit.Companion.route: String
    get() = "circuits/{data}"

val Screen.Driver.Companion.route: String
    get() = "driver/{data}"

val Screen.Constructor.Companion.route: String
    get() = "constructors/{data}"