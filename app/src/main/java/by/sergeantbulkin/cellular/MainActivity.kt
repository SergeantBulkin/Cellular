package by.sergeantbulkin.cellular

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.sergeantbulkin.cellular.room.AbonentsDatabase
import by.sergeantbulkin.cellular.ui.SharedViewModel
import by.sergeantbulkin.cellular.ui.abonent.AbonentFragment
import by.sergeantbulkin.cellular.ui.abonents.AbonentsFragment
import by.sergeantbulkin.cellular.ui.plans.PlansFragment
import by.sergeantbulkin.cellular.ui.services.ServicesFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity()
{
    //----------------------------------------------------------------------------------------------
    private lateinit var abonentsFragment: AbonentsFragment
    private lateinit var plansFragment: PlansFragment
    private lateinit var servicesFragment: ServicesFragment
    private lateinit var currentFragment: Fragment
    private lateinit var toolbar : Toolbar
    private lateinit var drawerLayout : DrawerLayout
    //----------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Инициализация фрагментов
        initFragments()

        //Настроить NavigationDrawer
        setUpNavigationDrawer()

        //Установить БД
        AbonentsDatabase.setAbonentsDatabase(applicationContext)

        //Установить слушателей SharedViewModel
        setSharedViewModelListeners()
    }
    //----------------------------------------------------------------------------------------------
    //Инициализация фрагментов
    private fun initFragments()
    {
        //Инициализация
        abonentsFragment = AbonentsFragment()
        plansFragment = PlansFragment()
        servicesFragment = ServicesFragment()

        //Добавить все фрагменты и спрятать их
        supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, abonentsFragment).hide(abonentsFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, plansFragment).hide(plansFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, servicesFragment).commit()

        currentFragment = servicesFragment
        loadFragmentFromDrawer(abonentsFragment)
    }
    //----------------------------------------------------------------------------------------------
    //Настроить NavigationDrawer
    private fun setUpNavigationDrawer()
    {
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        //Установить AppBar
        setSupportActionBar(toolbar)
        //NavigationIcon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //NavigationIcon click listener
        toolbar.setNavigationOnClickListener {
            when(currentFragment)
            {
                is AbonentFragment -> backToFragment(abonentsFragment)
                else -> drawerLayout.open()
            }
        }

        navView.setNavigationItemSelectedListener {
            navView.setCheckedItem(it)
            when (it.itemId)
            {
                R.id.nav_abonents -> loadFragmentFromDrawer(abonentsFragment)
                R.id.nav_plans -> loadFragmentFromDrawer(plansFragment)
                R.id.nav_services -> loadFragmentFromDrawer(servicesFragment)
            }
            drawerLayout.closeDrawers()
            true
        }
    }
    //----------------------------------------------------------------------------------------------
    //Установить слушателей SharedViewModel
    private fun setSharedViewModelListeners()
    {
        val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.inputString.observe(this, {
            Log.d("TAG", "String")
            addFragment(AbonentFragment())
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        })
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
        //Если открыт какой-либо из фрагментов AbonentFragment, PlanFragment или ServiceFragment
        //то закрыть их
        when(currentFragment)
        {
            is AbonentFragment -> backToFragment(abonentsFragment)
            else -> super.onBackPressed()
        }
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu : Menu) : Boolean
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    //----------------------------------------------------------------------------------------------
    override fun onStop()
    {
        Log.d("TAG", "MainActivity - onStop")
        //Убирать фрагменты, чтобы при включении тёмной темы не было наложения
        supportFragmentManager.beginTransaction().remove(abonentsFragment).remove(servicesFragment).remove(plansFragment).commit()
        super.onStop()
    }
    //----------------------------------------------------------------------------------------------
    override fun onDestroy()
    {
        Log.d("TAG", "MainActivity - onDestroy")
        //Уничтожить объект БД
        AbonentsDatabase.destroyDataBase()
        super.onDestroy()
    }
    //----------------------------------------------------------------------------------------------
    //Загрузить фрагмент из NavigationDrawer
    private fun loadFragmentFromDrawer(fragment: Fragment)
    {
        //Загрузить только если мы хотим переключиться на другой фрагмент
        if (fragment != currentFragment)
        {
            supportFragmentManager.beginTransaction().show(fragment).hide(currentFragment).commit()
            currentFragment = fragment
        }
    }
    //----------------------------------------------------------------------------------------------
    private fun addFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction().hide(currentFragment).add(R.id.nav_host_fragment ,fragment).commit()
        currentFragment = fragment
    }
    //----------------------------------------------------------------------------------------------
    private fun backToFragment(showFragment : Fragment)
    {
        supportFragmentManager.beginTransaction().remove(currentFragment).show(showFragment).commit()
        currentFragment = abonentsFragment
        toolbar.setNavigationIcon(R.drawable.ic_dehaze)
    }
    //----------------------------------------------------------------------------------------------
}