package com.example.alloybt.viewpager.graph_param

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.alloybt.R

class GraphDcMode(
    private val imageView: ImageView,
    width: Int,
    height: Int,
    private val context: View.OnTouchListener,
    bgd: Resources,
    private var paramList: MutableMap<Int,Params>
) {
    private val mWidth = width.toFloat()
    private val mHeight = height.toFloat()
    var imageBgd: Bitmap = BitmapFactory.decodeResource(bgd, R.drawable.block)

    // private  var   bitmap =  imageBgd.copy(Bitmap.Config.ARGB_8888, true)
    private var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    private val canvas = Canvas(bitmap)
    private val paint = Paint()

    @SuppressLint("ClickableViewAccessibility")
    fun setPaint() {

        paint.color = Color.RED
        paint.strokeWidth = 10F
        paint.textSize = 100.0F
        imageView.setImageBitmap(bitmap)
        canvas.drawLine(0.0F, 0.0F, mWidth, 0.0F, paint)
        canvas.drawLine(mWidth, 0.0F, mWidth, mHeight, paint)
        canvas.drawLine(mWidth, mHeight, 0.0F, mHeight, paint)
        canvas.drawLine(0.0F, mHeight, 0.0F, 0.0F, paint)
        imageView.setOnTouchListener(context)
        imageView.postInvalidate()

    }

    fun move(x: Float, y: Float) {

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //  canvas.drawBitmap(imageBgd,0.0F,0.0F,paint)

        paint.color = Color.RED
        canvas.drawLine(0.0F, mHeight, x, y, paint)
        canvas.drawLine(mWidth, mHeight, x, y, paint)
        imageView.postInvalidate()
        paint.color = Color.YELLOW
        canvas.drawText("text", x, y, paint)
        Log.d("tag", "move")
        imageView.postInvalidate()
    }

    fun setSettings(set1: Int, set2: Int, maxSet: Int) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.color = Color.RED
        val start1 = mWidth / 5
        val start2 = mWidth * 3 / 5
        val setWidth = mWidth / 5
        val setHeight = mHeight / maxSet

        canvas.drawLine(start1, mHeight, start1, set1 * setHeight, paint)
        canvas.drawLine(start1 + setWidth, mHeight, start1 + setWidth, set1 * setHeight, paint)
        canvas.drawLine(start1, set1 * setHeight, start1 + setWidth, set1 * setHeight, paint)

        canvas.drawLine(start2, mHeight, start2, set2 * setHeight, paint)
        canvas.drawLine(start2 + setWidth, mHeight, start2 + setWidth, set2 * setHeight, paint)
        canvas.drawLine(start2, set2 * setHeight, start2 + setWidth, set2 * setHeight, paint)

        imageView.postInvalidate()

    }

    fun setParams(maxSet: Int) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.color = Color.YELLOW
        val count = paramList.size + 2
        val setWidth = mWidth / count
        val setHeight = mHeight / maxSet


        (1 until count - 1).forEach {
            val value = (paramList[it]?.value ?: 0)
            val start = it * setWidth
            canvas.drawLine(start, mHeight, start, value * setHeight, paint)
            canvas.drawLine(start + setWidth, mHeight, start + setWidth, value * setHeight, paint)
            canvas.drawLine(start, value * setHeight, start + setWidth, value * setHeight, paint)
            canvas.drawCircle(start, value * setHeight, 5f, paint)
            canvas.drawCircle(start + setWidth, value * setHeight, 5f, paint)

            val matrix = Matrix()
            matrix.postScale(
                setWidth * 2 / imageBgd.width,
                (mHeight * 2 - value * setHeight * 2) / imageBgd.height
            )
            matrix.postRotate(0F)
            bitmap = Bitmap.createBitmap(
                imageBgd,
                0,
                0,
                imageBgd.width / 2,
                imageBgd.height / 2,
                matrix,
                true
            )
            canvas.drawBitmap(bitmap, start, value * setHeight, paint)
            //  canvas.drawBitmap(imageBgd,0.0F,0.0F,paint)

            imageView.postInvalidate(start.toInt(), mHeight.toInt(), (start + setWidth).toInt(), 0)
            imageView.postInvalidate(
                start.toInt() + 100,
                mHeight.toInt() + 100,
                (start + setWidth).toInt(),
                0
            )
        }


    }
}