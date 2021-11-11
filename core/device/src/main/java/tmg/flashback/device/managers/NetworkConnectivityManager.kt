package tmg.flashback.device.managers

/**
 * Interface to determine network connectivity synchronously
 * Abstracted for testing
 */
interface NetworkConnectivityManager {
    val isConnected: Boolean
}