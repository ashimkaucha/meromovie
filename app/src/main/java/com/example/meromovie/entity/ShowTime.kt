package com.example.meromovie.entity

import android.os.Parcel
import android.os.Parcelable

data class ShowTime (
    var _id : String? = null,
    var userId : String? = null,
    var movieId : String? = null,
    var datetime : String? = null,
    var price : String? = null,

    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(userId)
        parcel.writeString(movieId)
        parcel.writeString(datetime)
        parcel.writeString(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShowTime> {
        override fun createFromParcel(parcel: Parcel): ShowTime {
            return ShowTime(parcel)
        }

        override fun newArray(size: Int): Array<ShowTime?> {
            return arrayOfNulls(size)
        }
    }
}



