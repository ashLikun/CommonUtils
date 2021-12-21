package com.ashlikun.utils.simple

import android.os.Parcel
import android.os.Parcelable

/**
 * 作者　　: 李坤
 * 创建时间: 2021.12.21　16:45
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
class TestParcelable(var s1: String = "", var s2: Int = 0) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(s1)
        parcel.writeInt(s2)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TestParcelable> {
        override fun createFromParcel(parcel: Parcel): TestParcelable {
            return TestParcelable(parcel)
        }

        override fun newArray(size: Int): Array<TestParcelable?> {
            return arrayOfNulls(size)
        }
    }
}