package interfaced.webview.library.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class SlideAnimation(var mView: View, toHeight: Int) : Animation() {
    var mFromHeight: Int
    var mToHeight: Int
    override fun applyTransformation(
        interpolatedTime: Float,
        transformation: Transformation
    ) {
        val newHeight: Int
        if (mView.height != mToHeight) {
            newHeight = (mFromHeight + (mToHeight - mFromHeight) * interpolatedTime).toInt()
            mView.layoutParams.height = newHeight
            mView.requestLayout()
        }
    }

    override fun willChangeBounds(): Boolean {
        return true
    }

    init {
        mFromHeight = mView.height
        mToHeight = toHeight
    }
}