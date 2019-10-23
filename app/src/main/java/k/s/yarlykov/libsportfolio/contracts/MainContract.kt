package k.s.yarlykov.libsportfolio.contracts

interface MainContract {
    interface IMainView : BaseContract.IView
    interface IMainPresenter : BaseContract.IPresenter<IMainView> {
        fun onFabTapped()
    }
}