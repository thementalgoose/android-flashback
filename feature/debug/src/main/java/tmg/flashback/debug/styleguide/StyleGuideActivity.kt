package tmg.flashback.debug.styleguide

import android.os.Bundle
import tmg.flashback.debug.databinding.ActivityStyleGuideBinding
import tmg.flashback.ui.base.BaseActivity

class StyleGuideActivity: BaseActivity() {

    private lateinit var binding: ActivityStyleGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStyleGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}