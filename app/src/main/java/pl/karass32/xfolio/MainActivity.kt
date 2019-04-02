package pl.karass32.xfolio

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import pl.karass32.xfolio.adapter.CoinRvAdapter
import pl.karass32.xfolio.base.BaseActivity


class MainActivity : BaseActivity(), CoinRvAdapter.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.navHostFragment)
        drawerLayout.navView.setupWithNavController(navController)
    }

    fun setToggle(toolbar: Toolbar) {
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun lockDrawerLayout(enabled: Boolean) {
        drawerLayout.setDrawerLockMode(if (enabled) androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED else androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onItemClick(coinSymbol: String) {
        val args = Bundle()
        args.putString("COIN_SYMBOL", coinSymbol)
        findNavController(R.id.navHostFragment).navigate(R.id.coinListToDetails, args)
    }
}