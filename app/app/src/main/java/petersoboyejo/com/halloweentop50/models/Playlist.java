package petersoboyejo.com.halloweentop50.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Playlist implements Serializable {

    private String id;
    private String name;
    private String image;
    private String popularity;
    private String author;
    private int trackCount, followers;

    public Playlist(String json) {

        try {

            JSONObject playlistObject = new JSONObject(json);

            this.id = playlistObject.optString("id");
            this.name = playlistObject.optString("name");
            this.image = playlistObject.optString("image");
            this.author = playlistObject.optString("author");
            this.popularity = playlistObject.optString("popularity");
            this.trackCount = playlistObject.optInt("trackCount");
            this.followers = playlistObject.optInt("followers");


        } catch (JSONException e) {

            e.printStackTrace();

        }

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getAuthor() {
        return author;
    }

    public String getPopularity() {
        return popularity;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public int getFollowers() {
        return followers;
    }
}
