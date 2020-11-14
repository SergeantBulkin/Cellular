package by.sergeantbulkin.cellular.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.sergeantbulkin.cellular.room.dao.AbonentDAO
import by.sergeantbulkin.cellular.room.model.Abonent

@Database(entities = [Abonent::class], version = 1)
abstract class AbonentsDatabase : RoomDatabase()
{
    //Все DAO
    abstract fun abonentDao() : AbonentDAO

    companion object
    {
        //Объект БД
        var INSTANCE : AbonentsDatabase? = null

        fun setAbonentsDatabase(context: Context)
        {
            Log.d("TAG", "AbonentsDatabase - getAbonentsDatabase")
            //Создать, если еще не существует
            if (INSTANCE == null)
            {
                Log.d("TAG", "AbonentsDatabase - создание")
                synchronized(AbonentsDatabase::class)
                {
                    INSTANCE = Room.databaseBuilder(context, AbonentsDatabase::class.java, "abonents.db").build()
                }
            }
        }

        //Уничтожить объект БД
        fun destroyDataBase()
        {
            INSTANCE = null
        }
    }
}