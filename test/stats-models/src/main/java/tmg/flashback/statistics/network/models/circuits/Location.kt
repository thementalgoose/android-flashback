package tmg.flashback.statistics.network.models.circuits

fun Location.Companion.model(
    lat: String = "51.101",
    lng: String = "-1.101"
): Location = Location(
    lat = lat,
    lng = lng
)