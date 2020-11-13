package by.sergeantbulkin.cellular.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "abonent")
data class Abonent(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val _id : Int,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "last_name") val lastName : String,
    @ColumnInfo(name = "middle_name") val middleName : String,
    @ColumnInfo(name = "sex") val sex : Boolean,
    @ColumnInfo(name = "age") val age : Int,
    @ColumnInfo(name = "address") val address : String,
    @ColumnInfo(name = "registration_date") val registrationDate : String,
    @ColumnInfo(name = "mobile_number") val mobileNumber : String,
    @ColumnInfo(name = "plan_name") val planName : String,
    val planId : Int)
{

}