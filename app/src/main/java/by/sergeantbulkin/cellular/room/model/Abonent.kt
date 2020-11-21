package by.sergeantbulkin.cellular.room.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Abonent(
    val name : String,
    val lastName : String,
    val middleName : String,
    @ColumnInfo(name = "sex") val isWoman : Boolean,
    val age : Int,
    val address : String,
    val registrationDate: Long,
    val mobileNumber : String,
    val planId : Int) : Parcelable
{
    @PrimaryKey(autoGenerate = true) var abonentId : Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt()
    )
    {
        abonentId = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeString(name)
        parcel.writeString(lastName)
        parcel.writeString(middleName)
        parcel.writeByte(if (isWoman) 1 else 0)
        parcel.writeInt(age)
        parcel.writeString(address)
        parcel.writeLong(registrationDate)
        parcel.writeString(mobileNumber)
        parcel.writeInt(planId)
        parcel.writeInt(abonentId)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Abonent>
    {
        override fun createFromParcel(parcel: Parcel): Abonent
        {
            return Abonent(parcel)
        }

        override fun newArray(size: Int): Array<Abonent?>
        {
            return arrayOfNulls(size)
        }
    }
}