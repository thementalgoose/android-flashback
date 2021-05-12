package tmg.core.device.di

import org.koin.dsl.module
import tmg.core.device.managers.AndroidNetworkConnectivityManager
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.core.prefs.SharedPreferenceRepository

val deviceModule = module {
    single<NetworkConnectivityManager> { AndroidNetworkConnectivityManager(get()) }
    single { tmg.core.prefs.SharedPreferenceRepository(get()) }
    single { tmg.core.device.repository.DeviceRepository(get()) }
}