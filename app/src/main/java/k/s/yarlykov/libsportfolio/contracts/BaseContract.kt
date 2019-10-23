package k.s.yarlykov.libsportfolio.contracts

interface BaseContract {
    interface IView
    interface IPresenter<V : IView> {
        fun bind(view : V)
        fun unbind()
    }
}