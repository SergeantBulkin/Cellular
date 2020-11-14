package by.sergeantbulkin.cellular

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import by.sergeantbulkin.cellular.room.AbonentsDatabase
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
        loadFragment(abonentsFragment)
    }
    //----------------------------------------------------------------------------------------------
    //Настроить NavigationDrawer
    private fun setUpNavigationDrawer()
    {
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        //Установить AppBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {drawerLayout.open()}

        navView.setNavigationItemSelectedListener {
            navView.setCheckedItem(it)
            when (it.itemId)
            {
                R.id.nav_abonents -> loadFragment(abonentsFragment)
                R.id.nav_plans -> loadFragment(plansFragment)
                R.id.nav_services -> loadFragment(servicesFragment)
            }
            drawerLayout.closeDrawers()
            true
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
    override fun onDestroy()
    {
        Log.d("TAG", "MainActivity - onDestroy")
        AbonentsDatabase.destroyDataBase()
        //DisposableManager.dispose()
        super.onDestroy()
    }
    //----------------------------------------------------------------------------------------------
    private fun loadFragment(fragment: Fragment)
    {
        if (fragment != currentFragment)
        {
            supportFragmentManager.beginTransaction().show(fragment).hide(currentFragment).commit()
            currentFragment = fragment
        }
    }
    //----------------------------------------------------------------------------------------------
}