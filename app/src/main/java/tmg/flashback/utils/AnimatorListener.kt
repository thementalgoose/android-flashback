package tmg.flashback.utils

import android.animation.Animator

class AnimatorListener(
    val repeat: (animator: Animator?) -> Unit = { },
    val end: (animator: Animator?) -> Unit = { },
    val cancel: (animator: Animator?) -> Unit = { },
    val start: (animator: Animator?) -> Unit = { }
): Animator.AnimatorListener {
    override fun onAnimationRepeat(p0: Animator?) { repeat(p0) }
    override fun onAnimationEnd(p0: Animator?) { end(p0) }
    override fun onAnimationCancel(p0: Animator?) { cancel(p0) }
    override fun onAnimationStart(p0: Animator?) { start(p0) }
}