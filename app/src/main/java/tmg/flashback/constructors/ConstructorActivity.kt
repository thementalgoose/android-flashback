package tmg.flashback.constructors

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.WindowInsetsCompat
import kotlinx.android.synthetic.main.activity_constructor.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.R
import tmg.flashback.base.BaseActivity

class ConstructorActivity: BaseActivity() {

    private val viewModel: ConstructorViewModel by viewModel()

    private lateinit var constructorId: String
    private lateinit var constructorName: String

    override fun layoutId(): Int = R.layout.activity_constructor

    override fun arguments(bundle: Bundle) {
        super.arguments(bundle)
        constructorId = bundle.getString(keyConstructorId)!!
        constructorName = bundle.getString(keyConstructorName)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header.text = constructorName

        back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setInsets(insets: WindowInsetsCompat) {
        titlebar.setPadding(0, insets.systemWindowInsetTop, 0, 0)
        list.setPadding(0, 0, 0, insets.systemWindowInsetBottom)
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