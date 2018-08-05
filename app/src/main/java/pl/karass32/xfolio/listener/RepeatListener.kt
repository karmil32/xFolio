package pl.karass32.xfolio.listener

import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnTouchListener

open class RepeatListener(private var initialInterval: Long, private var normalInterval: Long, private var clickListener: OnClickListener) : OnTouchListener {

    private val handler = Handler()
    private var touchedView: View? = null

    private val handlerRunnable = object : Runnable {
        override fun run() {
            if (touchedView?.isEnabled!!) {
                handler.postDelayed(this, normalInterval)
                clickListener.onClick(touchedView)
            } else {
                handler.removeCallbacks(this)
                touchedView?.isPressed = false
                touchedView = null
            }
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                handler.removeCallbacks(handlerRunnable)
                handler.postDelayed(handlerRunnable, initialInterval)
                touchedView = view
                touchedView?.isPressed = true
                clickListener.onClick(view)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(handlerRunnable)
                touchedView?.isPressed = false
                touchedView = null
                return true
            }
        }

        return false    }
}