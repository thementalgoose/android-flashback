package tmg.flashback.shared.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_shared_provided.view.*
import org.koin.experimental.property.inject
import tmg.components.about.AboutThisAppActivity
import tmg.flashback.R
import tmg.flashback.configuration
import tmg.flashback.firebase.config.FirebaseRemoteConfigRepository
import tmg.flashback.prefs.SharedPrefsRepository
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.enums.ThemePref
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString

class ProvidedByViewHolder(view: View): RecyclerView.ViewHolder(view) {

    // TODO: Look at injecting these dependencies properly
    private val remoteConfig: RemoteConfigRepository = FirebaseRemoteConfigRepository(null)
    private val sharedPrefs: SharedPrefsRepository = SharedPrefsRepository(context)

    init {
        view.provided_by.text = remoteConfig.dataProvidedBy?.fromHtml() ?: getString(R.string.shared_provided_by)
        view.container.setOnClickListener {
            val isLightMode = sharedPrefs.theme == ThemePref.DAY || (sharedPrefs.theme == ThemePref.AUTO && context.isInDayMode())
            context.startActivity(
                AboutThisAppActivity.intent(
                    context = context,
                    configuration = configuration(context, !isLightMode)
                )
            )
        }
    }
}