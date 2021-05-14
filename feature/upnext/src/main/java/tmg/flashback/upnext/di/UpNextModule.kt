package tmg.flashback.upnext.di

import org.koin.dsl.module
import tmg.flashback.upnext.controllers.UpNextController

val upNextModule = module {
    single { UpNextController(get()) }
}