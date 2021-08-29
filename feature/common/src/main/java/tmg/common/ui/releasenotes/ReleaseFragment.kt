package tmg.common.ui.releasenotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import tmg.common.constants.ReleaseNotes
import tmg.common.databinding.FragmentReleaseNotesBinding
import tmg.core.ui.base.BaseFragment
import tmg.utilities.extensions.fromHtml

class ReleaseFragment: BaseFragment<FragmentReleaseNotesBinding>() {

    private lateinit var adapter: ReleaseAdapter

    override fun inflateView(inflater: LayoutInflater) = FragmentReleaseNotesBinding
        .inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logScreenViewed("Release notes")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ReleaseNotes
            .values()
            .sortedByDescending { it.version }

        adapter = ReleaseAdapter()
        binding.dataList.layoutManager = LinearLayoutManager(context)
        binding.dataList.adapter = adapter

        // TODO: Move this to a view model
        adapter.list = list

        binding.back.setOnClickListener {
            activity?.finish()
        }

    }
}