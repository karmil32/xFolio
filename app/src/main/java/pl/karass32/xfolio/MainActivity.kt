package pl.karass32.xfolio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import pl.karass32.xfolio.base.BaseActivity
import pl.karass32.xfolio.ui.coinlist.CoinListFragment
import pl.karass32.xfolio.ui.portfolio.PortfolioFragment
import pl.karass32.xfolio.ui.coinlist.FavoritesListFragment
import pl.karass32.xfolio.ui.preferences.SettingsActivity

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            when(preferences.getAutoOpen()) {
                CoinListFragment.TAG -> navigate(R.id.nav_all_coins)
                FavoritesListFragment.TAG -> navigate(R.id.nav_watchlist)
                PortfolioFragment.TAG -> navigate(R.id.nav_portfolio)
            }
        }
    }

    fun setToggle(toolbar: Toolbar) {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun lockDrawerLayout(enabled: Boolean) {
        drawer_layout.setDrawerLockMode(if (enabled) DrawerLayout.LOCK_MODE_LOCKED_CLOSED else DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigate(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("CommitTransaction")
    private fun navigate(itemId: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        when (itemId) {
            R.id.nav_all_coins -> {
                transaction.replace(R.id.contentFrame, CoinListFragment()).commit()
            }
            R.id.nav_watchlist -> {
                transaction.replace(R.id.contentFrame, FavoritesListFragment()).commit()
            }
            R.id.nav_portfolio -> {
                transaction.replace(R.id.contentFrame, PortfolioFragment()).commit()
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_about -> {}
        }
    }
}
