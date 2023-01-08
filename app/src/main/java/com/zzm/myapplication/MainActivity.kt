package com.zzm.myapplication

import android.annotation.SuppressLint
import android.graphics.PixelFormat
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), DeviceKeyMonitor.OnKeyListener {

    private var isFold = true
    private var deviceKeyMonitor: DeviceKeyMonitor? = null

    private var view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        deviceKeyMonitor = DeviceKeyMonitor(this, this)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            showWindow()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showWindow() {
        //  val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val layoutParam = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            format = PixelFormat.RGBA_8888
            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = MATCH_PARENT
            height = MATCH_PARENT
            gravity = Gravity.START or Gravity.TOP
        }
        view = LayoutInflater.from(this).inflate(R.layout.view, null)
        val cardView = view?.findViewById<CardView>(R.id.cardView)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        val list = listOf("苹果", "芒果", "李子", "西瓜", "榴莲")
        val adapter = AutoFillToolAdapter(list, windowManager, layoutParam, view)
        adapter.setShowCount(isFold)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.isSmoothScrollbarEnabled = true
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        recyclerView?.isNestedScrollingEnabled = false
        recyclerView?.adapter = adapter

        val layoutShowAll = view?.findViewById<FrameLayout>(R.id.layout_show_all)
        val tvShowAll = view?.findViewById<ImageView>(R.id.tv_show_all)
        val manage = view?.findViewById<Button>(R.id.manage)
        layoutShowAll?.setOnClickListener {
            isFold = !isFold
            adapter.setShowCount(isFold)
            if (!isFold) {
                tvShowAll?.visibility = View.GONE
                manage?.visibility = View.VISIBLE
            }
        }
        manage?.setOnClickListener {
            Toast.makeText(this, "manage click!", Toast.LENGTH_SHORT).show()
        }

        view?.setOnTouchListener { v, event ->
            val x = event.x.toInt()
            val y = event.y.toInt()
            val rect = Rect()
            cardView?.getGlobalVisibleRect(rect)
            if (!rect.contains(x, y)) {
                windowManager.removeView(v)
                isFold = true
            }
            return@setOnTouchListener false
        }

        windowManager.addView(view, layoutParam)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this, "按了返回键", Toast.LENGTH_SHORT).show()
            windowManager.removeView(view)
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onHomeClick() {
        Toast.makeText(this, "按了home键", Toast.LENGTH_SHORT).show()
        windowManager.removeView(view)
    }

    override fun onRecentClick() {
        Toast.makeText(this, "按了多任务键", Toast.LENGTH_SHORT).show()
        windowManager.removeView(view)
    }

    override fun onDestroy() {
        deviceKeyMonitor?.unregister()
        super.onDestroy()
    }
}