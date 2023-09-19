package tmg.flashback.ui.permissions

import tmg.flashback.device.AppPermissions

interface RationaleBottomSheetFragmentCallback {
    fun rationaleConfirmClicked(rationaleType: List<tmg.flashback.device.AppPermissions.RuntimePermission>)
    fun rationaleCancelClicked(rationaleType: List<tmg.flashback.device.AppPermissions.RuntimePermission>)
}