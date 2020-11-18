package by.sergeantbulkin.cellular.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlanService(
    @ColumnInfo(name = "plan_id") val plan_id : Int,
    @ColumnInfo(name = "service_id") val service_id : Int,
)
{
    @PrimaryKey(autoGenerate = true) var planServiceId : Int = 0
}