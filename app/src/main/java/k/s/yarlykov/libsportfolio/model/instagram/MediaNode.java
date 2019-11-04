package k.s.yarlykov.libsportfolio.model.instagram;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaNode {

    @SerializedName("data")
    @Expose
    public List<MediaAlbum> data = null;
    @SerializedName("paging")
    @Expose
    public Paging paging;

}