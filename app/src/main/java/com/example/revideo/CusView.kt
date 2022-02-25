package com.example.revideo

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager2.widget.ViewPager2

class CusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewPager2(context, attrs) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
    }
}