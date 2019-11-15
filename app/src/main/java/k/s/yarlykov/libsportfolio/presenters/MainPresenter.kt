package k.s.yarlykov.libsportfolio.presenters

class MainPresenter(private val activity : IMainView) {

    fun onFabTapped() {
        activity.onClickFabHandler()
    }
}