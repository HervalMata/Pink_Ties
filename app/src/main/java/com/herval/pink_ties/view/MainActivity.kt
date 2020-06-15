package com.herval.pink_ties.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.herval.pink_ties.R
import com.herval.pink_ties.data.NavMenuItemsDataBase
import com.herval.pink_ties.domain.NavMenuItem
import com.herval.pink_ties.domain.User
import com.herval.pink_ties.util.NavMenuItemDetailsLookup
import com.herval.pink_ties.util.NavMenuItemKeyProvider
import com.herval.pink_ties.util.NavMenuItemPredicate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_user_logged.*
import kotlinx.android.synthetic.main.nav_header_user_not_logged.*
import kotlinx.android.synthetic.main.nav_menu.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val FRAGMENT_TAG = "frag-tag"
    }

    lateinit var navMenuItems : List<NavMenuItem>
    lateinit var selectNavMenuItems : SelectionTracker<Long>
    lateinit var navMenuItemsLogged : List<NavMenuItem>
    lateinit var selectNavMenuItemsLogged : SelectionTracker<Long>

    private lateinit var appBarConfiguration: AppBarConfiguration

    val user = User("Herval Mata", R.drawable.user, false)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(

        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        initNavMenu(savedInstanceState)
        initFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectNavMenuItems.onSaveInstanceState(outState)
        selectNavMenuItemsLogged.onSaveInstanceState(outState)
    }

    inner class SelectObserverNavMenuItems(
        val callbackRemoveSelection: ()->Unit)
        : SelectionTracker.SelectionObserver<Long>(){
        override fun onItemStateChanged(key: Long, selected: Boolean) {
            super.onItemStateChanged(key, selected)
            if (!selected){
                return
            }
            callbackRemoveSelection()

            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    private fun initNavMenuItems(){
        rv_menu_items.setHasFixedSize(false)
        rv_menu_items.layoutManager = LinearLayoutManager(this)
        rv_menu_items.adapter = NavMenuItemsAdapter(navMenuItems)
        initNavMenuItemsSelection()
    }

    private fun initNavMenuItemsLogged(){
        rv_menu_items_logged.setHasFixedSize(false)
        rv_menu_items_logged.layoutManager = LinearLayoutManager(this)
        rv_menu_items_logged.adapter = NavMenuItemsAdapter(navMenuItemsLogged)
        initNavMenuItemsLoggedSelection()
    }

    private fun initNavMenuItemsSelection(){
        selectNavMenuItems = SelectionTracker.Builder<Long>(
            "id-selected-item", rv_menu_items,
            NavMenuItemKeyProvider(navMenuItems), NavMenuItemDetailsLookup(rv_menu_items),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(NavMenuItemPredicate(this)).build()

        selectNavMenuItems.addObserver(
            SelectObserverNavMenuItems {
                selectNavMenuItemsLogged.selection.filter {
                    selectNavMenuItemsLogged.deselect(it)
                }
            }
        )
        (rv_menu_items.adapter as NavMenuItemsAdapter).selectionTracker = selectNavMenuItems
    }

    private fun initNavMenuItemsLoggedSelection(){
        selectNavMenuItemsLogged = SelectionTracker.Builder<Long>(
            "id-selected-item-logged", rv_menu_items_logged,
            NavMenuItemKeyProvider(navMenuItemsLogged), NavMenuItemDetailsLookup(rv_menu_items_logged),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(NavMenuItemPredicate(this)).build()

        selectNavMenuItemsLogged.addObserver(
            SelectObserverNavMenuItems {
                selectNavMenuItems.selection.filter {
                    selectNavMenuItems.deselect(it)
                }
            }
        )
        (rv_menu_items_logged.adapter as NavMenuItemsAdapter).selectionTracker = selectNavMenuItemsLogged
    }

    private fun fillUserHeaderNavMenu(){
        if (user.status){
            iv_user.setImageResource(user.image)
            tv_user.text = user.name
        }
    }

    private fun showHideNavMenuViews(){
        if (user.status){
            rl_header_user_not_logged.visibility = View.GONE
            fillUserHeaderNavMenu()
        }
        else{
            rl_header_user_logged.visibility = View.GONE
            v_nav_vertical_line.visibility = View.GONE
            rv_menu_items_logged.visibility = View.GONE
        }
    }

    private fun initNavMenu(savedInstanceState: Bundle?){
        val navMenu = NavMenuItemsDataBase(this)
        navMenuItems = navMenu.items
        navMenuItemsLogged = navMenu.itemsLogged
        showHideNavMenuViews()
        initNavMenuItems()
        initNavMenuItemsLogged()
        if (savedInstanceState != null){
            selectNavMenuItems.onRestoreInstanceState(savedInstanceState)
            selectNavMenuItemsLogged.onRestoreInstanceState(savedInstanceState)
        }
        else{
            selectNavMenuItems.select(R.id.item_all_pinks.toLong())
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initFragment() {
        val supFrag = supportFragmentManager
        val fragment = supFrag.findFragmentByTag(FRAGMENT_TAG)
        if (fragment == null) {
            var fragment = getFragment(R.id.item_about.toLong())
        }
        if (fragment != null) {
            replaceFragment(fragment)
        }
    }

    private fun getFragment(fragId: Long): Fragment {
        return when (fragId) {
            R.id.item_about.toLong() -> AboutFragment()
            R.id.item_contact.toLong() -> ContactFragment()
            else -> AboutFragment()
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.fl_fragment_container, fragment, FRAGMENT_TAG
        ).commit()
    }

    fun onItemStateChanged(key: Long, selected: Boolean) {
        val fragment = getFragment(key)
        replaceFragment(fragment)
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    fun updateToolbarTitleInFragment(titleStringId: Int) {
        toolbar.title = getString(titleStringId)
    }
}


