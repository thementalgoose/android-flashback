package tmg.flashback.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.managers.AppPreferencesManager
import tmg.flashback.prefs.manager.PreferenceManager

@Module
@InstallIn(SingletonComponent::class)
interface PreferenceManager {

    @Binds
    fun providesPreferenceManager(impl: AppPreferencesManager): PreferenceManager
}