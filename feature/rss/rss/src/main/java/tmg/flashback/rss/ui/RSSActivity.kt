package tmg.flashback.rss.ui

import android.os.Bundle
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.rss.databinding.ActivityRssBinding

class RSSActivity: BaseActivity() {

    private lateinit var binding: ActivityRssBinding

    override val screenAnalytics: ScreenAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRssBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}