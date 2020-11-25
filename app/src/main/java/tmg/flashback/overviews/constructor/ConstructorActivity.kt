package tmg.flashback.overviews.constructor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_constructor.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity
import tmg.flashback.overviews.constructor.summary.ConstructorSummaryAdapter
import tmg.flashback.overviews.driver.summary.DriverSummaryAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class ConstructorActivity: BaseActivity() {

    private val viewModel: ConstructorViewModel by viewModel()

    private lateinit var constructorId: String
    private lateinit var constructorName: String
    private lateinit var adapter: ConstructorSummaryAdapter

    override fun layoutId(): Int = R.layout.activity_constructor

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        constructorId = bundle.getString(keyConstructorId)!!
        constructorName = bundle.getString(keyConstructorName)!!

        viewModel.inputs.setup(constructorId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = constructorName

        adapter = ConstructorSummaryAdapter(
                openUrl = viewModel.inputs::openUrl,
                openSeason = viewModel.inputs::openSeason
        )
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        back.setOnClickListener {
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