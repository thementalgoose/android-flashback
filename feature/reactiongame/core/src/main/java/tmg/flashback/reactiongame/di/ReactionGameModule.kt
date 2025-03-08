package tmg.flashback.reactiongame.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tmg.flashback.reactiongame.contract.usecases.IsReactionGameEnabledUseCase
import tmg.flashback.reactiongame.usecases.IsReactionGameEnabledUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal class ReactionGameModule {

    @Provides
    fun provideIsReactionGameUseCaseEnabledUseCase(
        impl: IsReactionGameEnabledUseCaseImpl
    ): IsReactionGameEnabledUseCase = impl
}