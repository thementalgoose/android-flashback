package tmg.flashback.statistics.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import tmg.flashback.statistics.network.models.MetadataWrapper
import tmg.flashback.statistics.network.models.circuits.AllCircuits
import tmg.flashback.statistics.network.models.circuits.Circuits
import tmg.flashback.statistics.network.models.constructors.AllConstructors
import tmg.flashback.statistics.network.models.constructors.Constructors
import tmg.flashback.statistics.network.models.drivers.AllDrivers
import tmg.flashback.statistics.network.models.drivers.Drivers
import tmg.flashback.statistics.network.models.overview.Overview
import tmg.flashback.statistics.network.models.races.Round
import tmg.flashback.statistics.network.models.races.Season

interface FlashbackApi {

    @GET("overview.json")
    suspend fun getOverview(): Response<MetadataWrapper<Overview>>

    @GET("overview/{season}.json")
    suspend fun getOverview(@Path("season") season: Int): Response<MetadataWrapper<Overview>>

    @GET("drivers.json")
    suspend fun getDrivers(): Response<MetadataWrapper<AllDrivers>>

    @GET("constructors.json")
    suspend fun getConstructors(): Response<MetadataWrapper<AllConstructors>>

    @GET("circuits.json")
    suspend fun getCircuits(): Response<MetadataWrapper<AllCircuits>>

    @GET("circuits/{id}.json")
    suspend fun getCircuit(@Path("id") id: String): Response<MetadataWrapper<Circuits>>

    @GET("drivers/{id}.json")
    suspend fun getDriver(@Path("id") id: String): Response<MetadataWrapper<Drivers>>

    @GET("constructors/{id}.json")
    suspend fun getConstructor(@Path("id") id: String): Response<MetadataWrapper<Constructors>>

    @GET("races/{season}.json")
    suspend fun getSeason(@Path("season") season: Int): Response<MetadataWrapper<Season>>

    @GET("races/{season}/{round}.json")
    suspend fun getSeason(@Path("season") season: Int, @Path("round") round: Int): Response<MetadataWrapper<Round>>
}