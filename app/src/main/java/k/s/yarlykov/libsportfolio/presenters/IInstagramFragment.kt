package k.s.yarlykov.libsportfolio.presenters

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Только со стратегией SingleStateStrategy все заработало без косяков.
 * Ну или по крайней мере их пока не видно ))
 */
@StateStrategyType(SingleStateStrategy::class)
interface IInstagramFragment : MvpView {

    fun onFrontProgressBar()
    fun onFrontWebView()
    fun onFrontRecyclerView()

    fun showAuthWebPage()
    fun updateMediaContent(uri : List<String>)
}