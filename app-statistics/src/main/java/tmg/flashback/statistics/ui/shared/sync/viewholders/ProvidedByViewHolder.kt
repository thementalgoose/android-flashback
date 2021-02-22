package tmg.flashback.statistics.ui.shared.sync.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_shared_provided.view.*
import tmg.flashback.core.enums.Theme
import tmg.flashback.core.extensions.isLightMode
import tmg.flashback.core.managers.AppSettingsManager
import tmg.flashback.statistics.R
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString

class ProvidedByViewHolder(
    private val appSettingsManager: AppSettingsManager,
    view: View
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    init {
        view.container.setOnClickListener(this)
    }

    fun bind(text: String?) {
        itemView.provided_by.text = text?.fromHtml() ?: getString(R.string.shared_provided_by)
        if (text != null) {
            Glide.with(itemView.icon)
                    .load(R.drawable.ergast)
                    .into(itemView.icon)
        }
    }

    override fun onClick(p0: View?) {
        appSettingsManager.openAboutThisApp(itemView.context)
    }
}