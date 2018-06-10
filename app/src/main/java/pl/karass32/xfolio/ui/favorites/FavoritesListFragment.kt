package pl.karass32.xfolio.ui.favorites

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import pl.karass32.xfolio.R
import pl.karass32.xfolio.base.BaseCoinListFragment

/**
 * Created by karas on 14.01.2018.
 */
class FavoritesListFragment : BaseCoinListFragment() {

    companion object {
        const val TAG = "FavoritesListFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(FavoritesListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.favorites_list_fragment, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initToolbar() {
        super.initToolbar()
        mainActivity.supportActionBar?.title = getString(R.string.nav_favorites)
    }
}