package k.s.yarlykov.libsportfolio.presenters

interface IInstagramFragment {

    fun onFrontProgressBar()
    fun onFrontWebView()
    fun onFrontRecyclerView()

    fun showAuthWebPage(authRequestUri : String)
    fun updateMediaContent(uri : List<String>)
}