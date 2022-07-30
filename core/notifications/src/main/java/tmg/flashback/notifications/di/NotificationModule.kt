package tmg.flashback.notifications.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.notifications.managers.FirebaseRemoteNotificationManager
import tmg.flashback.notifications.managers.RemoteNotificationManager

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationModule {

    @Binds
    abstract fun bindRemoteNotificationManager(impl: FirebaseRemoteNotificationManager): RemoteNotificationManager
}