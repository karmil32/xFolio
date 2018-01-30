package pl.karass32.xfolio.base

/**
 * Created by karas on 17.01.2018.
 */
interface BaseView<in T> {
    fun setPresenter(presenter: T)
}