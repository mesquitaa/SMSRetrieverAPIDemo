package com.rpm.view

import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.rpm.R
import com.rpm.helper.SignatureHelper
import com.rpm.retriever.SMSReceiver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SMSReceiver.OnSMSReceiveListener {

	val smsBroadcast = SMSReceiver()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		SignatureHelper(this).getAppSignatures()

		startSMSListener()
		registerReceiver()

		smsBroadcast.addListener(this)
	}

	private fun registerReceiver() {
		val intentFilter = IntentFilter()
		intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)

		registerReceiver(smsBroadcast, intentFilter)
	}

	private fun startSMSListener() {
		val task = SmsRetriever.getClient(this /* context */).startSmsRetriever()
		task.addOnSuccessListener {
			// Successfully started retriever, expect broadcast intent
			label.setText(R.string.waiting_code)
		}

		task.addOnFailureListener {
			label.setText(R.string.start_receiver_error)
		}
	}

	override fun onConnected(p0: Bundle?) {}

	override fun onConnectionSuspended(p0: Int) {}

	override fun onConnectionFailed(p0: ConnectionResult) {}

	override fun onSMSReceived(code: String) {
		unregisterReceiver(smsBroadcast)
		label.text = getString(R.string.code_received, code)


		// the code below shows how to register listener again
		// startSMSListener()
		// registerReceiver()
	}

	override fun onSMSTimeOut() {
		startSMSListener()
		registerReceiver()
		label.text = "Timeout"
	}
}
