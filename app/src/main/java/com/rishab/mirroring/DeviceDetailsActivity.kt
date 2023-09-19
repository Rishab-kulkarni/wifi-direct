package com.rishab.mirroring

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class DeviceDetailsActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_details)

        val list = intent.getParcelableArrayListExtra<P2pDevice>("list")
        DLog.i(TAG, "onCreate", "P2p Device Information = $list")
    }

    companion object {
        private const val TAG = "DeviceDetailsActivity"
    }
}
