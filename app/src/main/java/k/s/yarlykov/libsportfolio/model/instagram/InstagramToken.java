package k.s.yarlykov.libsportfolio.model.instagram;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstagramToken {

    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("user_id")
    @Expose
    public String userId;
}
