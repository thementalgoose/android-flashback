package tmg.flashback.managers.deviceconfig

import tmg.flashback.device.managers.DeviceConfigManager
import tmg.flashback.navigation.ActivityProvider
import tmg.utilities.models.DeviceStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDeviceConfigManager @Inject constructor(
    private val topActivityProvider: tmg.flashback.navigation.ActivityProvider
): DeviceConfigManager {
    override fun getDeviceStatus(): DeviceStatus? {
        return topActivityProvider.activity?.let {
            DeviceStatus(it, requestDeviceIMEI = false)
        }
    }
}