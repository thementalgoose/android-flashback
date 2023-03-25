package tmg.flashback.drivers.ui.stathistory

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment
import tmg.utilities.extensions.toEnum

@AndroidEntryPoint
class DriverStatHistoryBottomSheetFragment: BaseBottomSheetComposeFragment() {

    private val driverId: String by lazy { arguments?.getString(keyDriverId)!! }
    private val driverName: String by lazy { arguments?.getString(keyDriverName)!! }
    private val driverStatHistoryType: DriverStatHistoryType by lazy {
        arguments?.getInt(keyDriverStatHistoryType)?.toEnum<DriverStatHistoryType>() ?: DriverStatHistoryType.CHAMPIONSHIPS
    }

    override val content = @Composable {
        DriverStatHistoryScreenVM(
            driverId = driverId,
            driverName = driverName,
            driverStatHistoryType = driverStatHistoryType,
            actionUpClicked = {
                dismissAllowingStateLoss()
            }
        )
    }

    companion object {

        private const val keyDriverId: String = "driverId"
        private const val keyDriverName: String = "driverName"
        private const val keyDriverStatHistoryType = "driverStatHistoryType"

        fun instance(
            driverId: String,
            driverName: String,
            driverStatHistoryType: DriverStatHistoryType
        ) = DriverStatHistoryBottomSheetFragment().apply {
            arguments = bundleOf(
                keyDriverId to driverId,
                keyDriverName to driverName,
                keyDriverStatHistoryType to driverStatHistoryType.ordinal
            )
        }
    }
}