package tmg.flashback.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.common.ui.forceupgrade.ForceUpgradeActivity
import tmg.core.ui.base.BaseActivity
import tmg.flashback.R
import tmg.flashback.databinding.ActivitySyncBinding
import tmg.flashback.databinding.ViewSyncItemBinding
import tmg.flashback.ui.dashboard.HomeActivity
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent
import tmg.utilities.extensions.setStatusBarColor
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.show
import tmg.utilities.extensions.views.visible

class SyncActivity: BaseActivity() {

    private val viewModel: SyncViewModel by viewModel()

    private lateinit var binding: ActivitySyncBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySyncBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor(ContextCompat.getColor(this, R.color.splash_screen))

        observe(viewModel.outputs.configState) {
            binding.setup.updateTo(it)
            when (it) {
                SyncState.FAILED -> binding.failedToSync.visible()
                SyncState.LOADING -> { }
                SyncState.DONE -> { }
            }
        }

        observe(viewModel.outputs.circuitsState) {
            binding.circuits.updateTo(it)
        }
        observe(viewModel.outputs.constructorsState) {
            binding.constructors.updateTo(it)
        }
        observe(viewModel.outputs.driversState) {
            binding.drivers.updateTo(it)
        }
        observe(viewModel.outputs.racesState) {
            binding.races.updateTo(it)
        }

        observe(viewModel.outputs.showContinue) {
            binding.proceed.isEnabled = it
        }

        observeEvent(viewModel.outputs.goToDashboard) {
            startActivity(Intent(this@SyncActivity, HomeActivity::class.java))
            finish()
        }

        observeEvent(viewModel.outputs.goToForceUpgrade) {
            startActivity(Intent(this@SyncActivity, ForceUpgradeActivity::class.java))
            finish()
        }


        binding.setup.label.text = getString(R.string.splash_sync_config)
        binding.circuits.label.text = getString(R.string.splash_sync_circuits)
        binding.constructors.label.text = getString(R.string.splash_sync_constructors)
        binding.drivers.label.text = getString(R.string.splash_sync_drivers)
        binding.races.label.text = getString(R.string.splash_sync_races)


        binding.setup.container.setOnClickListener {
            viewModel.inputs.startRemoteConfig()
        }
        binding.circuits.container.setOnClickListener {
            viewModel.inputs.startSyncCircuits()
        }
        binding.constructors.container.setOnClickListener {
            viewModel.inputs.startSyncConstructors()
        }
        binding.drivers.container.setOnClickListener {
            viewModel.inputs.startSyncDrivers()
        }
        binding.races.container.setOnClickListener {
            viewModel.inputs.startSyncRaces()
        }


        binding.proceed.setOnClickListener {
            viewModel.inputs.continueClicked()
        }


        viewModel.inputs.startRemoteConfig()
        viewModel.inputs.startSyncCircuits()
        viewModel.inputs.startSyncConstructors()
        viewModel.inputs.startSyncDrivers()
        viewModel.inputs.startSyncRaces()
    }

    private fun ViewSyncItemBinding.updateTo(state: SyncState) {
        when (state) {
            SyncState.LOADING -> {
                this.container.isClickable = false
                this.container.isEnabled = false
                this.loading.visible()
                this.done.invisible()
                this.failed.invisible()
            }
            SyncState.DONE -> {
                this.container.isClickable = false
                this.container.isEnabled = false
                this.loading.invisible()
                this.done.visible()
                this.failed.invisible()
            }
            SyncState.FAILED -> {
                this.container.isClickable = true
                this.container.isEnabled = true
                this.loading.invisible()
                this.done.invisible()
                this.failed.visible()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}