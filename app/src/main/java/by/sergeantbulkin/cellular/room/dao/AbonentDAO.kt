package by.sergeantbulkin.cellular.room.dao

import androidx.room.*
import by.sergeantbulkin.cellular.room.model.Abonent
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface AbonentDAO
{
    //Добавить абонента
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAbonent(abonent: Abonent) : Completable

    //Удалить абонента
    @Delete
    fun deleteAbonent(abonent: Abonent) : Completable

    //Обновить абонента
    @Update
    fun updateAbonent(abonent: Abonent)

    //Получить всех абонентов
    @Query("SELECT * FROM abonent")
    fun getAbonents() : Single<List<Abonent>>
}