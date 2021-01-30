package tmg.flashback.repo.config

import org.threeten.bp.LocalDate
import tmg.flashback.repo.models.Timestamp
import tmg.flashback.repo.models.remoteconfig.SupportedArticleSource
import tmg.flashback.repo.models.remoteconfig.UpNextSchedule

class RemoteConfigRepositoryStub(
        var inputSupportedSeasons: Set<Int> = setOf(2018),
        var inputDefaultSeason: Int = 2018,
        var inputUpNext: List<UpNextSchedule> = listOf(UpNextSchedule(0,0,"", Timestamp(LocalDate.now()), null,null,null)),
        var inputBanner: String? = "banner",
        var inputDataProvidedBy: String? = "provided_by",
        var inputSearch: Boolean = true,
        var inputRss: Boolean = true,
        var inputRssAddCustom: Boolean = true,
        var inputRssSupportedSources: List<SupportedArticleSource> = listOf(SupportedArticleSource("", "", "", "", "", "", ""))
): RemoteConfigRepository() {

    override val supportedSeasonsRC: Set<Int> get() = inputSupportedSeasons
    override val defaultSeasonRC: Int get() = inputDefaultSeason
    override val upNextRC: List<UpNextSchedule> get() = inputUpNext
    override val bannerRC: String? get() = inputBanner
    override val dataProvidedByRC: String? get() = inputDataProvidedBy
    override val searchRC: Boolean get() = inputSearch

    override val rssRC: Boolean get() = inputRss
    override val rssAddCustomRC: Boolean get() = inputRssAddCustom
    override val rssSupportedSourcesRC: List<SupportedArticleSource> get() = inputRssSupportedSources
}