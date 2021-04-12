package tmg.flashback.statistics.ui.overview.constructor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.core.ui.BaseActivity
import tmg.flashback.core.utils.ScreenAnalytics
import tmg.flashback.statistics.databinding.ActivityConstructorBinding
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ConstructorActivity: BaseActivity() {

    private lateinit var binding: ActivityConstructorBinding
    private val viewModel: ConstructorViewModel by viewModel()

    override val screenAnalytics get() = ScreenAnalytics(
        screenName = "Constructor Overview",
        attributes = mapOf(
            "extra_constructor" to constructorId
        )
    )

    private lateinit var constructorId: String
    private lateinit var constructorName: String
    private lateinit var adapter: ConstructorSummaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstructorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.extras?.let {
            constructorId = it.getString(keyConstructorId)!!
            constructorName = it.getString(keyConstructorName)!!

            viewModel.inputs.setup(constructorId)
        }

        binding.header.text = constructorName

        adapter = ConstructorSummaryAdapter(
                openUrl = viewModel.inputs::openUrl,
                openSeason = viewModel.inputs::openSeason
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        observe(viewModel.outputs.list) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.openUrl) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }

        observeEvent(viewModel.outputs.openSeason) {

        }
    }

    companion object {
        private const val keyConstructorId: String = "constructorId"
        private const val keyConstructorName: String = "constructorName"

        fun intent(context: Context, constructorId: String, constructorName: String): Intent {
            val intent = Intent(context, ConstructorActivity::class.java)
            intent.putExtra(keyConstructorId, constructorId)
            intent.putExtra(keyConstructorName, constructorName)
            return intent
        }
    }
}