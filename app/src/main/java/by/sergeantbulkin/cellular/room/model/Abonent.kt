package by.sergeantbulkin.cellular.room.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "abonent")
data class Abonent(
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "last_name") val lastName : String,
    @ColumnInfo(name = "middle_name") val middleName : String,
    @ColumnInfo(name = "sex") val isWoman : Boolean,
    @ColumnInfo(name = "age") val age : Int,
    @ColumnInfo(name = "address") val address : String,
    @ColumnInfo(name = "registration_date") val registrationDate : String,
    @ColumnInfo(name = "mobile_number") val mobileNumber : String,
    @ColumnInfo(name = "plan_name") val planName : String,
    val planId : Int) : Parcelable
{
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") var id : Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )
    {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeString(name)
        parcel.writeString(lastName)
        parcel.writeString(middleName)
        parcel.writeByte(if (isWoman) 1 else 0)
        parcel.writeInt(age)
        parcel.writeString(address)
        parcel.writeString(registrationDate)
        parcel.writeString(mobileNumber)
        parcel.writeString(planName)
        parcel.writeInt(planId)
        parcel.writeInt(id)
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