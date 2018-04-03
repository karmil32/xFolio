package pl.karass32.xfolio.ui.preferences

import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import pl.karass32.xfolio.R
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout



class PrefActivity : PreferenceActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val root = findViewById<View>(android.R.id.list).parent.parent.parent as LinearLayout
        val bar = LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false) as AppBarLayout
        root.addView(bar, 0) // insert at top

        val toolbar = bar.getChildAt(0) as Toolbar
        toolbar.title = getString(R.string.action_settings)
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onBuildHeaders(target: MutableList<Header>?) {
        loadHeadersFromResource(R.xml.headers_preference, target)
    }

    override fun isValidFragment(fragmentName: String?): Boolean {
        return CoinListsPrefFragment::class.java.name == fragmentName
    }
}