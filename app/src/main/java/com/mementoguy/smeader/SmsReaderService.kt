package com.mementoguy.smeader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.lifecycle.MutableLiveData


/**
 * Created by Edward Muturi on 02/05/2020.
 */
class SmsReaderService : BroadcastReceiver() {

//    val smsLiveData  = MutableLiveData<List<String>>()

    companion object{
        val BUNDLE_SMS = "pdus"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        val intentExtras = intent?.extras
        if (intentExtras != null ){
            val sms = intentExtras.get(BUNDLE_SMS) as Array<Any>?
            var smsString = ""

            for (i in sms!!.indices){
                val smsMessage = SmsMessage.createFromPdu(sms[i] as ByteArray)

                val smsBody = smsMessage.toString()
                val address = smsMessage.originatingAddress

                smsString = "SMS FROM: $address \n $smsBody"
            }

            Toast.makeText(context, smsString, Toast.LENGTH_SHORT).show()

            val mainActivityInstance = MainActivity.mainActivityInstance
            mainActivityInstance.updateList(smsString)

        }
    }


}
