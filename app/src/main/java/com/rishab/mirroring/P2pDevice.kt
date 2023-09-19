package com.rishab.mirroring

import android.os.Parcel
import android.os.Parcelable

data class P2pDevice(
    val name : String,
    val mac : String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(mac)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        var str = ""
        str += "[Name] : $name"
        str += "[P2P Mac] :$mac"
        return str
    }

    companion object CREATOR : Parcelable.Creator<P2pDevice> {
        override fun createFromParcel(parcel: Parcel): P2pDevice {
            return P2pDevice(parcel)
        }

        override fun newArray(size: Int): Array<P2pDevice?> {
            return arrayOfNulls(size)
        }
    }
}
