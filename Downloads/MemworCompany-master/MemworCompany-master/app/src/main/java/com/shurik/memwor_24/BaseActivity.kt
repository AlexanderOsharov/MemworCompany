//package com.shurik.memwor_24
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.MenuItem
//import android.view.View
//import androidx.appcompat.app.ActionBarDrawerToggle
//import androidx.appcompat.widget.Toolbar
//import androidx.core.view.GravityCompat
//import androidx.drawerlayout.widget.DrawerLayout
//import com.google.android.material.navigation.NavigationView
//import com.shurik.memwor_24.browser.BrowserActivity
//import com.shurik.memwor_24.memwor.MemworActivity
//import com.shurik.memwor_24.pizza_planet.activities.PizzaPlanetActivity
//import com.shurik.memwor_24.pumpwimo.activities.PumpWiMoActivity
//
//// класс, позволяющий реализовать все методы drawer navigation
//abstract class BaseActivity : AppCompatActivity() {
//
//    // drawer navigation
//    protected lateinit var drawerLayout: DrawerLayout
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(getLayoutId())
//
//        drawerLayout = findViewById(R.id.drawer_layout)
//        val navigationView: NavigationView = findViewById(R.id.navigation_view)
//        val toolBar: Toolbar = findViewById(R.id.toolBar)
//
//        setSupportActionBar(toolBar)
//
//        val toggle = ActionBarDrawerToggle(
//            this,
//            drawerLayout,
//            toolBar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
////        navigationView.setNavigationItemSelectedListener {
////            when (it.itemId) {
////                R.id.memwor -> {
////                    startActivityAndFinish(MemworActivity::class.java)
////                    drawerLayout.closeDrawers()
////                }
////                R.id.browser -> {
////                    startActivityAndFinish(BrowserActivity::class.java)
////                    drawerLayout.closeDrawers()
////                }
////                R.id.pumpwimo -> {
////                    startActivityAndFinish(PumpWiMoActivity::class.java)
////                    drawerLayout.closeDrawers()
////                }
////                R.id.pizza_planet -> {
////                    startActivityAndFinish(PizzaPlanetActivity::class.java)
////                    drawerLayout.closeDrawers()
////                }
////            }
////            true
////        }
////    }
//
//    abstract fun getLayoutId(): Int
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val toggle = ActionBarDrawerToggle(
//            this,
//            drawerLayout,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//        if (toggle.onOptionsItemSelected(item)) {
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//
//    protected fun startActivityAndFinish(activityClass: Class<*>) {
//        if (this::class.java != activityClass) {
//            val intent = Intent(this, activityClass)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)  // REPLACE THIS FLAG
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                drawerLayout.closeDrawer(GravityCompat.START, true)
//                drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
//                    override fun onDrawerClosed(drawerView: View) {
//                        drawerLayout.removeDrawerListener(this)
//                        startActivity(intent)
//                        finish()
//                    }
//                })
//            } else {
//                startActivity(intent)
//                finish()
//            }
//        }
//    }
//
//    protected fun startActivityAndFinishAffinity(activityClass: Class<*>) {
//        if (this::class.java != activityClass) {
//            startActivity(Intent(this, activityClass))
//            finishAffinity()
//        }
//    }
//
//    protected fun startActivityIfNeeded(activityClass: Class<*>) {
//        if (this::class.java != activityClass) {
//            startActivity(Intent(this, activityClass))
//        }
//    }
//}