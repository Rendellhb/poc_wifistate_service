package br.com.bb.wifi_state_poc

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 99

    private var mServiceIntent: Intent? = null
    private lateinit var wifiStateService: WifiService
    private lateinit var wifiManager: WifiManager
    private lateinit var wifiReceiver: WifiReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        wifiStateService = WifiService()
        mServiceIntent = Intent(this, WifiService::class.java)

        if (!isMyServiceRunning(WifiService::class.java)) {
            startService(mServiceIntent)
        }

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }

        val arrayList =  ArrayList<String>()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)
        listview.setAdapter(adapter)

        wifiReceiver = WifiReceiver(wifiManager, this, adapter, arrayList)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION)
            } else {
                scanWifi(view.context, wifiReceiver, wifiManager)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanWifi(this, wifiReceiver, wifiManager)
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }

    override fun onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy()
    }
}
