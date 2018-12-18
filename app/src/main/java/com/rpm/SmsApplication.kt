package com.rpm

import android.app.Application
import com.rpm.helper.SignatureHelper

class SmsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // appSignatures it's the values to put into one SMS
        SignatureHelper(this).getAppSignatures()
    }
}
