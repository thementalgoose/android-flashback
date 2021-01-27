package tmg.flashback.managers.analytics

interface UserPropertiesManager {

    fun setAppVersion(appVersion: String)
    fun setOsVersion(osVersion: String)
    fun setDeviceModel(model: String)
}