package by.sergeantbulkin.cellular

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.ContentLoadingProgressBar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.sergeantbulkin.cellular.room.AbonentsDatabase
import by.sergeantbulkin.cellular.room.model.*
import by.sergeantbulkin.cellular.ui.abonents.AbonentBottomSheet
import by.sergeantbulkin.cellular.ui.abonents.AbonentsAdapter
import by.sergeantbulkin.cellular.ui.plans.PlanBottomSheet
import by.sergeantbulkin.cellular.ui.plans.PlansAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity()
{
    //----------------------------------------------------------------------------------------------
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab : FloatingActionButton
    private lateinit var toolbar : Toolbar
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var listProgressBar : ContentLoadingProgressBar
    //Открыто в данный момент
    private lateinit var currentScreen: CurrentScreen
    //Адаптеры
    private lateinit var abonentsAdapter: AbonentsAdapter
    private lateinit var plansAdapter: PlansAdapter
    //CompositeDisposable для хранения подписок
    private val compositeDisposable = CompositeDisposable()
    //----------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState : Bundle?)
    {
        Log.d("TAG", "MainActivity - onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Настроить NavigationDrawer
        setUpViews()

        //Установить БД
        AbonentsDatabase.setAbonentsDatabase(applicationContext)

        //Инициализация адаптеров
        initAdapters()

        //initDataToDB()
    }
    //----------------------------------------------------------------------------------------------
    private fun initDataToDB()
    {
        val services1 = listOf(
            Service("Service 1.1", 25f),
            Service("Service 2.1", 32f),
            Service("Service 2.2", 55f),
            Service("Service 2.3", 74f),
            Service("Service 3.1", 25f),
            Service("Service 3.2", 51f),
            Service("Service 3.3", 84f),
            Service("Service 4.1", 25f),
            Service("Service 4.2", 42f),
            Service("Service 4.3", 14f))


        val plans = listOf(
            Plan("Plan 1"),
            Plan("Plan 2"),
            Plan("Plan 3"),
            Plan("Plan 4"))


        AbonentsDatabase.INSTANCE?.planDao()?.insertPlans(plans)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.d("TAG", "Inserted")
            },{
                Log.d("TAG", "Error: ${it.localizedMessage}")
            }).let {
                if (it != null)
                {
                    compositeDisposable.add(it)
                }
            }

        AbonentsDatabase.INSTANCE?.serviceDao()?.insertServices(services1)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.d("TAG", "Inserted")
            },{
                Log.d("TAG", "Error: ${it.localizedMessage}")
            }).let {
                if (it != null)
                {
                    compositeDisposable.add(it)
                }
            }

        val plansServices = listOf(
            PlanService(1,1),
            PlanService(1,2),
            PlanService(2, 4),
            PlanService(3,3),
            PlanService(3,2),
            PlanService(4, 3),
            PlanService(4, 2)
        )

        /*AbonentsDatabase.INSTANCE?.planDao()?.getPlansInfo()
            ?.flattenAsObservable { it }
            ?.doOnNext { it.setServicesId() }
            ?.collectInto(arrayListOf<PlanInfo>(), {list, item -> list.add(item)})
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.d("TAG", "Recieved")
                Log.d("TAG", it.toString())
            }, {
                Log.d("TAG", "Error: ${it.localizedMessage}")
            }).let {
                if (it != null)
                {
                    compositeDisposable.add(it)
                }
            }*/
    }
    //----------------------------------------------------------------------------------------------
    //Настроить NavigationDrawer
    private fun setUpViews()
    {
        //Инициализация RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        //Инициализация FAB
        fab = findViewById(R.id.fab)
        fab.setOnClickListener{
            openBottomSheet()
        }
        //Инициализация ToolBar и NavigationDrawer
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        //Инициализация Progressbar
        listProgressBar = findViewById(R.id.list_progress_bar)

        //Установить AppBar
        setSupportActionBar(toolbar)
        //Включить отображение, чтобы обработать нажатие
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //NavigationIcon click listener открывает NavigationDrawer
        toolbar.setNavigationOnClickListener { drawerLayout.open() }

        //Обработка выбора пунктов из NavigationDrawer
        navView.setNavigationItemSelectedListener {
            navView.setCheckedItem(it)
            drawerLayout.closeDrawers()
            when (it.itemId)
            {
                R.id.nav_abonents -> loadAbonents()
                R.id.nav_plans -> loadPlans()
                R.id.nav_services -> Unit
            }
            true
        }
    }
    //----------------------------------------------------------------------------------------------
    private fun openBottomSheet()
    {
        when(currentScreen)
        {
            CurrentScreen.ABONENTS -> AbonentBottomSheet{ abonent, operation -> operationTo(abonent, operation)}.show(supportFragmentManager, "abonent")
            CurrentScreen.PLANS -> PlanBottomSheet{planInfo, operation -> operationTo(planInfo, operation)}.show(supportFragmentManager, "plan")
            CurrentScreen.SERVICES -> Unit
        }
    }
    //----------------------------------------------------------------------------------------------
    //Инициализация адаптеров
    private fun initAdapters()
    {
        //Открыть информацию об абоненте
        abonentsAdapter = AbonentsAdapter { abonent -> openInfo(abonent) }
        plansAdapter = PlansAdapter { planInfo -> openInfo(planInfo) }

        recyclerView.adapter = abonentsAdapter
        loadAbonents()
    }
    //----------------------------------------------------------------------------------------------
    private fun loadAbonents()
    {
        //Убрать текущий адаптер
        clearAdapter()
        showProgressBar()
        //Текущий экран  - Абоненты
        currentScreen = CurrentScreen.ABONENTS
        //Загрузить всех абонентов и установить в адаптер
        AbonentsDatabase.INSTANCE?.abonentDao()
            ?.getAbonents()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                hideProgressBar()
                abonentsAdapter.setAbonents(it)
                recyclerView.adapter = abonentsAdapter
            },{
                Log.d("TAG", "Error: ${it.localizedMessage}")
            })?.let { compositeDisposable.add(it) }
    }
    private fun loadPlans()
    {
        //Убрать текущий адаптер
        clearAdapter()
        showProgressBar()
        //Текущий экран  - Тарифные планы
        currentScreen = CurrentScreen.PLANS
        //Загрузить все планы и установить в адаптер
        AbonentsDatabase.INSTANCE?.planDao()
            ?.getPlansInfo()
            ?.flattenAsObservable { it }
            ?.doOnNext {
                it.setServicesId()
                it.services = AbonentsDatabase.INSTANCE?.serviceDao()?.getServicesForPlan(it.servicesIDs)!!
            }
            ?.collectInto(arrayListOf<PlanInfo>(), {list, item -> list.add(item)})
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                hideProgressBar()
                plansAdapter.setPlans(it)
                recyclerView.adapter = plansAdapter
            },{
                Log.d("TAG", "Error: ${it.localizedMessage}")
            })?.let { compositeDisposable.add(it) }
    }
    //private fun loadServices()
    //----------------------------------------------------------------------------------------------
    private fun openInfo(ab: Abonent)
    {
        AbonentBottomSheet
            .newInstance(ab) { abonent, operation -> operationTo(abonent, operation) }
            .show(supportFragmentManager, "abonent")
    }
    private fun openInfo(plan : PlanInfo)
    {
        PlanBottomSheet
            .newInstance(plan) {planInfo, operation -> operationTo(planInfo, operation) }
            .show(supportFragmentManager, "planInfo")
    }
    //private fun openInfo(service : Service)
    //----------------------------------------------------------------------------------------------
    private fun operationTo(ab: Abonent, operation: Operation)
    {
        showProgressBar()

        when (operation)
        {
            Operation.ADD -> {
                AbonentsDatabase.INSTANCE?.abonentDao()
                    ?.insertAbonent(ab)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        abonentsAdapter.addItem(ab)
                        hideProgressBar()
                    }, {

                    })
            }
            Operation.UPDATE ->{
                AbonentsDatabase.INSTANCE?.abonentDao()
                    ?.updateAbonent(ab)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        abonentsAdapter.updateItem(ab)
                        hideProgressBar()
                    }, {

                    })
            }
            Operation.DELETE -> {
                AbonentsDatabase.INSTANCE?.abonentDao()
                    ?.deleteAbonent(ab)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        abonentsAdapter.removeItem(ab)
                        hideProgressBar()
                    }, {

                    })
            }
        }
    }
    private fun operationTo(planInfo: PlanInfo, operation: Operation)
    {
        showProgressBar()

        when (operation)
        {
            Operation.ADD -> {
                AbonentsDatabase.INSTANCE
                    ?.planDao()
                    ?.insertPlan(Plan(planInfo.planName))
                    ?.flatMapCompletable {
                        val planServices = arrayListOf<PlanService>()
                        for (service in planInfo.services)
                        {
                            planServices.add(PlanService(it.toInt(), service.id))
                        }
                        AbonentsDatabase.INSTANCE?.planDao()?.insertPlanServices(planServices)}
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        plansAdapter.addItem(planInfo)
                        hideProgressBar()
                    },{
                        Log.d("TAG", "Error: ${it.localizedMessage}")
                    })?.let { compositeDisposable.add(it) }
            }
            Operation.UPDATE -> {
                AbonentsDatabase.INSTANCE
                    ?.planDao()
                    ?.updatePlan(Plan(planInfo.planName).apply { id = planInfo.planId })
                    ?.andThen(AbonentsDatabase.INSTANCE?.planDao()?.deletePlanServices(planInfo.planId))
                    ?.andThen{
                        val planServices = arrayListOf<PlanService>()
                        for (service in planInfo.services)
                        {
                            planServices.add(PlanService(planInfo.planId, service.id))
                        }
                        AbonentsDatabase.INSTANCE?.planDao()?.insertPlanServices(planServices)}
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        plansAdapter.updateItem(planInfo)
                        hideProgressBar()
                    }, {
                        Log.d("TAG", "Error: ${it.localizedMessage}")
                    })?.let { compositeDisposable.add(it) }
            }
            Operation.DELETE -> {
                AbonentsDatabase.INSTANCE
                    ?.planDao()
                    ?.deletePlan(Plan(planInfo.planName).apply { id = planInfo.planId })
                    ?.andThen(AbonentsDatabase.INSTANCE?.planDao()?.deletePlanServices(planInfo.planId))
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        Log.d("TAG", "onComplete")
                        plansAdapter.removeItem(planInfo)
                        hideProgressBar()
                    },{
                        Log.d("TAG", "Error: ${it.localizedMessage}")
                    })?.let { compositeDisposable.add(it) }
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    //Перехват нажатия на кнопку "Назад"
    override fun onBackPressed()
    {
        //Если открыт NavigationDrawer, то закрыть его и выйти из функции
        if (drawerLayout.isOpen)
        {
            drawerLayout.closeDrawers()
            return
        }

        super.onBackPressed()
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu : Menu) : Boolean
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    //----------------------------------------------------------------------------------------------
    private fun showProgressBar()
    {
        listProgressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar()
    {
        listProgressBar.visibility = View.GONE
    }
    private fun clearAdapter()
    {
        recyclerView.adapter = null
    }
    //----------------------------------------------------------------------------------------------
}