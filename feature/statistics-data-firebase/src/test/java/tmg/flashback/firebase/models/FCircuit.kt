package tmg.flashback.firebase.models

internal fun FCircuit.Companion.model(
    circuitName: String = "circuitName",
    country: String = "country",
    countryISO: String? = "countryISO",
    id: String = "circuitId",
    locality: String = "locality",
    locationLat: Double? = null,
    locationLng: Double? = null,
    location: FCircuitLocation? = FCircuitLocation.model(),
    wikiUrl: String? = "wikiUrl",
    results: Map<String, FCircuitResult>? = mapOf(
        "s2020r1" to FCircuitResult.model()
    )
): FCircuit = FCircuit(
    circuitName = circuitName,
    country = country,
    countryISO = countryISO,
    id = id,
    locality = locality,
    locationLat = locationLat,
    locationLng = locationLng,
    location = location,
    wikiUrl = wikiUrl,
    results = results
)

internal fun FCircuitLocation.Companion.model(
    lat: String? = "1.0",
    lng: String? = "2.0"
): FCircuitLocation = FCircuitLocation(
    lat = lat,
    lng = lng
)

internal fun FCircuitResult.Companion.model(
    date: String? = "2020-01-01",
    time: String? = "12:00:00",
    name: String = "name",
    season: Int = 2020,
    round: Int = 1,
    wikiUrl: String? = "wikiUrl"
): FCircuitResult = FCircuitResult(
    date = date,
    time = time,
    name = name,
    season = season,
    round = round,
    wikiUrl = wikiUrl,
)