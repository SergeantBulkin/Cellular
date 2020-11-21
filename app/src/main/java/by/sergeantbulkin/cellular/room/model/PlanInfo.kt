package by.sergeantbulkin.cellular.room.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation

data class PlanInfo(
    val planId : Int,
    val planName : String,
    val servicesId : String,
    val planCost : Float
) : Parcelable
{
    @Ignore
    var servicesIDs = arrayListOf<Int>()
    @Ignore
    var services = listOf<Service>()

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat()
    )
    {
        servicesIDs.addAll(parcel.createIntArray()!!.toList())
        services = parcel.createTypedArrayList(Service)!!
    }

    fun servicesToString() : String
    {
        var string = ""
        for (i in services.indices)
        {
            string += when(i)
            {
                services.size-1 -> services[i].name
                else -> "${services[i].name}\n"
            }
        }
        return string
    }

    fun setServicesId()
    {
        for (str in servicesId.split(","))
        {
            servicesIDs.add(str.toInt())
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeInt(planId)
        parcel.writeString(planName)
        parcel.writeString(servicesId)
        parcel.writeFloat(planCost)
        parcel.writeIntArray(servicesIDs.toIntArray())
        parcel.writeTypedList(services)
    }

    override fun describeContents(): Int
    {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlanInfo>
    {
        override fun createFromParcel(parcel: Parcel): PlanInfo
        {
            return PlanInfo(parcel)
        }

        override fun newArray(size: Int): Array<PlanInfo?>
        {
            return arrayOfNulls(size)
        }
    }
}