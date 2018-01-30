package pl.karass32.xfolio.extensions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import pl.karass32.xfolio.R

/**
 * Created by karas on 15.01.2018.
 */

fun FragmentTransaction.replace(fragment : Fragment) {
    replace(R.id.contentFrame, fragment).commit()
}