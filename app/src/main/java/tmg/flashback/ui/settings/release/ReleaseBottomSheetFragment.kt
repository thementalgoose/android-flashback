package tmg.flashback.ui.settings.release

import org.koin.android.ext.android.inject
import tmg.flashback.R
import tmg.flashback.repo.pref.DeviceRepository
import tmg.flashback.ui.base.BaseBottomSheetFragment

class ReleaseBottomSheetFragment: BaseBottomSheetFragment() {

    override fun layoutId(): Int = R.layout.fragment_release_notes

    override fun initViews() {

//        val list = releaseNotes
//            .filterKeys { it > prefsDeviceRepository.lastAppVersion }
//            .toList()
//            .reversed()
//            .sortedBy { it.first }
//            .map { it.second }
//
//        tvReleaseNotesDescription.text = list.map { getString(it) }.joinToString("<br/><br/>").fromHtml()

//        prefsDeviceRepository.lastAppVersion = BuildConfig.VERSION_CODE
    }
}