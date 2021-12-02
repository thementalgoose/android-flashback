package tmg.flashback.ads.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.experimental.property.inject
import tmg.flashback.ads.R
import tmg.flashback.ads.controller.AdsController
import tmg.flashback.ads.databinding.AdmobNativeBannerBinding
import tmg.flashback.ads.utils.viewScope
import tmg.utilities.extensions.views.gone

class NativeBanner: FrameLayout, KoinComponent {

    private val adsController: AdsController by inject()

    private var binding: AdmobNativeBannerBinding? = null

    private var alignIcon: Boolean = true
    private var adIndex: Int = 0

    constructor(context: Context) : super(context) {
        initView(null, null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs, null)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs, defStyleAttr)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(attrs, defStyleAttr)
    }


    private fun initView(attributeSet: AttributeSet?, defStyleAttr: Int? = -1) {

        context.theme
            .obtainStyledAttributes(attributeSet, R.styleable.NativeBanner, defStyleAttr ?: -1, 0)
            .apply {
                try {
                    alignIcon = getBoolean(R.styleable.NativeBanner_alignIcon, alignIcon)
                    adIndex = getInt(R.styleable.NativeBanner_adIndex, adIndex)
                } finally {
                    recycle()
                }
            }

        if (adsController.areAdvertsEnabled) {
            val layoutInflater = LayoutInflater.from(context)
            binding = AdmobNativeBannerBinding.inflate(layoutInflater, this, false)
            binding?.let { binding ->
                addView(binding.root)
                binding.skeleton.alpha = 0.1f
                binding.skeleton.showSkeleton()
                binding.adView.mediaView = binding.adMedia
                binding.adView.headlineView = binding.adHeadline

                binding.adView.iconView = binding.adAppIcon
                binding.adView.bodyView = binding.adBody
                binding.adMedia.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

                if (!alignIcon) {
                    binding.spacer.gone()
                    binding.adSpacer.gone()
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (adsController.areAdvertsEnabled) {
            viewScope.launch {
                val ad = adsController.getAd(context, adIndex)
                if (ad != null) {
                    binding?.let { binding ->
                        binding.skeleton.alpha = 1.0f
                        binding.skeleton.showOriginal()
                        binding.adView.setNativeAd(ad)
                        binding.adHeadline.text = ad.headline
                        binding.adBody.text = ad.body
                        binding.adAppIcon.setImageDrawable(ad.icon?.drawable)
                    }
                }
            }
        }
    }
}