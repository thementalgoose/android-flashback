package tmg.flashback.rss.ui

import android.os.Bundle
import tmg.flashback.rss.databinding.ActivityRssBinding
import tmg.core.ui.base.BaseActivity

class RSSActivity: BaseActivity() {

    private lateinit var binding: ActivityRssBinding

//    override val screenAnalytics: ScreenAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRssBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}