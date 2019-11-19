package k.s.yarlykov.libsportfolio.presenters

interface IInstagramPresenter {
    fun onViewCreated(appSecret: String)
    fun onAppCodeReceived(appCode: String)
}