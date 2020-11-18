package by.sergeantbulkin.cellular.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Plan(
    val name : String)
{
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}