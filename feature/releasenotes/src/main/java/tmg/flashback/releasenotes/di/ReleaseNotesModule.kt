package tmg.flashback.releasenotes.di

import org.koin.dsl.module
import tmg.flashback.releasenotes.ReleaseNotesNavigationComponent
import tmg.flashback.releasenotes.repository.ReleaseNotesRepository
import tmg.flashback.releasenotes.usecases.NewReleaseNotesUseCase

val releaseNotesModule = module {

    single { ReleaseNotesNavigationComponent(get()) }
    factory { NewReleaseNotesUseCase(get(), get()) }
    single { ReleaseNotesRepository(get(), get()) }
}