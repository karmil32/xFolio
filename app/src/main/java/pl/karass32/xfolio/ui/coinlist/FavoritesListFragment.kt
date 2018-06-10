package pl.karass32.xfolio.ui.coinlist

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.coin_list_fragment.view.*
import pl.karass32.xfolio.R
import pl.karass32.xfolio.base.BaseCoinListFragment

/**
 * Created by karas on 14.01.2018.
 */
class FavoritesListFragment : BaseCoinListFragment() {

    companion object {
        const val TAG = "FavoritesListFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.favorites_list_fragment, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initToolbar() {
        super.initToolbar()
        mainActivity.supportActionBar?.title = getString(R.string.nav_favorites)
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.getFavoritesList()?.observe(this, Observer { coinList ->
            coinList?.let {
                showList(it)
                mView.rvError.visibility = View.GONE
            }
        })
    }
}