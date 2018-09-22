package io.github.rajdeep1008.apkwizard.models

import android.content.pm.ApplicationInfo
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by rajdeep1008 on 19/04/18.
 */
data class Apk(val appInfo: ApplicationInfo, val appName: String, val packageName: String? = "", val version: String? = "", val systemApp: Boolean) : Parcelable {
    constructor(parcel: Parcel) : this (
            parcel.readParcelable(ApplicationInfo::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(appInfo, flags)
        parcel.writeString(appName)
        parcel.writeString(packageName)
        parcel.writeString(version)
        parcel.writeByte(if (systemApp) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Apk> {
        override fun createFromParcel(parcel: Parcel): Apk = Apk(parcel)
        override fun newArray(size: Int): Array<Apk?> = arrayOfNulls(size)
    }
}