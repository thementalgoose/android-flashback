package tmg.common.ui.releasenotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import tmg.common.constants.Releases
import tmg.common.databinding.FragmentReleaseNotesBinding
import tmg.core.ui.base.BaseFragment
import tmg.utilities.extensions.fromHtml

class ReleaseFragment: BaseFragment<FragmentReleaseNotesBinding>() {

    override fun inflateView(inflater: LayoutInflater) = FragmentReleaseNotesBinding
        .inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Release notes")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = Releases
            .values()
            .sortedByDescending { it.version }
            .map { it.release }

        binding.back.setOnClickListener {
            activity?.finish()
        }

        // TODO: Move this to a view model!
        binding.releaseNotes.text = list
            .joinToString("<br/><br/>") { getString(it) }
            .fromHtml()

    }
}