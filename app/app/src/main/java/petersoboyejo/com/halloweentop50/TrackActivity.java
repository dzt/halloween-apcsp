package petersoboyejo.com.halloweentop50;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import petersoboyejo.com.halloweentop50.adapters.HorizontalAdapter;
import petersoboyejo.com.halloweentop50.models.Playlist;
import petersoboyejo.com.halloweentop50.models.Track;

public class TrackActivity extends AppCompatActivity {

    private Track currentTrack;
    private TextView trackName, trackArtist, popularity;
    private ImageView artwork;

    private ArrayList<Playlist> playlists = new ArrayList<Playlist>();

    RecyclerView recyclerViewVertical;
    HorizontalAdapter horizontalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentTrack = (Track) getIntent().getSerializableExtra("TRACK_CHOICE");
            // TODO: Have it find the playlists that match whats under the track
            playlists = (ArrayList<Playlist>) getIntent().getSerializableExtra("PLAYLISTS");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.init();

    }

    private void init() {


        artwork = findViewById(R.id.artwrok_detail);
        trackName = findViewById(R.id.trackName_detail);
        trackArtist = findViewById(R.id.artistName_detail);
        popularity = findViewById(R.id.popularity);

        Picasso.get().load(currentTrack.getArtwork()).into(artwork);
        trackName.setText(currentTrack.getName());
        trackArtist.setText(currentTrack.getArtist());
        popularity.setText(currentTrack.getPopularity() + "% Popularity on Spotify");

        recyclerViewVertical = (RecyclerView) findViewById(R.id.vertical_recycler_view);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        horizontalAdapter = new HorizontalAdapter(TrackActivity.this, playlists);
        recyclerViewVertical.setAdapter(horizontalAdapter);

        horizontalAdapter.notifyDataSetChanged();


    }
}
