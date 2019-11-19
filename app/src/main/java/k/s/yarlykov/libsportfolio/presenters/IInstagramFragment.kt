package k.s.yarlykov.libsportfolio.presenters

interface IInstagramFragment {

    fun onFrontProgressBar()
    fun onFrontWebView()
    fun onFrontRecyclerView()

    fun showAuthWebPage()
    fun updateMediaContent(uri : List<String>)
}