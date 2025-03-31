package com.example.th5_bai3_blockphone
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast
import android.os.Build
import android.telecom.TelecomManager
import androidx.annotation.RequiresPermission

class CallBlockerReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.ANSWER_PHONE_CALLS)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber != null) {
                if (isBlockedNumber(incomingNumber, context)) {
                    endCall(context)
                    Toast.makeText(context, "Chặn cuộc gọi từ: $incomingNumber", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isBlockedNumber(phoneNumber: String, context: Context?): Boolean {
        val blockedNumbers = listOf("+84901234567", "+84987654321") // Danh sách số bị chặn
        return blockedNumbers.contains(phoneNumber)
    }

    @RequiresPermission(Manifest.permission.ANSWER_PHONE_CALLS)
    private fun endCall(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val telecomManager = context?.getSystemService(Context.TELECOM_SERVICE) as? TelecomManager
            telecomManager?.endCall()
        } else {
            try {
                val telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val clazz = Class.forName(telephonyManager.javaClass.name)
                val method = clazz.getDeclaredMethod("getITelephony")
                method.isAccessible = true
                val iTelephony = method.invoke(telephonyManager)
                val endCallMethod = iTelephony.javaClass.getDeclaredMethod("endCall")
                endCallMethod.invoke(iTelephony)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}