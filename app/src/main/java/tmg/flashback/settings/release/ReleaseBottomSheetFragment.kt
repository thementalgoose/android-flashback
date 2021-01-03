package tmg.flashback.settings.release

import kotlinx.android.synthetic.main.fragment_release_notes.*
import org.koin.android.ext.android.inject
import tmg.flashback.BuildConfig
import tmg.flashback.R
import tmg.flashback.base.BaseBottomSheetFragment
import tmg.flashback.releaseNotes
import tmg.flashback.repo.pref.DeviceRepository
import tmg.utilities.extensions.fromHtml

class ReleaseBottomSheetFragment: BaseBottomSheetFragment() {

    private val prefsDeviceRepository: DeviceRepository by inject()

    override fun layoutId(): Int = R.layout.fragment_release_notes

    override fun initViews() {

        val list = releaseNotes
            .filterKeys { it > prefsDeviceRepository.lastAppVersion }
            .toList()
            .reversed()
            .sortedBy { it.first }
            .map { it.second }

        tvReleaseNotesDescription.text = list.map { getString(it) }.joinToString("<br/><br/>").fromHtml()

        prefsDeviceRepository.lastAppVersion = BuildConfig.VERSION_CODE
    }
}