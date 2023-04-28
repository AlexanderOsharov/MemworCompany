package com.shurik.memwor_24.browser

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height

        when {
            position < -1 -> {
                view.alpha = 0f // page is not visible
            }
            position <= 1 -> {
                val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                val vertMargin = pageHeight * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2
                if (position < 0) {
                    view.translationX = horzMargin - vertMargin / 2
                } else {
                    view.translationX = -horzMargin + vertMargin / 2
                }

                view.scaleX = scaleFactor
                view.scaleY = scaleFactor

                view.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            }
            else -> { // position > 1
                view.alpha = 0f // page is not visible
            }
        }
    }
}
