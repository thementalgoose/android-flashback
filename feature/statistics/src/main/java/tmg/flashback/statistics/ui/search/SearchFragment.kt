package tmg.flashback.statistics.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import tmg.core.ui.base.BaseFragment
import tmg.flashback.statistics.databinding.FragmentSearchBinding

class SearchFragment: BaseFragment<FragmentSearchBinding>() {

    private lateinit var adapter: SearchAdapter

    override fun inflateView(inflater: LayoutInflater) = FragmentSearchBinding
        .inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logScreenViewed("Search")

        binding.swipeRefresh.isEnabled = false

        binding.back.setOnClickListener {
            activity?.finish()
        }

        adapter = SearchAdapter()
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        adapter.list = listOf<SearchItem>(
            SearchItem.Circuit("CIRCUIT ID"),
            SearchItem.Driver("Fdjsfsd ID2"),
            SearchItem.Race("CIRCUIT ID2"),
            SearchItem.Constructor("CIRCUIT ID2"),
            SearchItem.Driver("CIRCUIT ID2"),
            SearchItem.Circuit("CIRCUIT ID3"),
            SearchItem.Circuit("123 ID3"),
            SearchItem.Constructor("2313 ID3"),
            SearchItem.Circuit("sdfsdgdsg ID3"),
            SearchItem.Driver("CIRCUIT ID5"),
            SearchItem.Circuit("CIRCUIT ID7")
        )
    }
}