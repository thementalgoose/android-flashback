package tmg.flashback.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.di.navigation.AppWidgetNavigationComponent
import tmg.flashback.widgets.contract.WidgetNavigationComponent


@Module
@InstallIn(SingletonComponent::class)
class WidgetModule {

    @Provides
    fun bindsWidgetNavigationComponent(impl: AppWidgetNavigationComponent): WidgetNavigationComponent = impl
}