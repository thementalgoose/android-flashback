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
import tmg.flashback.ads.controller.AdsController
import tmg.flashback.ads.databinding.AdmobNativeBannerBinding
import tmg.flashback.ads.utils.viewScope

class NativeBanner: FrameLayout, KoinComponent {

    private val adsController: AdsController by inject()
    private var binding: AdmobNativeBannerBinding? = null

    constructor(context: Context) : super(context) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    private fun initView() {

        if (adsController.areAdvertsEnabled) {
            val layoutInflater = LayoutInflater.from(context)
            binding = AdmobNativeBannerBinding.inflate(layoutInflater, this, false)
            binding?.let { binding ->
                addView(binding.root)
                binding.root.mediaView = binding.adMedia
                binding.root.headlineView = binding.adHeadline
                binding.root.iconView = binding.adAppIcon
                binding.root.bodyView = binding.adBody

                binding.adMedia.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (adsController.areAdvertsEnabled) {
            viewScope.launch {
                val ad = adsController.getAd(context)
                if (ad != null) {
                    binding?.let { binding ->
                        binding.root.setNativeAd(ad)
                        binding.adHeadline.text = ad.headline
                        binding.adBody.text = ad.body
                        binding.adAppIcon.setImageDrawable(ad.icon?.drawable)
                    }
                }
            }
        }
    }
}