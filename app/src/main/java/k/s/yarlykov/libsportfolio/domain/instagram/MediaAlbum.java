package k.s.yarlykov.libsportfolio.domain.instagram;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaAlbum {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("media_url")
    @Expose
    public String mediaUrl;
    @SerializedName("permalink")
    @Expose
    public String permalink;

}