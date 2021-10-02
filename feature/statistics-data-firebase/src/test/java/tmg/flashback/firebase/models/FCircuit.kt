package tmg.flashback.firebase.models

internal fun FCircuit.Companion.model(
    circuitName: String = "circuitName",
    country: String = "country",
    countryISO: String? = "countryISO",
    id: String = "circuitId",
    locality: String = "locality",
    locationLat: Double? = 1.0,
    locationLng: Double? = 2.0,
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
    wikiUrl = wikiUrl,
    results = results
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