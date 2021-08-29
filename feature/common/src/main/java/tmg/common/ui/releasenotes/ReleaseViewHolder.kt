package tmg.common.ui.releasenotes

import androidx.recyclerview.widget.RecyclerView
import tmg.common.constants.ReleaseNotes
import tmg.common.databinding.ViewReleaseNoteBinding
import tmg.utilities.extensions.views.getString

class ReleaseViewHolder(
    private val binding: ViewReleaseNoteBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ReleaseNotes) {
        val title = getString(item.title)
        binding.version.text = "${item.versionName} $title"
        binding.content.setText(item.release)
    }
}