package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.managers.AndroidAppShortcutManager
import tmg.flashback.managers.AppShortcutManager

var shortcutModule = module {

    // App shortcuts
    single<AppShortcutManager> { AndroidAppShortcutManager(get()) }
}