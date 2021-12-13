package com.ashlikun.utils.receiver

import android.content.*
import android.net.Uri
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SmsMessage
import com.ashlikun.utils.other.LogUtils.i

/**
 * @author　　: 李坤
 * 创建时间: 2021/12/13 23:53
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：短信接收器，可获取短信内容，发送者号码，短信中心号码等。
 * <uses-permission android:name="android.permission.RECEIVE_SMS"></uses-permission>
 * action: android.provider.Telephony.SMS_RECEIVED
 */
typealias SmsListener = (msg: SmsMessage) -> Unit
typealias SmsListenerLast = (msg: String, fromAddress: String, serviceCenterAddress: String) -> Unit

class SmsReceiver : BroadcastReceiver() {
    var smsListener: SmsListener? = null
    var smsListenerLast: SmsListenerLast? = null
    override fun onReceive(context: Context, intent: Intent) {
        try {
            i("收到广播：" + intent.action)
            val bundle = intent.extras
            for (key in bundle!!.keySet()) {
                i(key + " : " + bundle[key])
            }
            val pdus = intent.extras!!["pdus"] as Array<Any>?
            var fromAddress = ""
            var serviceCenterAddress = ""
            if (pdus != null) {
                var msgBody = ""
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                    for (obj in pdus) {
                        val sms = SmsMessage.createFromPdu(obj as ByteArray)
                        msgBody += sms.messageBody ?: ""
                        fromAddress = sms.originatingAddress ?: ""
                        serviceCenterAddress = sms.serviceCenterAddress
                        smsListener?.invoke(sms)
                    }
                }
                smsListenerLast?.invoke(msgBody, fromAddress, serviceCenterAddress)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun registerSmsReceiver(context: Context, smsListener: SmsListener?) {
        try {
            this.smsListener = smsListener
            val filter = IntentFilter()
            filter.addAction("android.provider.Telephony.SMS_RECEIVED")
            filter.priority = Int.MAX_VALUE
            context.registerReceiver(this, filter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun unRegisterSmsReceiver(context: Context) {
        try {
            context.unregisterReceiver(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    companion object {
        fun sendMsgToPhone(phone: String, msg: String) {
            i("发送手机：$phone ,内容： $msg")
            val manager = SmsManager.getDefault()
            val texts: List<String> = manager.divideMessage(msg)
            for (txt in texts) {
                manager.sendTextMessage(phone, null, txt, null, null)
            }

        }

        fun saveMsgToSystem(context: Context, phone: String?, msg: String?) {
            val values = ContentValues()
            values.put("date", System.currentTimeMillis())
            //阅读状态 
            values.put("read", 0)
            //1为收 2为发  
            values.put("type", 2)
            values.put("address", phone)
            values.put("body", msg)
            context.contentResolver.insert(Uri.parse("content://sms/inbox"), values)
        }
    }
}