package k.s.yarlykov.libsportfolio

import android.util.Log

enum class CONTENT {
    GALLERY, FAVORITES
}

const val KEY_BUNDLE = "KEY_BUNDLE"
const val KEY_LAYOUT_ID = "KEY_LAYOUT_ID"

fun logIt(message : String) {
    Log.e("APP_TAG", message)
}