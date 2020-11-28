package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.di.toggle.FlashbackToggleDB
import tmg.flashback.notifications.FirebasePushNotificationManager
import tmg.flashback.notifications.PushNotificationManager
import tmg.flashback.firebase.repos.*
import tmg.flashback.repo.toggle.ToggleDB
import tmg.flashback.repo.db.stats.*

val firebaseModule = module {

    single<SeasonOverviewDB> { SeasonOverviewFirestore(get()) }
    single<HistoryDB> { HistoryFirestore(get()) }
    single<CircuitDB> { CircuitFirestore(get()) }
    single<DataDB> { DataFirestore(get()) }
    single<DriverDB> { DriverFirestore(get()) }
    single<ConstructorDB> { ConstructorFirestore(get()) }

    single<ToggleDB> { FlashbackToggleDB(get()) }
}
