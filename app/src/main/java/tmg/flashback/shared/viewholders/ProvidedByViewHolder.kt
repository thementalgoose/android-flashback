package tmg.flashback.shared.viewholders

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_shared_provided.view.*
import tmg.components.about.AboutThisAppActivity
import tmg.flashback.R
import tmg.flashback.configuration
import tmg.flashback.home.HomeActivity
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString

class ProvidedByViewHolder(view: View): RecyclerView.ViewHolder(view) {

    init {
        view.provided_by.text = getString(R.string.shared_provided_by).fromHtml()
        view.container.setOnClickListener {
            context.startActivity(
                AboutThisAppActivity.intent(
                    context = context,
                    configuration = configuration(context, false)
                )
            )
        }
    }
}