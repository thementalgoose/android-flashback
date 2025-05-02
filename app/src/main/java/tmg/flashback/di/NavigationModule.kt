package tmg.flashback.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.appshortcuts.provider.HomeClassProvider
import tmg.flashback.di.navigation.AppHomeClassProvider
import tmg.flashback.navigation.AppApplicationNavigationComponent
import tmg.flashback.navigation.AppInternalNavigationComponent
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.navigation.InternalNavigationComponent
import tmg.flashback.notifications.navigation.NotificationNavigationProvider

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun providesApplicationNavigationComponent(impl: AppApplicationNavigationComponent): ApplicationNavigationComponent

    @Binds
    fun providesInternalNavigationComponent(impl: AppInternalNavigationComponent): InternalNavigationComponent

    @Binds
    fun providesNotificationNavigationProvider(impl: AppApplicationNavigationComponent): NotificationNavigationProvider

    @Binds
    fun providesHomeClassProvider(impl: AppHomeClassProvider): HomeClassProvider
}
