package br.com.bb.wifi_state_poc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.ArrayAdapter

class WifiReceiver(
    private val wifiManager: WifiManager,
    private val context: Context,
    private val adapter: ArrayAdapter<String>?,
    private val arrayList: ArrayList<String>?
) : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        arrayList?.clear()
        val results = wifiManager.getScanResults()
        context.unregisterReceiver(this)

        for (scanResult in results) {
            if (adapter == null) {
                Log.i("WifiReceiver", scanResult.BSSID +
                        " - " + scanResult.SSID +
                        " - " + scanResult.capabilities)
            } else {
                arrayList?.add(scanResult.SSID + " - " + scanResult.capabilities)
                adapter.notifyDataSetChanged()
            }
        }
    }
}