package tmg.flashback.ui.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import tmg.flashback.ui.animation.GlideProvider
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.NavigationService
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase

val uiModule = module {

    single { ActivityProvider(get()) }
    single { NavigationService(androidApplication()) }

    single { ThemeRepository(get(), get()) }
    single { GlideProvider() }

    factory { ChangeNightModeUseCase(get()) }

}