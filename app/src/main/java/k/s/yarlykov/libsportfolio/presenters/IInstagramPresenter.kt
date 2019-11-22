package k.s.yarlykov.libsportfolio.presenters

interface IInstagramPresenter {
    fun onAppCodeReceived(applicationCode: String)
    fun onViewCreated(appSecret: String)
    fun onResume()
    fun onPause()
}