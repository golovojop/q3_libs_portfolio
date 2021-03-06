package k.s.yarlykov.libsportfolio.presenters

import k.s.yarlykov.libsportfolio.CONTENT

interface ITabPresenter {
    fun onViewCreated(content: CONTENT)
    fun onDestroyView()
    fun onClickLikeButton(position: Int)
    fun onClickFavoriteButton(position: Int)

}