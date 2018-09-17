package com.skateboard.corronisonview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class CorronisonView(context: Context, attributeSet: AttributeSet?) : GLSurfaceView(context, attributeSet)
{

    private var time = 10

    private var render: CorronisonViewRender? = null

    private var animator: Animator? = null

    private var isSetRender = false

    init
    {
        if (attributeSet != null)
        {
            pareAttributeSet(attributeSet)
        }
        prepareTimeAnimator()
        setEGLContextClientVersion(3)
    }

    private fun pareAttributeSet(attributeSet: AttributeSet)
    {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CorronisonView)
        time = typedArray.getInteger(R.styleable.CorronisonView_duration, time)
        typedArray.recycle()
    }

    private fun prepareTimeAnimator()
    {
        val animator = ValueAnimator.ofInt(time)
        animator.duration = time.toLong() * 1000
        animator.addUpdateListener(ValueAnimator.AnimatorUpdateListener {
            render?.percent = it.animatedFraction
        })
        this.animator = animator
    }

    fun setBitmap(bitmap: Bitmap)
    {
        render = CorronisonViewRender(context, bitmap)
        isSetRender = true
        setRenderer(render)
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        animator?.start()
    }


    override fun onDetachedFromWindow()
    {
        super.onDetachedFromWindow()
        animator?.cancel()
        animator = null
    }

    override fun onResume()
    {
        if (isSetRender)
        {
            super.onResume()
        }
    }

    override fun onPause()
    {
        if (isSetRender)
        {
            super.onPause()

        }
    }
}