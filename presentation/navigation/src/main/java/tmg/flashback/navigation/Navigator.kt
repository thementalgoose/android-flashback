package tmg.flashback.navigation

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import tmg.flashback.crash_reporting.manager.CrashManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor(
    private val crashManager: CrashManager
) {

    lateinit var navController: NavHostController

    fun navigate(destination: NavigationDestination) {
        crashManager.log("Navigating to ${destination.route}")
        navController.navigate(destination)
    }
}