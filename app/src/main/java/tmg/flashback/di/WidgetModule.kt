package tmg.flashback.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.di.navigation.AppWidgetNavigationComponent
import tmg.flashback.widgets.upnext.navigation.WidgetNavigationComponent


@Module
@InstallIn(SingletonComponent::class)
interface WidgetModule {

    @Binds
    fun bindsWidgetNavigationComponent(impl: AppWidgetNavigationComponent): WidgetNavigationComponent
}