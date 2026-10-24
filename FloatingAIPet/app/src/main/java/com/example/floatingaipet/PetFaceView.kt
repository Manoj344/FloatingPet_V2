package com.example.floatingaipet

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.sin

class PetFaceView(context: Context) : View(context) {
    private enum class Mood { HAPPY, EXCITED, NORMAL, WORRIED, SAD, SLEEPY, CHARGING, FULL }
    private var mood = Mood.HAPPY
    private var battery = 100
    private var charging = false
    private var blink = 1f
    private var phase = 0f

    private val shell = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(220, 235, 255)
        style = Paint.Style.FILL
    }
    private val screen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(5, 10, 25)
        style = Paint.Style.FILL
    }
    private val glow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(0, 205, 255)
        style = Paint.Style.STROKE
        strokeWidth = 3.5f
    }
    private val eye = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(20, 215, 255)
        style = Paint.Style.STROKE
        strokeWidth = 5f
        strokeCap = Paint.Cap.ROUND
    }
    private val mouth = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(20, 215, 255)
        style = Paint.Style.STROKE
        strokeWidth = 4.5f
        strokeCap = Paint.Cap.ROUND
    }
    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1800
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            phase = it.animatedValue as Float
            invalidate()
        }
    }

    init {
        animator.start()
        contentDescription = "Floating AI pet face. Tap to change expression."
    }

    fun cycleExpression() {
        mood = when (mood) {
            Mood.HAPPY -> Mood.EXCITED
            Mood.EXCITED -> Mood.SLEEPY
            else -> Mood.HAPPY
        }
        invalidate()
    }

    fun setCharging(value: Boolean) {
        charging = value
        updateMood()
    }

    fun setBattery(percent: Int, isCharging: Boolean, isFull: Boolean) {
        battery = percent.coerceIn(0, 100)
        charging = isCharging
        mood = when {
            isFull || battery >= 100 -> Mood.FULL
            charging -> Mood.CHARGING
            battery <= 10 -> Mood.SAD
            battery <= 20 -> Mood.WORRIED
            battery <= 39 -> Mood.WORRIED
            else -> Mood.NORMAL
        }
        invalidate()
    }

    private fun updateMood() {
        mood = when {
            battery >= 100 -> Mood.FULL
            charging -> Mood.CHARGING
            battery <= 20 -> Mood.WORRIED
            else -> Mood.NORMAL
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        val pulse = 1f + 0.018f * sin(phase * Math.PI * 2).toFloat()
        canvas.save()
        canvas.scale(pulse, pulse, w / 2, h / 2)

        canvas.drawRoundRect(RectF(w * .035f, h * .12f, w * .965f, h * .90f),
            h * .25f, h * .25f, shell)
        canvas.drawRoundRect(RectF(w * .09f, h * .18f, w * .91f, h * .84f),
            h * .20f, h * .20f, screen)
        canvas.drawRoundRect(RectF(w * .09f, h * .18f, w * .91f, h * .84f),
            h * .20f, h * .20f, glow)

        val eyeY = h * .48f
        val leftX = w * .34f
        val rightX = w * .66f
        val eyeHeight = when (mood) {
            Mood.SLEEPY, Mood.SAD -> h * .018f
            else -> h * .09f
        }
        val eyePaint = eye
        eyePaint.strokeWidth = h * .045f
        if (mood == Mood.SLEEPY || mood == Mood.SAD) {
            canvas.drawLine(leftX - w*.09f, eyeY, leftX + w*.09f, eyeY, eyePaint)
            canvas.drawLine(rightX - w*.09f, eyeY, rightX + w*.09f, eyeY, eyePaint)
        } else {
            canvas.drawCircle(leftX, eyeY, eyeHeight, eyePaint)
            canvas.drawCircle(rightX, eyeY, eyeHeight, eyePaint)
            val shine = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE; style = Paint.Style.FILL }
            canvas.drawCircle(leftX + w*.025f, eyeY - h*.025f, h*.018f, shine)
            canvas.drawCircle(rightX + w*.025f, eyeY - h*.025f, h*.018f, shine)
        }

        mouth.strokeWidth = h * .035f
        val path = android.graphics.Path()
        when (mood) {
            Mood.SAD, Mood.WORRIED -> {
                path.moveTo(w*.43f, h*.70f); path.quadTo(w*.50f, h*.63f, w*.57f, h*.70f)
            }
            Mood.SLEEPY -> {
                path.moveTo(w*.45f, h*.70f); path.lineTo(w*.55f, h*.70f)
            }
            Mood.EXCITED, Mood.FULL -> {
                path.moveTo(w*.42f, h*.65f); path.quadTo(w*.50f, h*.80f, w*.58f, h*.65f)
            }
            else -> {
                path.moveTo(w*.43f, h*.67f); path.quadTo(w*.50f, h*.75f, w*.57f, h*.67f)
            }
        }
        canvas.drawPath(path, mouth)

        if (charging || mood == Mood.CHARGING || mood == Mood.FULL) {
            val bolt = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(255, 210, 60); textSize = h*.18f; textAlign = Paint.Align.CENTER
            }
            canvas.drawText(if (mood == Mood.FULL) "★" else "⚡", w*.5f, h*.31f, bolt)
        } else if (mood == Mood.SAD || mood == Mood.WORRIED) {
            val warning = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(255, 180, 60); textSize = h*.13f; textAlign = Paint.Align.CENTER
            }
            canvas.drawText("$battery%", w*.5f, h*.31f, warning)
        }
        canvas.restore()
    }

    override fun onDetachedFromWindow() {
        animator.cancel()
        super.onDetachedFromWindow()
    }
}
