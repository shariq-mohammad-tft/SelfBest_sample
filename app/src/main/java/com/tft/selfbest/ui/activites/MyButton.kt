package com.tft.selfbest.ui.activites

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.Log

class MyButton(private val context: Context, private val text: String,
                private val textSize: Int, private val color: Int,
                private val listener: MyButtonListener) {
    private var pos:Int = 0
    private var clickRegion: RectF?= null
    private val resources: Resources = context.resources

    fun onClick(x:Float, y: Float):Boolean{
        if(clickRegion != null && clickRegion!!.contains(x, y)) {
            listener.onClick(pos)
            return true
        }
        return false
    }

    fun onDraw(c: Canvas, rectF: RectF, pos:Int){
        val p= Paint()
        p.color=color
        c.drawRoundRect(rectF, 10f, 10f, p)

        p.color= Color.WHITE
        p.textSize=textSize.toFloat()

        val r = Rect()
        val cHeight = rectF.height()
        val cWidth = rectF.width()
        //Log.e("Swipe", " Rec Width $cWidth Height $cHeight")
        //Log.e("Swipe", " Rec Width ${r.width()} Height ${r.height()}")
        p.textAlign = Paint.Align.LEFT
        p.getTextBounds(text, 0, text.length, r)
        val x = cWidth/2f - r.width()/2f - r.left.toFloat()
        val y = cHeight/2f + r.height()/2f - r.bottom.toFloat()
        c.drawText(text, rectF.left+x, rectF.top+y, p)
        Log.e("Swipe", " Test 2")
        clickRegion = rectF
        this.pos = pos
    }


}