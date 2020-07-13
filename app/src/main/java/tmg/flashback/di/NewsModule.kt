package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.env
import tmg.flashback.news.News
import tmg.flashback.repo.db.news.NewsDB

val newsModule = module {

    single<NewsDB> { News(get(), env.isLive) }
}