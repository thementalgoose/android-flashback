package tmg.flashback.device.managers

import android.content.Context
import android.net.NetworkCapabilities
import android.util.Log
import tmg.flashback.device.BuildConfig
import tmg.utilities.extensions.managerConnectivity

internal class AndroidNetworkConnectivityManager(
    val context: Context
) : NetworkConnectivityManager {
    override val isConnected: Boolean
        get() {
            val connectivityManager = context.managerConnectivity
            val network = connectivityManager?.activeNetwork
            if (network == null) {
                log("isConnect=false (NO ACTIVE NETWORK)")
                return false
            }
            val actNetwork = connectivityManager.getNetworkCapabilities(network)
            if (actNetwork == null) {
                log("isConnect=false (NO CAPABILITIES)")
                return false
            }
            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> { // check if wifi is connected
                    log("isConnect=true (WIFI)")
                    true
                }
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> { // check if mobile dats is connected
                    log("isConnect=true (CELLULAR)")
                    true
                }
                actNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> {
                    log("isConnect=true (INTERNET)")
                    true
                }
                else -> {
                    log("isConnect=false")
                    false
                }
            }
        }

    private fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d("Network", msg)
        }
    }
}