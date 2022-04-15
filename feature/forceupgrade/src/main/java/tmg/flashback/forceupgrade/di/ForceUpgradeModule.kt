package tmg.flashback.forceupgrade.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.forceupgrade.ForceUpgradeNavigationComponent
import tmg.flashback.forceupgrade.repository.ForceUpgradeRepository
import tmg.flashback.forceupgrade.ui.forceupgrade.ForceUpgradeViewModel

val forceUpgradeModule = module {
    single { ForceUpgradeNavigationComponent(get()) }
    single { ForceUpgradeRepository(get()) }
    viewModel { ForceUpgradeViewModel(get(), get()) }
}