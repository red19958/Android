package com.example.hw5_animation

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.getSize
import android.os.Parcel


class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var radius: Float = 50f
    private var infinite: Boolean = true
    //private var animationDuration: Float = 0f
    //private var startAnimationTime: Long = System.currentTimeMillis()
    private var ox: Float = 0f
    private var oy: Float = 0f
    private var ox1: Float = 0f
    private var oy1: Float = 0f
    private var ox2: Float = 0f
    private var oy2: Float = 0f
    private var a: Float = 0f
    private var b: Float = 0f
    private var t: Float = 0f


    private var paint0 = Paint().apply {
        color = 0xFFFF8000.toInt()
    }

    private var paint1 = Paint().apply {
        color = 0xFFC557C7.toInt()
    }

    private var paint2 = Paint().apply {
        color = 0xFF54E2F9.toInt()
    }

    init {
        isSaveEnabled = true
        val a: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, defStyleRes)
        try {
            radius = a.getDimension(R.styleable.CustomView_radius, 75f)
            infinite = a.getBoolean(R.styleable.CustomView_infinite, true)
            //animationDuration = a.getFloat(R.styleable.CustomView_animationDuration, 0f)
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.save()

        if (infinite /*|| System.currentTimeMillis() - startAnimationTime <= animationDuration*/) {
            t = ((System.currentTimeMillis() % 10000000)).toFloat() / 200
            ox = (a + 2 * radius * kotlin.math.sin(t.toDouble()*2)).toFloat()
            oy = (b + 2 * radius * kotlin.math.cos(t.toDouble()*2)).toFloat()
            ox1 = (a + 2 * radius * kotlin.math.sin((t-2).toDouble()/4)).toFloat()
            oy1 = (b + 2 * radius * kotlin.math.cos((t-2).toDouble()/4)).toFloat()
            ox2 = (a + 2 * radius * kotlin.math.sin((t-4).toDouble())).toFloat()
            oy2 = (b + 2 * radius * kotlin.math.cos((t-4).toDouble())).toFloat()
        }

        canvas?.drawCircle(ox, oy, radius, paint0)
        canvas?.drawCircle(ox1, oy1, radius, paint1)
        canvas?.drawCircle(ox2, oy2, radius, paint2)
        invalidate()
        canvas!!.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(getSize(widthMeasureSpec), getSize(heightMeasureSpec))
        a = measuredWidth.toFloat() / 2
        b = measuredHeight.toFloat() / 2
    }


    override fun onSaveInstanceState(): Parcelable {
        val state = CustomState(super.onSaveInstanceState())
        state.saveRadius = this.radius

        if (this.infinite) {
            state.saveInfinite = 1
        } else {
            state.saveInfinite = 0
        }

        //state.saveAnimationDuration = this.animationDuration
        //state.saveStartAnimationTime = this.startAnimationTime
        state.saveOx = this.ox
        state.saveOy = this.oy
        state.saveOx1 = this.ox1
        state.saveOy1 = this.oy1
        state.saveOx2 = this.ox2
        state.saveOy2 = this.oy2
        state.saveA = this.a
        state.saveB = this.b
        state.saveT = this.t
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state as CustomState
        super.onRestoreInstanceState(state.superState)
        this.radius = state.saveRadius
        this.infinite = (state.saveInfinite == 1)
        //this.animationDuration = state.saveAnimationDuration
        //this.startAnimationTime = state.saveStartAnimationTime
        this.ox = state.saveOx
        this.oy = state.saveOy
        this.ox1 = state.saveOx1
        this.oy1 = state.saveOy1
        this.ox2 = state.saveOx2
        this.oy2 = state.saveOy2
        this.a = state.saveA
        this.b = state.saveB
        this.t = state.saveT
    }

    private class CustomState : BaseSavedState {
        var saveRadius: Float = 75f
        var saveInfinite: Int = 1
        //var saveAnimationDuration: Float = 0f
        //var saveStartAnimationTime: Long = System.currentTimeMillis()
        var saveOx: Float = 0f
        var saveOy: Float = 0f
        var saveOx1: Float = 0f
        var saveOy1: Float = 0f
        var saveOx2: Float = 0f
        var saveOy2: Float = 0f
        var saveA: Float = 0f
        var saveB: Float = 0f
        var saveT: Float = 0f

        constructor(superState: Parcelable?) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            saveRadius = parcel.readFloat()
            saveInfinite = parcel.readInt()
            //saveAnimationDuration = parcel.readFloat()
            //saveStartAnimationTime = parcel.readLong()
            saveOx = parcel.readFloat()
            saveOy = parcel.readFloat()
            saveOx1 = parcel.readFloat()
            saveOy1 = parcel.readFloat()
            saveOx2 = parcel.readFloat()
            saveOy2 = parcel.readFloat()
            saveA = parcel.readFloat()
            saveB = parcel.readFloat()
            saveT = parcel.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(saveRadius)
            out.writeInt(saveInfinite)
            //out.writeFloat(saveAnimationDuration)
            //out.writeLong(saveStartAnimationTime)
            out.writeFloat(saveOx)
            out.writeFloat(saveOy)
            out.writeFloat(saveOx1)
            out.writeFloat(saveOy1)
            out.writeFloat(saveOx2)
            out.writeFloat(saveOy2)
            out.writeFloat(saveA)
            out.writeFloat(saveB)
            out.writeFloat(saveT)
        }

        companion object CREATOR : Parcelable.Creator<CustomState> {
            override fun createFromParcel(parcel: Parcel): CustomState = CustomState(parcel)
            override fun newArray(size: Int): Array<CustomState?> = arrayOfNulls(size)
        }
    }
}
