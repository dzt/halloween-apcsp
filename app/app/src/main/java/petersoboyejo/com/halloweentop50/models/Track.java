package petersoboyejo.com.halloweentop50.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Track implements Serializable {

    private String id, name, artwork, artist;
    private int dups, popularity;
    private ArrayList<String> playlistIDs = new ArrayList<String>();

    public Track (String json) {
        try {

            JSONObject playlistObject = new JSONObject(json);

            this.id = playlistObject.optString("id");
            this.name = playlistObject.optString("name");
            this.artwork = playlistObject.optString("artwork");
            this.artist = playlistObject.optString("artist");
            this.dups = playlistObject.optInt("dups");
            this.popularity = playlistObject.optInt("popularity");

            JSONArray playlistList = playlistObject.optJSONArray("playlists");
            for (int i = 0; i < playlistList.length(); i++) {
                String str = playlistList.getString(i);
                if (str != null) {
                    this.playlistIDs.add(str);
                }
            }



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

    public String getArtwork() {
        return artwork;
    }

    public String getArtist() {
        return artist;
    }

    public int getDups() {
        return dups;
    }

    public ArrayList<String> getPlaylistIDs() {
        return playlistIDs;
    }

    public int getPopularity() {
        return popularity;
    }
}
