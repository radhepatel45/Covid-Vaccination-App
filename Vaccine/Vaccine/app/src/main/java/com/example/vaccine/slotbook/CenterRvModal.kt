package com.example.covidappdemo1.slotbook

import android.os.Parcel
import android.os.Parcelable

data class CenterRvModal(

    // string variable for center name.
    val centerName: String = "",
    val centerAddress: String = "",
    val centerFromTime: String = "",
    val centerToTime: String = "",
    val vaccineName: String = "",
    val ageLimit: Int = 0,
    val fee_type: String = "",
    val availableCapacity: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(centerName)
        parcel.writeString(centerAddress)
        parcel.writeString(centerFromTime)
        parcel.writeString(centerToTime)
        parcel.writeString(vaccineName)
        parcel.writeInt(ageLimit)
        parcel.writeString(fee_type)
        parcel.writeInt(availableCapacity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CenterRvModal> {
        override fun createFromParcel(parcel: Parcel): CenterRvModal {
            return CenterRvModal(parcel)
        }

        override fun newArray(size: Int): Array<CenterRvModal?> {
            return arrayOfNulls(size)
        }
    }
}