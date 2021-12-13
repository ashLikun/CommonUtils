package com.ashlikun.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.ashlikun.utils.other.LogUtils.i
import com.ashlikun.utils.other.StringUtils.isEmpty

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/13 23:48
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：电话呼叫广播
 * <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
 * <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"></uses-permission>
 *
 *
 * action: android.intent.action.PHONE_STATE;  android.intent.action.NEW_OUTGOING_CALL;
 *
 *
 * 去电时：
 * 未接：phone_state=OFFHOOK;
 * 挂断：phone_state=IDLE
 * 来电时:
 * 未接：phone_state=RINGING
 * 已接：phone_state=OFFHOOK;
 * 挂断：phone_state=IDLE
 */
typealias PhoneListener = (state: CallState, number: String) -> Unit

/**
 * 分别是：
 *
 *
 * 播出电话
 * 播出电话结束
 * 接入电话铃响
 * 接入通话中
 * 接入通话完毕
 */
enum class CallState {
    Outgoing, OutgoingEnd, IncomingRing, Incoming, IncomingEnd
}

class PhoneReceiver : BroadcastReceiver() {

    companion object {
        private const val RINGING = "RINGING"
        private const val OFFHOOK = "OFFHOOK"
        private const val IDLE = "IDLE"
        private const val PHONE_STATE = "android.intent.action.PHONE_STATE"
        private const val NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL"
        private const val INTENT_STATE = "state"
        private const val INTENT_INCOMING_NUMBER = "incoming_number"
    }

    var phoneListener: PhoneListener? = null
    private var isDialOut = false
    private var number: String = ""
    override fun onReceive(context: Context, intent: Intent) {
        i("action: " + intent.action)
        i("intent : ")
        val bundle = intent.extras
        for (key in bundle!!.keySet()) {
            i(key + " : " + bundle[key])
        }
        if (NEW_OUTGOING_CALL == intent.action) {
            isDialOut = true
            val outNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
            if (!isEmpty(outNumber)) {
                number = outNumber
            }
            phoneListener?.invoke(CallState.Outgoing, number)
        } else if (PHONE_STATE == intent.action) {
            val state = intent.getStringExtra(INTENT_STATE)
            val inNumber = intent.getStringExtra(INTENT_INCOMING_NUMBER)
            if (!isEmpty(inNumber)) {
                number = inNumber
            }
            if (RINGING == state) {
                isDialOut = false
                phoneListener?.invoke(CallState.IncomingRing, number)
            } else if (OFFHOOK == state) {
                phoneListener?.invoke(CallState.Incoming, number)
            } else if (IDLE == state) {
                if (isDialOut) {
                    phoneListener?.invoke(CallState.OutgoingEnd, number)
                } else {
                    phoneListener?.invoke(CallState.IncomingEnd, number)
                }
            }
        }
    }

    /**
     * 去电时：
     * 未接：phone_state=OFFHOOK;
     * 挂断：phone_state=IDLE
     * 来电时:
     * 未接：phone_state=RINGING
     * 已接：phone_state=OFFHOOK;
     * 挂断：phone_state=IDLE
     */
    fun registerReceiver(context: Context, phoneListener: PhoneListener) {
        try {
            val filter = IntentFilter()
            filter.addAction("android.intent.action.PHONE_STATE")
            filter.addAction("android.intent.action.NEW_OUTGOING_CALL")
            filter.priority = Int.MAX_VALUE
            context.registerReceiver(this, filter)
            this.phoneListener = phoneListener
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun unRegisterReceiver(context: Context) {
        try {
            context.unregisterReceiver(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}