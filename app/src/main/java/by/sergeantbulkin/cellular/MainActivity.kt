package by.sergeantbulkin.cellular

import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity()
{
    //----------------------------------------------------------------------------------------------
    private lateinit var appBarConfiguration : AppBarConfiguration
    //----------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Установить AppBar
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Настроить NavigationDrawer
        setUpNavigationDrawer()
    }
    //----------------------------------------------------------------------------------------------
    //Настроить NavigationDrawer
    private fun setUpNavigationDrawer()
    {
        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_abonents, R.id.nav_plans, R.id.nav_services), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    //----------------------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu : Menu) : Boolean
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    //----------------------------------------------------------------------------------------------
    override fun onSupportNavigateUp() : Boolean
    {
        Log.d("TAG", "onSupportNavigateUp")
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    //----------------------------------------------------------------------------------------------

}