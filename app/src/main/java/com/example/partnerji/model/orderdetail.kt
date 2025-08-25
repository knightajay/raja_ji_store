package com.example.partnerji.model

import android.os.Parcel
import android.os.Parcelable
class orderdetail (): Parcelable {
    var userUid: String? = null
    var username: String? = null
    var useradrees: String? = null
    var useremail: String? = null
    var companyname: String? = null
    var companyaddress: String? = null
    var companyprice: String? = null
    var companyimage: String? = null
    var usercontactno: String? = null
    var selectdate: String? = null
    var itemPushKey: String? = null
    var orderAccepted: Boolean = false
    var paymentrecieve: Boolean = false

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        username = parcel.readString()
        useradrees = parcel.readString()
        useremail = parcel.readString()
        companyname = parcel.readString()
        companyaddress = parcel.readString()
        companyprice = parcel.readString()
        companyimage=parcel.readString()
        usercontactno = parcel.readString()
        selectdate = parcel.readString()
        itemPushKey = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentrecieve = parcel.readByte() != 0.toByte()
    }

    constructor(
        userId: String,
        nameofusers: String,
        addressofuser: String,
        emailofuser: String,
        companayname: String,
        compayaddress: String,
        companyPrice: String,
        conpanyimage: String,
        conatactnumber: String,
        selecetsdate: String,
        itemPushKey: String?,
        b: Boolean,
        b1: Boolean
    ) : this() {
        this.userUid = userId
        this.username = nameofusers
        this.useradrees = addressofuser
        this.useremail = emailofuser
        this.companyname = companayname
        this.companyaddress = compayaddress
        this.companyprice = companyPrice
        this.companyimage = conpanyimage
        this.usercontactno = conatactnumber
        this.selectdate = selecetsdate
        this.itemPushKey = itemPushKey
        this.orderAccepted = orderAccepted
        this.paymentrecieve = paymentrecieve
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(username)
        parcel.writeString(useradrees)
        parcel.writeString(useremail)
        parcel.writeString(companyname)
        parcel.writeString(companyaddress)
        parcel.writeString(companyprice)
        parcel.writeString(companyimage)
        parcel.writeString(usercontactno)
        parcel.writeString(selectdate)
        parcel.writeString(itemPushKey)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentrecieve) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<orderdetail> {
        override fun createFromParcel(parcel: Parcel): orderdetail {
            return orderdetail(parcel)
        }

        override fun newArray(size: Int): Array<orderdetail?> {
            return arrayOfNulls(size)
        }
    }
}


