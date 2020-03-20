package tmg.f1stats.settings.sync

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_settings_sync.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.threeten.bp.Year
import tmg.f1stats.R
import tmg.f1stats.base.BaseActivity
import tmg.f1stats.repo.enums.SyncType
import tmg.utilities.extensions.click
import tmg.utilities.extensions.subscribeNoError

class SettingsSyncActivity: BaseActivity() {

    private lateinit var adapter: SettingsSyncAdapter

    private val viewModel: SettingsSyncViewModel by viewModel()

    override fun layoutId(): Int = R.layout.activity_settings_sync

    override fun initViews() {
        super.initViews()
        adapter = SettingsSyncAdapter()
        rvSync.adapter = adapter
        rvSync.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun observeViewModel() {

        btnTypeOverview
            .click()
            .subscribeNoError {
                etSeason.text.toString().toIntOrNull()?.let {
                    viewModel.inputs.addSyncItem(it, SyncType.SEASON_OVERVIEW)
                }
            }
            .autoDispose()

        btnTypeLaps
            .click()
            .subscribeNoError {
                etSeason.text.toString().toIntOrNull()?.let {
                    viewModel.inputs.addSyncItem(it, SyncType.LAP)
                }
            }
            .autoDispose()

        viewModel.outputs
            .itemAdded()
            .subscribeNoError {
                Toast.makeText(applicationContext, "Item added", Toast.LENGTH_LONG).show()
            }
            .autoDispose()

        viewModel.outputs
            .results()
            .subscribeNoError {
                adapter.replaceAll(it)
            }
            .autoDispose()

    }
}