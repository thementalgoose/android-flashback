package tmg.flashback.device.managers

import tmg.utilities.models.DeviceStatus

interface DeviceConfigManager {
    fun getDeviceStatus(): DeviceStatus?
}