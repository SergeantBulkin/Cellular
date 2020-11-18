package by.sergeantbulkin.cellular.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Service(
    val name : String,
    val cost : Float)
{
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}