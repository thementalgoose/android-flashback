package tmg.flashback.configuration.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.configuration.firebase.FirebaseRemoteConfigService
import tmg.flashback.configuration.services.RemoteConfigService

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigModule {

    @Binds
    abstract fun bindsRemoteConfigService(impl: FirebaseRemoteConfigService): RemoteConfigService
}