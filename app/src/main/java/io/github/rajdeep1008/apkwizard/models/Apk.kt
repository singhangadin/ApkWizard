package io.github.rajdeep1008.apkwizard.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by rajdeep1008 on 19/04/18.
 */
data class Apk(val label: String, val packageName: String, val version: String, val systemApp: Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(label)
        parcel.writeString(packageName)
        parcel.writeString(version)
        parcel.writeByte(if (systemApp) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Apk> {
        override fun createFromParcel(parcel: Parcel): Apk {
            return Apk(parcel)
        }

        override fun newArray(size: Int): Array<Apk?> {
            return arrayOfNulls(size)
        }
    }
}