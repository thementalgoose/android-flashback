package tmg.flashback.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.managers.AppStyleManager
import tmg.flashback.ui.managers.StyleManager

@Module
@InstallIn(SingletonComponent::class)
class ThemeModule {

    @Provides
    fun provideStyleManager(impl: AppStyleManager): StyleManager = impl
}