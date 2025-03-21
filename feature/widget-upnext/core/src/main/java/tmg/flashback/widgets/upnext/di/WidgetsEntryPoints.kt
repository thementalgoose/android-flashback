package tmg.flashback.widgets.upnext.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import tmg.flashback.data.repo.ScheduleRepository
import tmg.flashback.widgets.upnext.contract.WidgetNavigationComponent
import tmg.flashback.widgets.upnext.repository.UpNextWidgetRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface WidgetsEntryPoints {
    fun widgetsRepository(): UpNextWidgetRepository
    fun widgetsNavigationComponent(): WidgetNavigationComponent
    fun scheduleRepository(): ScheduleRepository

    companion object {
        fun get(context: Context): WidgetsEntryPoints = EntryPointAccessors.fromApplication(context, WidgetsEntryPoints::class.java)
    }
}

