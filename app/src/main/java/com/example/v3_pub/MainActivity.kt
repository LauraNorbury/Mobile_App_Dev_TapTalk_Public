package com.example.v3_pub

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

//main activity is the only non fragment. I use it to do the navigation initialisation

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //some inital set up

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        //i have to do the design of the nav bar programatically because my theme is overriding the
        //colours i want it to pick.

        navView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        //this part is setting the icon colours and what the look like active

        val iconColorStateList = ContextCompat.getColorStateList(this, R.color.nav_item_color_state)
        val textColorStateList = ContextCompat.getColorStateList(this, R.color.nav_item_text_color_state)

        //using the support files to get the colours

        navView.itemIconTintList = iconColorStateList
        navView.itemTextColor = textColorStateList


        //initialise the nav host - i wrote about this in detail in my document

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //  BottomNavigationView with NavController
        navView.setupWithNavController(navController)
        navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.pubsFragment, R.id.profile_screen, R.id.mapsFragment, R.id.favouritesFragment),
            drawerLayout
        )

        //ActionBarDrawerToggle - DrawerLayout is the actual layout itself. Toggle is to show it or not
        toggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Listener to show/hide the BottomNavigationView and burger/back button based on destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashscreen, R.id.loginFragment, R.id.registrationFragment -> {
                    navView.visibility = View.GONE
                    // Hide burger button
                    toggle.isDrawerIndicatorEnabled = false
                }
                R.id.homeFragment, R.id.pubsFragment, R.id.profile_screen, R.id.mapsFragment, R.id.favouritesFragment -> {
                    navView.visibility = View.VISIBLE
                    // Show burger button
                    toggle.isDrawerIndicatorEnabled = true
                    toggle.syncState()
                }
                else -> {
                    navView.visibility = View.VISIBLE
                    // Show back button and hide burger button
                    toggle.isDrawerIndicatorEnabled = false
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
            }
        }
    }

    //lastly more navigation

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
