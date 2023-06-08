package tmg.flashback.widgets.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import tmg.flashback.domain.repo.ScheduleRepository
import tmg.flashback.widgets.contract.WidgetNavigationComponent
import tmg.flashback.widgets.repository.WidgetRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface WidgetsEntryPoints {
    fun widgetsRepository(): WidgetRepository
    fun widgetsNavigationComponent(): WidgetNavigationComponent
    fun scheduleRepository(): ScheduleRepository

    companion object {
        fun get(context: Context): WidgetsEntryPoints = EntryPointAccessors.fromApplication(context, WidgetsEntryPoints::class.java)
    }
}

