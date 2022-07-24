package tmg.flashback.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgs
import androidx.navigation.NavType
import androidx.navigation.navArgument

fun navStringRequired(name: String): NamedNavArgument = navArgument(name = name) {
    this.type = NavType.StringType
    this.nullable = false
}
fun navString(name: String): NamedNavArgument = navArgument(name = name) {
    this.type = NavType.StringType
    this.nullable = true
}

fun navIntRequired(name: String): NamedNavArgument = navArgument(name = name) {
    this.type = NavType.IntType
    this.nullable = false
}
fun navInt(name: String): NamedNavArgument = navArgument(name = name) {
    this.type = NavType.IntType
    this.nullable = true
}