package com.example.hangmangame;

import android.app.AlertDialog
import android.graphics.Canvas
import android.view.View

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle
import android.util.AttributeSet;
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView

class HangmanView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var wrongGuesses: Int = 0
    private val paint: Paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawLine(100f, 600f, 400f, 600f, paint) // 1
            it.drawLine(250f, 600f, 250f, 200f, paint) // 2
            it.drawLine(250f, 200f, 450f, 200f, paint) // 3
            it.drawLine(450f, 200f, 450f, 300f, paint) // 4

            if (wrongGuesses >= 1) {
                it.drawCircle(450f, 350f, 50f, paint)
            }
            if (wrongGuesses >= 2) {
                it.drawLine(450f, 400f, 450f, 500f, paint)
            }
            if (wrongGuesses >= 3) {
                it.drawLine(450f, 420f, 400f, 480f, paint)
            }
            if (wrongGuesses >= 4) {
                it.drawLine(450f, 420f, 500f, 480f, paint)
            }
            if (wrongGuesses >= 5) {
                it.drawLine(450f, 500f, 400f, 580f, paint)
            }
            if (wrongGuesses >= 6) {
                it.drawLine(450f, 500f, 500f, 580f, paint) 
            }
        }
    }


    fun updateWrongGuesses(guesses: Int) {
        wrongGuesses = guesses
        invalidate()
    }
}
