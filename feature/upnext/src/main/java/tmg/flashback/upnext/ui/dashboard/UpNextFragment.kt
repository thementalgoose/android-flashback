package tmg.flashback.upnext.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import tmg.core.ui.base.BaseFragment
import tmg.flashback.upnext.databinding.FragmentUpNextBinding

class UpNextFragment: BaseFragment<FragmentUpNextBinding>() {

    override fun inflateView(inflater: LayoutInflater) = FragmentUpNextBinding
        .inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}