package tmg.flashback.ui.animation

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import tmg.flashback.ui.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlideProvider @Inject constructor() {

    fun load(
        imageView: ImageView,
        img: String?,
        @DrawableRes
        error: Int? = R.drawable.unknown_avatar // https://www.svgrepo.com/svg/45791/racing-helmet
    ) {
        Glide.with(imageView.context)
            .load(img)
            .placeholder(shimmerDrawable)
            .error(error)
            .into(imageView)
    }

    fun clear(imageView: ImageView) {
        Glide.with(imageView.context)
            .clear(imageView)
    }

    private val shimmer =
        Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(500) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.7f) //the alpha of the underlying children
            .setHighlightAlpha(0.6f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

    private val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }
}