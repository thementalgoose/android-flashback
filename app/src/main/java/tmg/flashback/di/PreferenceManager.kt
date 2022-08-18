package tmg.flashback.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.managers.AppPreferencesManager
import tmg.flashback.prefs.manager.PreferenceManager

@Module
@InstallIn(SingletonComponent::class)
class PreferenceManager {

    @Provides
    fun providesPreferenceManager(impl: AppPreferencesManager): PreferenceManager = impl
}