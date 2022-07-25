package tmg.flashback.appshortcuts.di

import org.koin.dsl.module
import tmg.flashback.appshortcuts.manager.AppShortcutManager

val appShortcutModule = module {
    single { AppShortcutManager(get(), get()) }
}