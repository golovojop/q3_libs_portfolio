package k.s.yarlykov.libsportfolio.contracts

interface TabContract {
    interface ITabFragment : BaseContract.IView
    interface ITabPresenter : BaseContract.IPresenter<ITabFragment>
}