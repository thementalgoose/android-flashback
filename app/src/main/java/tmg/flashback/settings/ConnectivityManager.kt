package tmg.flashback.settings

import android.content.Context
import android.net.NetworkCapabilities
import tmg.flashback.repo.db.CrashReporter
import tmg.utilities.extensions.managerConnectivity

interface ConnectivityManager {
    val isConnected: Boolean
}

class NetworkConnectivityManager(
    val context: Context
) : ConnectivityManager {
    override val isConnected: Boolean
        get() {
            val connectivityManager = context.managerConnectivity
            val networks = connectivityManager.allNetworks
            return networks.any {
                @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                connectivityManager.getNetworkCapabilities(it).hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
        }
}