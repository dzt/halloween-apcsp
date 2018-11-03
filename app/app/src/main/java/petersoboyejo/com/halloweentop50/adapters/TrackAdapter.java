package petersoboyejo.com.halloweentop50.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import petersoboyejo.com.halloweentop50.MainActivity;
import petersoboyejo.com.halloweentop50.R;
import petersoboyejo.com.halloweentop50.TrackActivity;
import petersoboyejo.com.halloweentop50.models.Track;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private List<Track> tracks;
    private Context context;
    private MainActivity activity;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView trackName, artist, rank;
        RelativeLayout relativeLayout;
        ImageView artwork;
        Button open;

        public ViewHolder(View view) {
            super(view);
            trackName = (TextView) view.findViewById(R.id.trackName);
            artwork = (ImageView) view.findViewById(R.id.artwork);
            artist = (TextView) view.findViewById(R.id.artist);
            rank = (TextView) view.findViewById(R.id.rank);
            open = (Button) view.findViewById(R.id.open);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.item_track);
            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

            Intent intent = new Intent(context, TrackActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("TRACK_CHOICE", tracks.get(clickedPosition));
            intent.putExtra("PLAYLISTS", TrackAdapter.this.activity.playlists);
            TrackAdapter.this.context.startActivity(intent);

        }

    }


    public TrackAdapter(List<Track> tracks, Context context, MainActivity activity) {
        this.tracks = tracks;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tracks_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Track t = tracks.get(position);

        /* Give UI elements values */
        holder.trackName.setText(t.getName());
        holder.artist.setText(t.getArtist());
        holder.rank.setText(Integer.toString(t.getDups()));
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load(t.getArtwork()).into(holder.artwork);

        holder.open.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com/track/" + t.getId()));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TrackAdapter.this.context.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }
}