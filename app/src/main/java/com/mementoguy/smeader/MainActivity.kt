package com.mementoguy.smeader

import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    override fun onStart() {
        super.onStart()

        mainActivityInstance= this
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

    }


    companion object {
       lateinit var mainActivityInstance : MainActivity
    }
}