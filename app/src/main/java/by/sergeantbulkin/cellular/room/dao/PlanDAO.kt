package by.sergeantbulkin.cellular.room.dao

import android.graphics.LightingColorFilter
import androidx.room.*
import by.sergeantbulkin.cellular.room.model.Plan
import by.sergeantbulkin.cellular.room.model.PlanInfo
import by.sergeantbulkin.cellular.room.model.PlanService
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PlanDAO
{
    //Добавить тарифный план
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlan(plan: Plan) : Completable

    //Добавить тарифные планы
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlans(plans: List<Plan>) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlanServices(plansService: List<PlanService>) : Completable

    //Удалить тарифный план
    @Delete
    fun deletePlan(plan: Plan) : Completable

    //Обновить тарифный план
    @Update
    fun updatePlan(plan: Plan)

    //Получить все тарифные планы
    @Query("SELECT * FROM `plan`")
    fun getPlans() : Single<List<Plan>>

    @Query("SELECT * FROM PlanService")
    fun getPlansServices() : Single<List<PlanService>>

    //SELECT  planId, planName,  GROUP_CONCAT(Service.serviceId) AS services_id, Service.serviceName, SUM(Service.cost) AS planCost
    // FROM `plan`
    //INNER JOIN PlanService
    // ON planId = PlanService.plan_id
    //INNER JOIN Service ON Service.serviceId = PlanService.service_id
    // GROUP BY planId
    @Transaction
    @Query("SELECT" +
            " Plan.id AS planId," +
            " Plan.name AS planName," +
            " GROUP_CONCAT(Service.id) AS servicesId," +
            " SUM(Service.cost) AS planCost " +
            "FROM Plan" +
            " INNER JOIN PlanService " +
            "ON Plan.id = PlanService.plan_id" +
            " INNER JOIN Service " +
            "ON Service.id = PlanService.service_id" +
            " GROUP BY Plan.id")
    fun getPlansInfo() : Single<List<PlanInfo>>
}