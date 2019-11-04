package k.s.yarlykov.libsportfolio.model.instagram;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cursors {

    @SerializedName("before")
    @Expose
    public String before;
    @SerializedName("after")
    @Expose
    public String after;

}