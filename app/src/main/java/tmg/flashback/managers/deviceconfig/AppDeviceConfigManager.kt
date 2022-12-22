package tmg.flashback.managers.deviceconfig

import tmg.flashback.device.managers.DeviceConfigManager
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.utilities.extensions.deviceStatus
import tmg.utilities.models.DeviceStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDeviceConfigManager @Inject constructor(): DeviceConfigManager {
    override fun getDeviceStatus(): DeviceStatus? {
        return null
    }
}