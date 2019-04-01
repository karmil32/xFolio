package pl.karass32.xfolio.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import pl.karass32.xfolio.R

/**
 * Created by karas on 15.01.2018.
 */

fun androidx.fragment.app.FragmentTransaction.replace(fragment : androidx.fragment.app.Fragment) {
    replace(R.id.contentFrame, fragment).commit()
}