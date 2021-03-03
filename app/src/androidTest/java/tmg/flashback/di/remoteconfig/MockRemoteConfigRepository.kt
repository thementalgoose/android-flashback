package tmg.flashback.di.remoteconfig

import tmg.flashback.core.managers.ConfigurationManager
import tmg.flashback.core.model.ForceUpgrade
import tmg.flashback.core.model.SupportedSource
import tmg.flashback.core.model.UpNextSchedule
import tmg.flashback.core.repositories.ConfigurationRepository

internal object MockRemoteConfigRepository: ConfigurationRepository {

    override val upNext: List<UpNextSchedule>
        get() = emptyList()
    override val supportedSeasons: Set<Int>
        get() = setOf(2018, 2019, 2020, 2021)
    override val defaultSeason: Int
        get() = 2019
    override val banner: String
        get() = "MOCK BANNER"
    override val forceUpgrade: ForceUpgrade?
        get() = null
    override val rss: Boolean
        get() = true
    override val rssAddCustom: Boolean
        get() = false
    override val rssSupportedSources: List<SupportedSource>
        get() = emptyList()
    override val dataProvidedBy: String
        get() = "Mock"
    override val search: Boolean
        get() = false
}