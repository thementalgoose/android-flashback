package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.BuildConfig
import tmg.flashback.BuildConfig.DEBUG
import tmg.flashback.Env
import tmg.flashback.env
import tmg.flashback.news.News
import tmg.flashback.news.apis.autosport.AutosportRssRetrofit
import tmg.flashback.news.apis.autosport.buildRetrofitAutosport
import tmg.flashback.repo.db.news.NewsDB

val newsModule = module {

    single<NewsDB> { News(get(), env.isLive) }
}