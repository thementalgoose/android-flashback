package tmg.flashback.upnext.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseFragment
import tmg.flashback.upnext.databinding.FragmentUpNextBinding
import tmg.utilities.extensions.observe

class UpNextFragment: BaseFragment<FragmentUpNextBinding>() {

    private val viewModel: UpNextViewModel by viewModel()

    private lateinit var upNextAdapter: UpNextBreakdownAdapter

    override fun inflateView(inflater: LayoutInflater) = FragmentUpNextBinding
        .inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upNextAdapter = UpNextBreakdownAdapter()
        binding.content.layoutManager = LinearLayoutManager(context)
        binding.content.adapter = upNextAdapter

        observe(viewModel.outputs.content) {
            upNextAdapter.list = it
        }
    }
}