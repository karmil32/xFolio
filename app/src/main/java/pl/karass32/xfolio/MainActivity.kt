package pl.karass32.xfolio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import pl.karass32.xfolio.ui.coinlist.CoinListFragment
import pl.karass32.xfolio.ui.portfolio.PortfolioFragment
import pl.karass32.xfolio.ui.WatchlistFragment
import pl.karass32.xfolio.extension.replace
import pl.karass32.xfolio.ui.preferences.PrefActivity

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nav_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            navigate(R.id.nav_all_coins)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        navigate(item.itemId)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("CommitTransaction")
    private fun navigate(itemId : Int) {
        val transaction = supportFragmentManager.beginTransaction()
        when (itemId) {
            R.id.nav_all_coins -> {
                transaction.replace(CoinListFragment())
            }
            R.id.nav_watchlist -> {
                transaction.replace(WatchlistFragment())
            }
            R.id.nav_portfolio -> {
                transaction.replace(PortfolioFragment())
            }
            R.id.nav_settings -> {
                val intent = Intent(this, PrefActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_about -> {

            }
        }
    }
}
