package com.rishab.mirroring

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager
import android.os.Handler
import android.util.Log
import android.widget.ArrayAdapter

class WifiDirectBroadcastReceiver(
    private val handler: Handler,
    private val wifiP2pManager: WifiP2pManager,
    private val channel : WifiP2pManager.Channel,
    private val context: Context,
    private val adapter : ArrayAdapter<P2pDevice>,
    private val deviceNameList : ArrayList<P2pDevice>
) : BroadcastReceiver() {
    override fun onReceive(context: Context , intent: Intent) {
        Log.i(TAG, "Rishab@ ${intent.action}")

        when(intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                Log.i(TAG, "Rishab@ $state")
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                wifiP2pManager.requestPeers(channel) { wifiP2pDeviceList ->
                    for(device in wifiP2pDeviceList.deviceList) {
                        val p2pDeviceInfo = P2pDevice(device.deviceName, device.deviceAddress)
                        val isDevicePresentInList = ((deviceNameList.find { it.mac == p2pDeviceInfo.mac }) != null)
                        if(!isDevicePresentInList) {
                            adapter.add(p2pDeviceInfo)
                            deviceNameList.add(p2pDeviceInfo)
                        }
                    }
                }
            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {}
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {}
        }
    }

    companion object {
        private const val TAG = "WifiDirectBroadcastReceiver"
    }
}
