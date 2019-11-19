package k.s.yarlykov.libsportfolio.presenters

import k.s.yarlykov.libsportfolio.model.Photo

interface ITabFragment {
    fun updateContent(photos: List<Photo>)
}