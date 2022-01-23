package tmg.flashback.ui.di

import org.koin.dsl.module
import tmg.flashback.ui.animation.GlideProvider
import tmg.flashback.ui.controllers.ThemeController

val uiModule = module {

    single { ThemeController(get(), get(), get()) }
    single { GlideProvider() }
}