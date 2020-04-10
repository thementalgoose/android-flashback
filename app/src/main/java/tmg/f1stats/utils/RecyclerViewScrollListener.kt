package tmg.f1stats.utils

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onScroll(callback: (isUp: Boolean) -> Unit) {
    this.addOnScrollListener(RecyclerViewScrollListener(callback))
}

class RecyclerViewScrollListener(
    val callback: (isUp: Boolean) -> Unit
): RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy <= 0) {
            callback(true)
        }
        else if (dy > 0) {
            callback(false)
        }

        super.onScrolled(recyclerView, dx, dy)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }
}