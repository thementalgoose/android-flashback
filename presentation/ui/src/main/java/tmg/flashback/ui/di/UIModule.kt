package tmg.flashback.ui.di

import org.koin.dsl.module
import tmg.flashback.ui.animation.GlideProvider
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.ApplicationNavigationProvider
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase

val uiModule = module {

    single { ActivityProvider(get()) }

    single { ThemeRepository(get(), get()) }
    single { GlideProvider() }

    factory { ChangeNightModeUseCase(get()) }

}