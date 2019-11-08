package k.s.yarlykov.libsportfolio.model.instagram;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaFileList {

    @SerializedName("data")
    @Expose
    public List<MediaFile> data = null;

}
