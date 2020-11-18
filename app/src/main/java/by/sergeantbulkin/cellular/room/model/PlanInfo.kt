package by.sergeantbulkin.cellular.room.model

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation

data class PlanInfo(
    val planId : Int,
    val planName : String,
    val servicesId : String,
    val planCost : Float
)
{
    @Ignore
    var servicesIDs = arrayListOf<Int>()

    fun servicesToString() : String
    {
        var string = ""
        /*for (service in services)
        {
            string += "${service.serviceName}\n"
        }*/
        return string
    }

    fun servicesCost() : Float
    {
        /*for (service in services)
        {
            cost += service.cost
        }*/
        return planCost
    }

    fun setServicesId()
    {
        for (str in servicesId.split(","))
        {
            servicesIDs.add(str.toInt())
        }
    }
}