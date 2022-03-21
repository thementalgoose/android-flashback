package tmg.flashback.ui.di

import org.koin.dsl.module
import tmg.flashback.ui.animation.GlideProvider
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase

val uiModule = module {

    single { ThemeRepository(get(), get()) }
    single { tmg.flashback.ui.animation.GlideProvider() }

    factory { ChangeNightModeUseCase(get()) }

}