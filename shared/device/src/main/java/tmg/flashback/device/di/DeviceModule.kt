package tmg.flashback.device.di

import org.koin.dsl.module
import tmg.flashback.device.managers.AndroidNetworkConnectivityManager
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.device.repository.DeviceRepository
import tmg.flashback.device.repository.SharedPreferenceRepository
import tmg.utilities.prefs.SharedPrefManager

val deviceModule = module {
    single<NetworkConnectivityManager> { AndroidNetworkConnectivityManager(get()) }
    single { SharedPreferenceRepository(get()) }
    single { DeviceRepository(get()) }
}