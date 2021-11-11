package tmg.flashback.statistics.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivitySearchBinding
import tmg.utilities.extensions.loadFragment

class SearchActivity: BaseActivity() {

    private lateinit var binding: ActivitySearchBinding

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(SearchFragment(), R.id.container)
    }
}