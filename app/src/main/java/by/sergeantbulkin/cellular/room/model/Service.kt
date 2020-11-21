package by.sergeantbulkin.cellular.room.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Service(
    val name : String,
    val cost : Float) : Parcelable
{
    @PrimaryKey(autoGenerate = true) var id : Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readFloat()
    )
    {
        id = parcel.readInt()
    }

    override fun toString(): String
    {
        return name
    }

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeString(name)
        parcel.writeFloat(cost)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int
    {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Service>
    {
        override fun createFromParcel(parcel: Parcel): Service
        {
            return Service(parcel)
        }

        override fun newArray(size: Int): Array<Service?>
        {
            return arrayOfNulls(size)
        }
    }
}