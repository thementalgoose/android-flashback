package tmg.flashback.rss.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tmg.flashback.rss.databinding.ActivityRssBinding
import tmg.flashback.ui.base.BaseActivity

internal class RSSActivity: BaseActivity() {

    private lateinit var binding: ActivityRssBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRssBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        fun intent(context: Context): Intent {
            return Intent(context, RSSActivity::class.java)
        }
    }
}