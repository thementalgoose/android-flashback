package tmg.flashback.common.ui.releasenotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import tmg.flashback.common.constants.ReleaseNotes
import tmg.flashback.common.databinding.FragmentReleaseNotesBinding
import tmg.flashback.ui.base.BaseFragment
import tmg.utilities.lifecycle.viewInflateBinding

class ReleaseFragment: BaseFragment() {

    private lateinit var adapter: ReleaseAdapter
    private val binding by viewInflateBinding(FragmentReleaseNotesBinding::inflate)

    override fun onCreateView() = binding.root

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