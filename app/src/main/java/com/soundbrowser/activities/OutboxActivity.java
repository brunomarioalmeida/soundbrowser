package com.soundbrowser.activities;

import info.androidhive.customlistviewvolley.CustomListAdapter;
import info.androidhive.customlistviewvolley.Movie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.soundbrowser.R;
import com.soundbrowser.controller.AppController;

public class OutboxActivity extends Activity
{
	private static final String TAG = OutboxActivity.class.getSimpleName();
	
	// Movies json url
    private static final String url = "http://api.androidhive.info/json/movies.json";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;
 
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
        // Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_1, countries
        );
//        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                SharedPreferences preferences = getSharedPreferences("session",getApplicationContext().MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("searchStr", (String) ((TextView)view).getText());
//                editor.commit();
//            }
//        });
//        textView.setAdapter(adapter);
        
//        ImageView v = (ImageView)findViewById(R.id.sss); //new ImageView(this);
////        v.setImageURI(Uri.parse("http://img0.rtp.pt/EPG/radio/imagens/1070_7907_60189.png"));
//        v.setImageResource(R.drawable.i_1070_7907_60189);
//        
//        v.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions
////        v.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
////            LayoutParams.WRAP_CONTENT));
//
//        // Add the ImageView to the layout and set the layout as the content view
//        LinearLayout mLinearLayout = new LinearLayout(this);
//        mLinearLayout.addView(v);
//        setContentView(mLinearLayout);
        
        setContentView(R.layout.outbox_list);
//        setContentView(R.layout.activity_main);
        
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);
 
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
 
        // changing action bar color
//        getActionBar().setBackgroundDrawable(
//                new ColorDrawable(Color.parseColor("#1b1b1b")));
 
        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
 
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
 
                                JSONObject obj = response.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setTitle(obj.getString("title"));
                                movie.setThumbnailUrl(obj.getString("image"));
                                movie.setRating(((Number) obj.get("rating"))
                                        .doubleValue());
                                movie.setYear(obj.getInt("releaseYear"));
 
                                // Genre is json array
                                JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);
 
                                // adding movie to movies array
                                movieList.add(movie);
 
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
 
                        }
 
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();
 
                    }
                });
 
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }        
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }
 
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}