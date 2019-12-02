package k.s.yarlykov.libsportfolio.domain.room

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Photo (
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var url: String,
    var favorite: Boolean,
    var likes: Int,

    @Ignore
    var bitmap: Bitmap ?
) {
    constructor() : this(0, "", false, 0, null)
}