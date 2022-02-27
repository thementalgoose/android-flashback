package tmg.flashback.regulations.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.threeten.bp.Year
import tmg.flashback.regulations.R
import tmg.flashback.regulations.databinding.ActivityFormatOverviewBinding
import tmg.flashback.ui.base.BaseActivity
import tmg.utilities.extensions.loadFragment

class FormatOverviewActivity: BaseActivity() {

    private lateinit var binding: ActivityFormatOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormatOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val season: Int = intent.extras?.getInt(keySeason) ?: Year.now().value

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.titleExpanded.text = getString(R.string.format_year_title, season)
        binding.titleCollapsed.text = getString(R.string.format_year_title, season)

        loadFragment(FormatOverviewFragment.instance(season), R.id.fragment)
    }

    companion object {

        private const val keySeason = "season"

        fun intent(context: Context, season: Int): Intent {
            return Intent(context, FormatOverviewActivity::class.java).apply {
                this.putExtra(keySeason, season)
            }
        }
    }
}