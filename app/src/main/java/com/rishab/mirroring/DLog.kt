package com.rishab.mirroring

import android.util.Log

object DLog {

    @JvmStatic
    fun i(tag : String, methodName : String, msg : String) {
        Log.i(tag, "Rishab@ $methodName, $msg")
    }
}