package br.com.bb.wifi_state_poc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WifiManager


fun scanWifi(context: Context, wifiReceiver: BroadcastReceiver, wifiManager: WifiManager) {
    context.registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
    wifiManager.startScan()
}