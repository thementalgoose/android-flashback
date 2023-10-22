package tmg.flashback.weekend.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.weekend.WeekendNavigationComponentImpl
import tmg.flashback.weekend.contract.WeekendNavigationComponent

@Module
@InstallIn(SingletonComponent::class)
class WeekendModule {

    @Provides
    fun provideWeekendNavigationComponent(impl: WeekendNavigationComponentImpl): WeekendNavigationComponent = impl
}