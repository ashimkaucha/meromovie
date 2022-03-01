package com.example.meromovie.entity

import android.os.Parcel
import android.os.Parcelable

data class Movie (
    var _id : String? = null,
    var mname : String? = null,
    var mdesc : String? = null,
    var releasedate : String? = null,
    var mcategories : String? = null,
    var cover : String? = null,

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(mname)
        parcel.writeString(mdesc)
        parcel.writeString(releasedate)
        parcel.writeString(mcategories)
        parcel.writeString(cover)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}



