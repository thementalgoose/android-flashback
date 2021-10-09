package tmg.core.device.managers

import android.content.Context
import android.net.NetworkCapabilities
import tmg.utilities.extensions.managerConnectivity

internal class AndroidNetworkConnectivityManager(
    val context: Context
) : NetworkConnectivityManager {
    override val isConnected: Boolean
        get() {
            val connectivityManager = context.managerConnectivity
            val networks = connectivityManager?.allNetworks ?: arrayOf()
            return networks.any {
                @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                connectivityManager?.getNetworkCapabilities(it)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: true
            }
        }
}