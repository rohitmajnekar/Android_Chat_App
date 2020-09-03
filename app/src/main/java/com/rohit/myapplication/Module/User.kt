package com.rohit.myapplication.Module

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val Status:String,val Name:String,val Email:String,val Image:String,val Thumb:String, val id:String ):
    Parcelable {
    constructor():this("","","","","","")
}