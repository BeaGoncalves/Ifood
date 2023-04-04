package com.example.ifood

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Jamilton
 */
class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val mListener: OnItemClickListener) :
    RecyclerView.OnItemTouchListener {

    private val mGestureDetector: GestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            val child: View? = recyclerView.findChildViewUnder(e.x, e.y)
            if (child != null) {
                mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child))
            }
        }
    })

    interface OnItemClickListener : AdapterView.OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onLongItemClick(view: View?, position: Int)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView: View? = rv.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView))
            return true
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}
