package k.s.yarlykov.libsportfolio.model

import android.graphics.Bitmap

data class Photo(val id: Int, val bitmap: Bitmap, val likes : Int = 0, val isFavourite : Boolean = false)