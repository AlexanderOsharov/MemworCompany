package com.shurik.memwor_24.browser.fragments_all.view_pager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2

class CustomViewPager2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var viewPager2: ViewPager2

    init {
        viewPager2 = ViewPager2(context, attrs).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        addView(viewPager2)
    }

    fun getViewPager(): ViewPager2 {
        return viewPager2
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return viewPager2.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}

