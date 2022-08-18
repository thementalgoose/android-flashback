package tmg.flashback.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.appshortcuts.provider.HomeClassProvider
import tmg.flashback.di.navigation.AppHomeClassProvider
import tmg.flashback.managers.AppApplicationNavigationComponent
import tmg.flashback.notifications.navigation.NotificationNavigationProvider
import tmg.flashback.ui.navigation.ApplicationNavigationComponent

@Module
@InstallIn(SingletonComponent::class)
class NavigationModule {

    @Provides
    fun providesApplicationNavigationComponent(impl: AppApplicationNavigationComponent): ApplicationNavigationComponent = impl

    @Provides
    fun providesNotificationNavigationProvider(impl: AppApplicationNavigationComponent): NotificationNavigationProvider = impl

    @Provides
    fun providesHomeClassProvider(impl: AppHomeClassProvider): HomeClassProvider = impl
}
