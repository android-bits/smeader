package com.mementoguy.smeader

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val messageList = ArrayList<String>()

    lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messageList)
        list.adapter = arrayAdapter

        requestSmsPermission()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    override fun onStart() {
        super.onStart()

        mainActivityInstance = this
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                baseContext,
                "android.permission.READ_SMS"
            ) == PackageManager.PERMISSION_GRANTED
        )
            readSms("MPESA")
        else {

            val REQUEST_CODE_READ_SMS = 123
            ActivityCompat.requestPermissions(
                this,
                arrayOf("android.permission.READ_SMS"),
                REQUEST_CODE_READ_SMS
            )
        }
    }

    private fun readSms(senderId: String) {
//        columns of interest
        val smsColumns =
            arrayOf(Telephony.TextBasedSmsColumns.BODY, Telephony.TextBasedSmsColumns.ADDRESS)
//        query selection criteria
        val smsSelection = "${Telephony.TextBasedSmsColumns.ADDRESS} =?"
        val smsArgs = arrayOf(senderId)
//execute query
        val smsInboxCursor = contentResolver.query(
            Uri.parse("content://sms/inbox"),
            smsColumns,
            smsSelection,
            smsArgs,
            null
        )

        val indexBody = smsInboxCursor?.getColumnIndex("body")
//loop through results and add to list adapter

        if (!smsInboxCursor!!.moveToFirst()) return
        arrayAdapter.clear()

        do {
            val smsMpesaSuccess = filterMpesaSuccessSms(smsInboxCursor.getString(indexBody!!))
            if (smsMpesaSuccess.isNotBlank())
                arrayAdapter.add(smsMpesaSuccess)

        } while (smsInboxCursor.moveToNext())
    }

    fun updateList(smsMessage: String) {
        arrayAdapter.insert(smsMessage, 0)
        arrayAdapter.notifyDataSetChanged()
    }

    fun filterMpesaSuccessSms(smsBody: String): String {
        var smsMpesaSuccess = ""
        val indexSearchEnd = smsBody.indexOfFirst {
            it.equals('.')
        }

        try {
            if (smsBody.substring(0, indexSearchEnd).contains("Confirmed"))
                smsMpesaSuccess = smsBody
        } catch (e: StringIndexOutOfBoundsException) {

        }

        return smsMpesaSuccess
    }


    companion object {
        lateinit var mainActivityInstance: MainActivity
    }
}