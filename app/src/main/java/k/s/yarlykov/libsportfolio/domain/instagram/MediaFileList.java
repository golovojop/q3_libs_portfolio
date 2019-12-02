package k.s.yarlykov.libsportfolio.domain.instagram;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaFileList {

    @SerializedName("data")
    @Expose
    public List<MediaFile> data = null;

}
