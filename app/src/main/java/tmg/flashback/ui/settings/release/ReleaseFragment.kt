package tmg.flashback.ui.settings.release

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import tmg.flashback.constants.Releases
import tmg.flashback.constants.ViewType
import tmg.flashback.shared.ui.base.BaseFragment
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.databinding.FragmentReleaseNotesBinding
import tmg.utilities.extensions.fromHtml

class ReleaseFragment: BaseFragment<FragmentReleaseNotesBinding>() {

    override val screenAnalytics = ScreenAnalytics(
        screenName = "Release Notes"
    )

    override fun inflateView(inflater: LayoutInflater) = FragmentReleaseNotesBinding
        .inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsController.logEvent(ViewType.SETTINGS_RELEASE_NOTES)

        val list = Releases
            .values()
            .sortedByDescending { it.version }
            .map { it.release }

        binding.back.setOnClickListener {
            activity?.finish()
        }

        binding.releaseNotes.text = list
            .joinToString("<br/><br/>") { getString(it) }
            .fromHtml()

    }
}