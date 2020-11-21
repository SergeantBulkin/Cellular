package by.sergeantbulkin.cellular.room.dao

import androidx.room.*
import by.sergeantbulkin.cellular.room.model.Service
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ServiceDAO
{
    //Добавить услугу
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertService(service: Service) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertServices(services: List<Service>) : Completable

    //Удалить услугу
    @Delete
    fun deleteService(service: Service) : Completable

    //Обновить услугу
    @Update
    fun updateService(service: Service) : Completable

    //Получить все услуги
    @Query("SELECT * FROM service")
    fun getServices() : Single<List<Service>>

    //Получить услуги для плана
    @Query("SELECT * FROM service WHERE id IN (:values)")
    fun getServicesForPlan(values : List<Int>) : List<Service>
}