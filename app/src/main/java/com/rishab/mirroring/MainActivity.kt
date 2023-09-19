package com.rishab.mirroring

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var scanButton: Button
    private lateinit var stopScanButton: Button
    private lateinit var deviceListView : ListView
    private lateinit var adapter : ArrayAdapter<P2pDevice>
    private var deviceNameList : ArrayList<P2pDevice> = ArrayList<P2pDevice>()


    private val intentFilter = IntentFilter()
    private lateinit var manager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var broadcastReceiver: WifiDirectBroadcastReceiver

    private lateinit var handler: Handler
    private var discoveryRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this

        initializeUI()
        initializeWifiP2P()

        DLog.i(TAG, "onCreate", "")

        scanButton.setOnClickListener {
            if(!discoveryRunning) {
                discoveryRunning = true
                startP2pDiscovery()
            }
        }

        stopScanButton.setOnClickListener {
            if(discoveryRunning) {
                discoveryRunning = false
                stopP2pDiscovery()
            }
        }

        deviceListView.setOnItemClickListener { adapterView, view, position, id ->
            run {
                val intent = Intent(this, DeviceDetailsActivity::class.java)
                intent.putParcelableArrayListExtra("list", deviceNameList)
                startActivity(intent)
            }
        }
    }

    private fun initializeWifiP2P() {
        intentFilter.apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }

        handler = Handler(Looper.getMainLooper())
        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(context, Looper.getMainLooper(), null)
        broadcastReceiver = WifiDirectBroadcastReceiver(handler, manager, channel, context, adapter, deviceNameList)

        registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun initializeUI() {
        scanButton = findViewById(R.id.scanButton)
        stopScanButton = findViewById(R.id.stopScanButton)
        deviceListView = findViewById(R.id.listView)

        adapter = ArrayAdapter<P2pDevice>(this, android.R.layout.simple_list_item_1, deviceNameList)
        deviceListView.adapter = adapter
    }

    private fun startP2pDiscovery() {
        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                DLog.i(TAG, "onSuccess", "")
            }

            override fun onFailure(failReason: Int) {
                DLog.i(TAG, "onFailure", "")
            }
        })
    }

    private fun stopP2pDiscovery() {
        manager.stopPeerDiscovery(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                DLog.i(TAG, "onSuccess", "")
            }
            override fun onFailure(p0: Int) {
                DLog.i(TAG, "onFailure", "")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        deviceNameList.toMutableList().clear()
        unregisterReceiver(broadcastReceiver)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
