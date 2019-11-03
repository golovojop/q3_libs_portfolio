package k.s.yarlykov.libsportfolio.ui.fragments

import android.os.Bundle
import k.s.yarlykov.libsportfolio.CONTENT
import k.s.yarlykov.libsportfolio.KEY_BUNDLE

class GalleryTabFragment : TabFragment() {

    companion object {

        fun create(bundle: Bundle?): GalleryTabFragment {
            return GalleryTabFragment().apply {
                arguments = Bundle().apply {
                    putBundle(KEY_BUNDLE, bundle)
                }
            }
        }
    }

    override val contentType = CONTENT.GALLERY
}