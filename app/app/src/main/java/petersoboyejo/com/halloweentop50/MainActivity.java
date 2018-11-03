package petersoboyejo.com.halloweentop50;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import petersoboyejo.com.halloweentop50.adapters.TrackAdapter;
import petersoboyejo.com.halloweentop50.models.Playlist;
import petersoboyejo.com.halloweentop50.models.Track;

public class MainActivity extends AppCompatActivity {

    private String fetchURL = "http://10.0.2.2:3000/fetch";

    public ArrayList<Track> tracks = new ArrayList();
    public ArrayList<Playlist> playlists = new ArrayList();
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Top Tracks");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        trackAdapter = new TrackAdapter(tracks, getApplicationContext(), this);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(trackAdapter);

        this.fetch(fetchURL, "halloween");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                this.search();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void search() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_input_query, null, false);
        final EditText input = viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MainActivity.this.fetch(fetchURL, input.getText().toString());
                dialog.dismiss();

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void fetch(String fetchURL, String query) {

        progressBar.setVisibility(View.VISIBLE);
        tracks.clear();
        playlists.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                fetchURL + "?s=" + query, new Response.Listener < String > () {
            @Override
            public void onResponse(String response) {

                Log.d("RES", response);


                try {

                    JSONObject res = new JSONObject(response);

                    if (res.getBoolean("error") ) {
                        Toast.makeText(MainActivity.this, "Error" + res.get("message"), Toast.LENGTH_SHORT).show();
                    } else {


                        /* Get Tracks */
                        JSONArray tracksList = res.getJSONArray("tracks");
                        for (int i = 0; i < tracksList.length(); i++){
                            tracks.add(new Track(tracksList.getJSONObject(i).toString()));
                        }

                        /* Get Playlists */
                        JSONArray playlistsList = res.getJSONArray("playlists");
                        for (int i = 0; i < playlistsList.length(); i++){
                            playlists.add(new Playlist(playlistsList.getJSONObject(i).toString()));
                        }

                        trackAdapter.notifyDataSetChanged();
                        recyclerView.getViewTreeObserver()
                                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onGlobalLayout() {
                                        progressBar.setVisibility(View.GONE);
                                        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    }
                                });

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(getLocalClassName(), "Error");
                Toast.makeText(MainActivity.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            public HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
