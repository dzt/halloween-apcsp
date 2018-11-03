package petersoboyejo.com.halloweentop50.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import petersoboyejo.com.halloweentop50.R;
import petersoboyejo.com.halloweentop50.models.Playlist;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private Context context;
    private List<Playlist> dataList;

    public HorizontalAdapter(Context context, List<Playlist> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_list_row, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(HorizontalAdapter.ViewHolder holder, int position) {

        Playlist playlist = dataList.get(position);
        Picasso.get().load(playlist.getImage()).into(holder.playlistImage);
        holder.playlistName.setText(playlist.getName());
        holder.trackCount.setText(Integer.toString(playlist.getTrackCount()));
        holder.followers.setText(Integer.toString(playlist.getFollowers()));

    }

    @Override
    public int getItemCount() {
        return (dataList!=null ? dataList.size() : 0);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView playlistImage;

        TextView playlistName;
        TextView trackCount;
        TextView followers;

        CardView cardView;

        ViewHolder(View itemView) {

            super(itemView);

            playlistImage = (ImageView) itemView.findViewById(R.id.playlistImage);

            playlistName = (TextView) itemView.findViewById(R.id.playlistName);
            trackCount = (TextView) itemView.findViewById(R.id.trackCount);
            followers = (TextView) itemView.findViewById(R.id.followers);


//            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Toast.makeText(context, dataList.get(clickedPosition).getName(), Toast.LENGTH_LONG).show();
        }
    }
}