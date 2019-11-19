package k.s.yarlykov.libsportfolio.domain.instagram;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paging {

    @SerializedName("cursors")
    @Expose
    public Cursors cursors;

}
