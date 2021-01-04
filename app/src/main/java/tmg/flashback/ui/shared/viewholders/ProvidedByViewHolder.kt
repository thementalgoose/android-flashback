package tmg.flashback.ui.shared.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_shared_provided.view.*
import tmg.components.about.AboutThisAppActivity
import tmg.flashback.R
import tmg.flashback.constants.AboutThisAppConfig
import tmg.flashback.extensions.isLightMode
import tmg.flashback.repo.enums.ThemePref
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString

class ProvidedByViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private var isLightMode: Boolean = false

    init {
        view.container.setOnClickListener(this)
    }

    fun bind(text: String?, theme: ThemePref) {
        isLightMode = theme.isLightMode(context)
        itemView.provided_by.text = text?.fromHtml() ?: getString(R.string.shared_provided_by)
        if (text != null) {
            Glide.with(itemView.icon)
                    .load(R.drawable.ergast)
                    .into(itemView.icon)
        }
    }

    override fun onClick(p0: View?) {
        context.startActivity(AboutThisAppActivity.intent(
                context = context,
                configuration = AboutThisAppConfig.configuration(context, !isLightMode)
        ))
    }
}