package tmg.flashback.statistics.ui.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import tmg.flashback.statistics.R
import tmg.utilities.extensions.dpToPx
import tmg.utilities.extensions.getColor
import tmg.utilities.utils.ColorUtils.Companion.darken
import kotlin.math.roundToInt

class RaceNumberView: androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(attrs, defStyleAttr)
    }

    private val horizontalPadding: Int = 3f.dpToPx(context.resources).roundToInt()
    private val verticalPadding: Int = 2f.dpToPx(context.resources).roundToInt()

    @ColorInt
    var colorHighlight: Int = context.theme.getColor(R.attr.colorPrimary)
        set(value) {
            field = darken(value, factor = 0.8f)
            setShadowLayer(12f, 0f, 0f, field)
            invalidate()
        }

    private fun initView(attributeSet: AttributeSet?, defStyleAttr: Int = -1) {
        context.theme
            .obtainStyledAttributes(attributeSet, R.styleable.RaceNumberView, defStyleAttr, 0)
            .apply {
                try {
                    colorHighlight = getColor(R.styleable.RaceNumberView_boxColor, colorHighlight)
                } finally {
                    recycle()
                }
            }
        typeface = ResourcesCompat.getFont(context, R.font.closeness)
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        setTextColor(Color.WHITE)
    }
}