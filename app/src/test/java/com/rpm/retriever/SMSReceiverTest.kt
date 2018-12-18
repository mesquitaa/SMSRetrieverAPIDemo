package com.rpm.retriever

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.phone.SmsRetriever.*
import com.google.android.gms.common.api.CommonStatusCodes.*
import com.google.android.gms.common.api.Status
import com.rpm.retriever.SMSReceiver.OnSMSReceiveListener
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class SMSReceiverTest {

    @Mock lateinit var listener: OnSMSReceiveListener
    @Mock lateinit var context: Context
    @Mock lateinit var bundle: Bundle
    @Mock lateinit var intent: Intent

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(intent.action).thenReturn(SMS_RETRIEVED_ACTION)
        `when`(intent.extras).thenReturn(bundle)
        `when`(bundle.get(EXTRA_SMS_MESSAGE)).thenReturn("Your code: 5160")
    }

    @Test
    fun shouldCallListenerSuccessWhenStatusEquals_0() {
        `when`(bundle.get(EXTRA_STATUS)).thenReturn(Status(SUCCESS))
        val receiver = SMSReceiver()
        receiver.addListener(listener)
        receiver.onReceive(context, intent)

        verify(listener).onSMSReceived(otp = "5160")
    }

    @Test
    fun shouldCallListenerTimeOutWhenStatusEquals_15() {
        `when`(bundle.get(EXTRA_STATUS)).thenReturn(Status(TIMEOUT))
        val receiver = SMSReceiver()
        receiver.addListener(listener)
        receiver.onReceive(context, intent)

        verify(listener).onSMSTimeOut()
    }

    @Test
    fun shouldNotBrokenAppWhenListenerNotIsAttached() {
        `when`(bundle.get(EXTRA_STATUS)).thenReturn(Status(SUCCESS))
        val receiver = SMSReceiver()
        receiver.onReceive(context, intent)

        verify(listener, never()).onSMSTimeOut()
        verify(listener, never()).onSMSReceived(otp = "5160")
    }

    @Test
    fun ignoreAnyOtherStatus() {
        `when`(bundle.get(EXTRA_STATUS)).thenReturn(Status(SUCCESS_CACHE))
        val receiver = SMSReceiver()
        receiver.addListener(listener)
        receiver.onReceive(context, intent)

        verify(listener, never()).onSMSTimeOut()
        verify(listener, never()).onSMSReceived(otp = "5160")
    }
}
