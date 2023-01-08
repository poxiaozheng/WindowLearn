package com.zzm.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class DeviceKeyMonitor(private val context: Context, private val listener: OnKeyListener) {
    private var deviceKeyReceiver: BroadcastReceiver? = null

    //通过广播监听Home键和多任务键点击
    init {
        deviceKeyReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                    val reason = intent.getStringExtra(SYSTEM_REASON)
                    if (!reason.isNullOrEmpty()) {
                        if (SYSTEM_HOME_KEY == reason) {
                            listener.onHomeClick()
                        } else if (SYSTEM_HOME_RECENT_APPS == reason) {
                            listener.onRecentClick()
                        }
                    }
                }
            }
        }
        context.registerReceiver(
            deviceKeyReceiver,
            IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        )
    }

    fun unregister() {
        deviceKeyReceiver?.let {
            context.unregisterReceiver(it)
            deviceKeyReceiver = null
        }
    }

    interface OnKeyListener {
        fun onHomeClick()
        fun onRecentClick()
    }

    companion object {
        private const val SYSTEM_REASON = "reason"
        private const val SYSTEM_HOME_KEY = "homekey"
        private const val SYSTEM_HOME_RECENT_APPS = "recentapps"
    }
}