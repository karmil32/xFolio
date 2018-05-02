package pl.karass32.xfolio.ui.preferences

import android.os.Bundle
import pl.karass32.xfolio.base.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, PrefFragment()).commit()
    }
}