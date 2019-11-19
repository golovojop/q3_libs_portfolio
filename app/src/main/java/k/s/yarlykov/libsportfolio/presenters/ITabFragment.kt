package k.s.yarlykov.libsportfolio.presenters

import k.s.yarlykov.libsportfolio.domain.room.Photo

interface ITabFragment {
    fun updateContent(photos: List<Photo>)
}