package tmg.flashback.managers.networkconnectivity

import android.content.Context
import android.net.NetworkCapabilities
import tmg.flashback.core.managers.NetworkConnectivityManager
import tmg.utilities.extensions.managerConnectivity

class AndroidNetworkConnectivityManager(
    val context: Context
) : NetworkConnectivityManager {
    override val isConnected: Boolean
        get() {
            val connectivityManager = context.managerConnectivity
            val networks = connectivityManager.allNetworks
            return networks.any {
                @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                connectivityManager.getNetworkCapabilities(it)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: true
            }
        }
}