package k.s.yarlykov.libsportfolio

import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class CONTENT : Parcelable {
    GALLERY, FAVOURITES
}

const val KEY_BUNDLE = "KEY_BUNDLE"
const val KEY_LAYOUT_ID = "KEY_LAYOUT_ID"
const val KEY_CATEGORY = "KEY_CATEGORY"

fun logIt(message : String) {
    Log.e("APP_TAG", message)
}