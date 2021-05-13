package tmg.flashback.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tmg.flashback.ui.SplashViewModel

val appModule = module {

    viewModel { SplashViewModel(get(), get(), get()) }
//    viewModel { }
}