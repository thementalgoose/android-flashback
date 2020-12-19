package tmg.flashback.shared.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_shared_provided.view.*
import org.koin.experimental.property.inject
import tmg.components.about.AboutThisAppActivity
import tmg.flashback.R
import tmg.flashback.configuration
import tmg.flashback.extensions.isLightMode
import tmg.flashback.firebase.config.FirebaseRemoteConfigRepository
import tmg.flashback.prefs.SharedPrefsRepository
import tmg.flashback.repo.config.RemoteConfigRepository
import tmg.flashback.repo.enums.ThemePref
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.isInDayMode
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString

class ProvidedByViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private var isLightMode: Boolean = false

    fun bind(text: String?, theme: ThemePref) {
        isLightMode = theme.isLightMode(context)
        itemView.provided_by.text = text?.fromHtml() ?: getString(R.string.shared_provided_by)
    }

    override fun onClick(p0: View?) {
        context.startActivity(AboutThisAppActivity.intent(
                context = context,
                configuration = configuration(context, !isLightMode)
        ))
    }
}