package tmg.flashback.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.di.navigation.AppWidgetNavigationComponent
import tmg.flashback.managers.widgets.AppWidgetManager
import tmg.flashback.managers.widgets.WidgetManager
import tmg.flashback.widgets.WidgetNavigationComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class WidgetModule() {

    @Binds
    abstract fun bindsWidgetNavigationComponent(impl: AppWidgetNavigationComponent): WidgetNavigationComponent

    @Binds
    abstract fun bindsWidgetManager(impl: AppWidgetManager): WidgetManager

}