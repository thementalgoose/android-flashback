package tmg.flashback.flashbackapi.api.api

import androidx.annotation.Keep
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import tmg.flashback.flashbackapi.api.models.MetadataWrapper
import tmg.flashback.flashbackapi.api.models.circuits.AllCircuits
import tmg.flashback.flashbackapi.api.models.circuits.CircuitHistory
import tmg.flashback.flashbackapi.api.models.constructors.AllConstructors
import tmg.flashback.flashbackapi.api.models.constructors.ConstructorHistory
import tmg.flashback.flashbackapi.api.models.drivers.AllDrivers
import tmg.flashback.flashbackapi.api.models.drivers.DriverHistory
import tmg.flashback.flashbackapi.api.models.news.News
import tmg.flashback.flashbackapi.api.models.overview.Event
import tmg.flashback.flashbackapi.api.models.overview.Overview
import tmg.flashback.flashbackapi.api.models.races.Round
import tmg.flashback.flashbackapi.api.models.races.Season

@Keep
interface FlashbackApi {

    @GET("overview.json")
    suspend fun getOverview(): Response<MetadataWrapper<Overview>>

    @GET("overview/{season}.json")
    suspend fun getOverview(@Path("season") season: Int): Response<MetadataWrapper<Overview>>

    @GET("overview/{season}/events.json")
    suspend fun getOverviewEvents(@Path("season") season: Int): Response<MetadataWrapper<List<Event>>>

    @GET("drivers.json")
    suspend fun getDrivers(): Response<MetadataWrapper<AllDrivers>>

    @GET("constructors.json")
    suspend fun getConstructors(): Response<MetadataWrapper<AllConstructors>>

    @GET("circuits.json")
    suspend fun getCircuits(): Response<MetadataWrapper<AllCircuits>>

    @GET("circuits/{id}.json")
    suspend fun getCircuit(@Path("id") id: String): Response<MetadataWrapper<CircuitHistory>>

    @GET("drivers/{id}.json")
    suspend fun getDriver(@Path("id") id: String): Response<MetadataWrapper<DriverHistory>>

    @GET("constructors/{id}.json")
    suspend fun getConstructor(@Path("id") id: String): Response<MetadataWrapper<ConstructorHistory>>

    @GET("races/{season}.json")
    suspend fun getSeason(@Path("season") season: Int): Response<MetadataWrapper<Season>>

    @GET("races/{season}/{round}.json")
    suspend fun getSeason(@Path("season") season: Int, @Path("round") round: Int): Response<MetadataWrapper<Round>>

    @GET("races/{season}/events.json")
    suspend fun getSeasonEvents(@Path("season") season: Int): Response<MetadataWrapper<List<Event>>>
}