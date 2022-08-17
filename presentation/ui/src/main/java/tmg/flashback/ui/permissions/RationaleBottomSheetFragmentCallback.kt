package tmg.flashback.ui.permissions

interface RationaleBottomSheetFragmentCallback {
    fun rationaleConfirmClicked(rationaleType: RationaleType)
    fun rationaleCancelClicked(rationaleType: RationaleType)
}