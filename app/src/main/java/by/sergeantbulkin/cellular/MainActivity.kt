package by.sergeantbulkin.cellular

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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
    private lateinit var sharedViewModel: SharedViewModel
    //----------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState : Bundle?)
    {
        Log.d("TAG", "MainActivity - onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Настроить NavigationDrawer
        setUpNavigationDrawer()

        //Инициализация фрагментов
        initFragments()

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
        Log.d("TAG", "Фрагментов initFragments - ${supportFragmentManager.fragments.size}")
        supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, abonentsFragment).hide(abonentsFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, plansFragment).hide(plansFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, servicesFragment).commit()

        currentFragment = servicesFragment
        loadFragmentFromDrawer(abonentsFragment, resources.getString(R.string.menu_abonents))
    }
    //----------------------------------------------------------------------------------------------
    //Настроить NavigationDrawer
    private fun setUpNavigationDrawer()
    {
        toolbar = findViewById(R.id.toolbar)
        setTitle(resources.getString(R.string.menu_abonents))
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        //Установить AppBar
        setSupportActionBar(toolbar)
        //Включить отображение, чтобы обработать нажатие
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //NavigationIcon click listener
        toolbar.setNavigationOnClickListener {
            when(currentFragment)
            {
                is AbonentFragment -> backToFragment(abonentsFragment, resources.getString(R.string.menu_abonents))
                else -> drawerLayout.open()
            }
        }
        //Обработка выбора пунктов из NavigationDrawer
        navView.setNavigationItemSelectedListener {
            navView.setCheckedItem(it)
            when (it.itemId)
            {
                R.id.nav_abonents -> loadFragmentFromDrawer(abonentsFragment, it.title.toString())
                R.id.nav_plans -> loadFragmentFromDrawer(plansFragment, it.title.toString())
                R.id.nav_services -> loadFragmentFromDrawer(servicesFragment, it.title.toString())
            }
            drawerLayout.closeDrawers()
            true
        }
    }
    //----------------------------------------------------------------------------------------------
    //Установить слушателей SharedViewModel
    private fun setSharedViewModelListeners()
    {
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.temp.observe(this, {
            addFragment(AbonentFragment(), resources.getString(R.string.label_abonent_add))
        })
        sharedViewModel.abonent.observe(this, {
            addFragment(AbonentFragment.newInstance(it), resources.getString(R.string.label_abonent_edit))
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
            is AbonentFragment -> backToFragment(abonentsFragment, resources.getString(R.string.menu_abonents))
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
    override fun onStart()
    {
        Log.d("TAG", "MainActivity - onStart")
        super.onStart()
    }

    //----------------------------------------------------------------------------------------------
    override fun onResume()
    {
        Log.d("TAG", "MainActivity - onResume")
        super.onResume()
    }

    //----------------------------------------------------------------------------------------------
    override fun onPause()
    {
        Log.d("TAG", "MainActivity - onPause")
        super.onPause()
    }

    //----------------------------------------------------------------------------------------------
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle)
    {
        Log.d("TAG", "MainActivity - onSaveInstanceState2")
        super.onSaveInstanceState(outState, outPersistentState)
    }

    //----------------------------------------------------------------------------------------------
    override fun onRestoreInstanceState(savedInstanceState: Bundle)
    {
        Log.d("TAG", "MainActivity - onRestoreInstanceState1")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?)
    {
        Log.d("TAG", "MainActivity - onStart")
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    //----------------------------------------------------------------------------------------------
    override fun onStop()
    {
        Log.d("TAG", "MainActivity - onStop")
        //Убирать фрагменты, чтобы при включении тёмной темы не было наложения
        supportFragmentManager.beginTransaction().remove(abonentsFragment).remove(servicesFragment).remove(plansFragment).remove(currentFragment).commit()
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
    private fun loadFragmentFromDrawer(fragment: Fragment, title: String)
    {
        //Загрузить только если мы хотим переключиться на другой фрагмент
        if (fragment != currentFragment)
        {
            supportFragmentManager.beginTransaction().show(fragment).hide(currentFragment).commit()
            currentFragment = fragment
            setTitle(title)
        }
    }
    //----------------------------------------------------------------------------------------------
    private fun addFragment(fragment: Fragment, title : String)
    {
        supportFragmentManager.beginTransaction().hide(currentFragment).add(R.id.nav_host_fragment ,fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
        currentFragment = fragment
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setTitle(title)
    }
    //----------------------------------------------------------------------------------------------
    private fun backToFragment(showFragment : Fragment, title: String)
    {
        supportFragmentManager.beginTransaction().remove(currentFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).show(showFragment).commit()
        currentFragment = abonentsFragment
        toolbar.setNavigationIcon(R.drawable.ic_dehaze)
        setTitle(title)
    }
    //----------------------------------------------------------------------------------------------
    private fun setTitle(title : String)
    {
        toolbar.title = title
    }
    //----------------------------------------------------------------------------------------------
}