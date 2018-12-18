package com.rpm.retriever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SMSReceiver : BroadcastReceiver() {

	private var otpReceiver: OnSMSReceiveListener? = null

	fun addListener(receiver: OnSMSReceiveListener) {
		this.otpReceiver = receiver
	}

	override fun onReceive(context: Context, intent: Intent) {
		if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
			val status = intent.extras?.get(SmsRetriever.EXTRA_STATUS) as Status?

			when (status?.statusCode) {
				CommonStatusCodes.SUCCESS -> {
					// Get SMS message contents
					val message: String = intent.extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
					otpReceiver?.onSMSReceived(message.replace(Regex("[^\\d]"), ""))
				}

				// This event occurs when 5 minutes was passed, withoud receive sms
				CommonStatusCodes.TIMEOUT -> otpReceiver?.onSMSTimeOut()
			}
		}
	}

	interface OnSMSReceiveListener {
		fun onSMSReceived(otp: String)
		fun onSMSTimeOut()
	}
}
