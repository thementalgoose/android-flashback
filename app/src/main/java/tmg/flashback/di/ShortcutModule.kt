package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.managers.appshortcuts.AndroidAppShortcutManager
import tmg.flashback.managers.appshortcuts.AppShortcutManager

var shortcutModule = module {

    // App shortcuts
    single<AppShortcutManager> { AndroidAppShortcutManager(get()) }
}