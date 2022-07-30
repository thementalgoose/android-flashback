package tmg.flashback.ads.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.ads.R
import tmg.flashback.ads.databinding.AdmobNativeBannerBinding
import tmg.flashback.ads.config.repository.AdsRepository
import tmg.flashback.ads.usecases.GetAdUseCase
import tmg.utilities.extensions.views.gone
import javax.inject.Inject

@AndroidEntryPoint
class NativeBanner: FrameLayout {

    @Inject
    protected lateinit var adsRepository: AdsRepository
    @Inject
    protected lateinit var getAdUseCase: GetAdUseCase

    private var binding: AdmobNativeBannerBinding? = null

    var adIndex: Int = 0
    var offsetCard: Boolean = true

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

    private fun initView(attributeSet: AttributeSet?, defStyleAttr: Int? = -1) {

        context.theme
            .obtainStyledAttributes(attributeSet, R.styleable.NativeBanner, defStyleAttr ?: -1, 0)
            .apply {
                try {
                    adIndex = getInt(R.styleable.NativeBanner_adIndex, adIndex)
                } finally {
                    recycle()
                }
            }

        if (adsRepository.areAdvertsEnabled) {
            val layoutInflater = LayoutInflater.from(context)
            binding = AdmobNativeBannerBinding.inflate(layoutInflater, this, false)
            binding?.let { binding ->
                addView(binding.root, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                ))
//                binding.skeleton.alpha = 0.1f
//                binding.skeleton.showSkeleton()
                binding.adView.mediaView = binding.adMedia
                binding.adView.headlineView = binding.adHeadline

                binding.adView.iconView = binding.adAppIcon
                binding.adView.bodyView = binding.adBody
                binding.adMedia.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

                if (!offsetCard) {
                    binding.adSpacer.gone()
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (adsRepository.areAdvertsEnabled) {

            findViewTreeLifecycleOwner()?.lifecycleScope?.launchWhenStarted {
                val ad = getAdUseCase.getAd(context, adIndex)
                if (ad != null) {
                    binding?.let { binding ->
//                        binding.skeleton.alpha = 1.0f
//                        binding.skeleton.showOriginal()
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