package tmg.flashback.repo.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import tmg.flashback.repo.models.Timestamp
import tmg.flashback.repo.models.remoteconfig.SupportedArticleSource
import tmg.flashback.repo.models.remoteconfig.UpNextSchedule

internal class RemoteConfigRepositoryTest {

    private val stub = RemoteConfigRepositoryStub()

    @Test
    fun `RemoteConfigRepository supported seasons returns non cached value`() {

        val input1 = setOf(2018)
        val input2 = setOf(2017)

        stub.inputSupportedSeasons = input1
        assertEquals(stub.supportedSeasons, input1)

        stub.inputSupportedSeasons = input2
        assertEquals(stub.supportedSeasons, input2)
    }

    @Test
    fun `RemoteConfigRepository default season returns cached value`() {

        val input1 = 2018
        val input2 = 2017

        stub.inputDefaultSeason = input1
        assertEquals(stub.defaultSeason, input1)

        stub.inputDefaultSeason= input2
        assertEquals(stub.defaultSeason, input1)
    }

    @Test
    fun `RemoteConfigRepository up next list returns non cached value`() {

        val input1 = listOf(UpNextSchedule(1,0,"", Timestamp(LocalDate.now()),null,null,null))
        val input2 = listOf(UpNextSchedule(2,0,"", Timestamp(LocalDate.now()),null,null,null))

        stub.inputUpNext = input1
        assertEquals(stub.upNext, input1)

        stub.inputUpNext = input2
        assertEquals(stub.upNext, input2)
    }

    @Test
    fun `RemoteConfigRepository banner returns non cached value`() {

        val input1 = "banner value 1"
        val input2 = "banner value 2"

        stub.inputBanner = input1
        assertEquals(stub.banner, input1)

        stub.inputBanner = input2
        assertEquals(stub.banner, input2)
    }

    @Test
    fun `RemoteConfigRepository data provided by returns non cached value`() {

        val input1 = "welp"
        val input2 = "second"

        stub.inputDataProvidedBy = input1
        assertEquals(stub.dataProvidedBy, input1)

        stub.inputDataProvidedBy = input2
        assertEquals(stub.dataProvidedBy, input2)
    }

    @Test
    fun `RemoteConfigRepository search returns cached value`() {

        val input1 = true
        val input2 = false

        stub.inputSearch = input1
        assertEquals(stub.search, input1)

        stub.inputSearch = input2
        assertEquals(stub.search, input1)
    }

    //region RSS

    @Test
    fun `RemoteConfigRepository rss returns cached value`() {

        val input1 = true
        val input2 = false

        stub.inputRss = input1
        assertEquals(stub.rss, input1)

        stub.inputRss = input2
        assertEquals(stub.rss, input1)
    }

    @Test
    fun `RemoteConfigRepository rss add custom returns cached value`() {

        val input1 = true
        val input2 = false

        stub.inputRssAddCustom = input1
        assertEquals(stub.rssAddCustom, input1)

        stub.inputRssAddCustom = input2
        assertEquals(stub.rssAddCustom, input1)
    }


    @Test
    fun `RemoteConfigRepository rss supported sources returns cached value`() {

        val input1 = listOf(SupportedArticleSource("AGAIN", "", "", "", "", "", ""))
        val input2 = listOf(SupportedArticleSource("INFO", "", "", "", "", "", ""))

        stub.inputRssSupportedSources = input1
        assertEquals(stub.rssSupportedSources, input1)

        stub.inputRssSupportedSources = input2
        assertEquals(stub.rssSupportedSources, input1)
    }

    //endregion
}