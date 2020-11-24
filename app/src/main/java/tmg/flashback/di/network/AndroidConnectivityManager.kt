package tmg.flashback.di.network

import android.content.Context
import android.net.NetworkCapabilities
import tmg.flashback.repo.NetworkConnectivityManager
import tmg.utilities.extensions.managerConnectivity

class AndroidConnectivityManager(
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