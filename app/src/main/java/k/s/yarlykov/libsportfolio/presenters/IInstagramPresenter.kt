package k.s.yarlykov.libsportfolio.presenters

interface IInstagramPresenter {
    fun onViewCreated(appSecret: String)
    fun onResume()
    fun onPause()
    fun onAppCodeReceived(appCode: String)
}