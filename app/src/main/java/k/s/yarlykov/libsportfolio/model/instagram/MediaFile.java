package k.s.yarlykov.libsportfolio.model.instagram;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaFile {

    @SerializedName("permalink")
    @Expose
    public String permalink;
    @SerializedName("id")
    @Expose
    public String id;
}
