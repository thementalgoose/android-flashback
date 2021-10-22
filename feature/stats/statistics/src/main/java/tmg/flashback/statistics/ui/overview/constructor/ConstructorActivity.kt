package tmg.flashback.statistics.ui.overview.constructor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ActivityConstructorBinding
import tmg.utilities.extensions.loadFragment

class ConstructorActivity: BaseActivity() {

    private lateinit var binding: ActivityConstructorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstructorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lateinit var constructorName: String
        lateinit var constructorId: String

        intent?.extras?.let {
            constructorId = it.getString(keyConstructorId)!!
            constructorName = it.getString(keyConstructorName)!!
        }

        loadFragment(ConstructorFragment.instance(constructorId, constructorName), R.id.container)
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