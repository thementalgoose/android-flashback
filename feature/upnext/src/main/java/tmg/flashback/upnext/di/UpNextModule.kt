package tmg.flashback.upnext.di

import org.koin.dsl.module
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.repository.UpNextRepository

val upNextModule = module {
    single { UpNextController(get()) }
    single { UpNextRepository(get()) }
}