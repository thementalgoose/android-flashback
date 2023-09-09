package tmg.flashback.ui.permissions

import tmg.flashback.ui.AppPermissions

interface RationaleBottomSheetFragmentCallback {
    fun rationaleConfirmClicked(rationaleType: List<AppPermissions.RuntimePermission>)
    fun rationaleCancelClicked(rationaleType: List<AppPermissions.RuntimePermission>)
}