package tmg.flashback.privacypolicy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.privacypolicy.PrivacyPolicyNavigationComponentImpl

@Module
@InstallIn(SingletonComponent::class)
class PrivacyPolicyModule {

    @Provides
    fun providePrivacyPolicyNavigationComponent(impl: PrivacyPolicyNavigationComponentImpl): PrivacyPolicyNavigationComponent = impl
}