package com.shahruie.zanankoochak;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shahruie.zanankoochak.adater.CustomListAdapter;
import com.shahruie.zanankoochak.adater.MyRecyclerViewAdapter;
import com.shahruie.zanankoochak.app.AppController;
import com.shahruie.zanankoochak.model.Movie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.shahruie.zanankoochak.util.ConnectionDetector;

public class MainActivity2 extends Activity implements MyRecyclerViewAdapter.ItemClickListener {
	// Log tag
	private static final String TAG = MainActivity.class.getSimpleName();

	// Movies json url
	private static final String url = "https://www.aparat.com/etc/api/videoBySearch/text/%DA%A9%D8%A7%D8%B1%D8%AA%D9%88%D9%86+%D8%B2%D9%86%D8%A7%D9%86+%DA%A9%D9%88%DA%86%DA%A9/perpage/10";
	private ProgressDialog pDialog;
	private List<Movie> movieList = new ArrayList<Movie>();
	private List<String> pageList3=new ArrayList<String>();
	private RecyclerView rcv;
	int page_num=0;
	WebView small_web;
	Button retry;
	int video_id=0;
	private MyRecyclerViewAdapter adapter;
	private boolean loading = true;
	FloatingActionButton full_screen;
	int pastVisiblesItems, visibleItemCount, totalItemCount;
	private ConnectionDetector _cd;
	private boolean _isInternetPresent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		retry = (Button) findViewById(R.id.retry);
		full_screen = (FloatingActionButton) findViewById(R.id.full_screen);
		full_screen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String frame = movieList.get(video_id).getFrame();
				Intent myIntent = new Intent(MainActivity2.this, MainActivity.class);
				myIntent.putExtra("frame", frame);
				startActivity(myIntent);
			}
		});
		small_web = (WebView) findViewById(R.id.small_web);
		small_web.setBackgroundColor(Color.parseColor("#000000"));
		rcv = (RecyclerView) findViewById(R.id.list);
		final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		rcv.setLayoutManager(linearLayoutManager);
		adapter = new MyRecyclerViewAdapter(this, movieList);
		adapter.setClickListener(this);
		rcv.setAdapter(adapter);
		rcv.addOnScrollListener(new RecyclerView.OnScrollListener()
		{
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy)
			{
				if(dy > 0) //check for scroll down
				{
					visibleItemCount = linearLayoutManager.getChildCount();
					totalItemCount = linearLayoutManager.getItemCount();
					pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

					if (loading)
					{
						if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
						{
							loading = false;
							Log.v("...", "Last Item Wow !");
							//Do page.. i.e. fetch new data
							if(pageList3.get(page_num).length()>0){
								get_next_page();
								}
						}
					}
				}
			}
		});
		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("بارگذاری...");
		pDialog.setCancelable(false);
		_cd = new ConnectionDetector(getApplicationContext());
		_isInternetPresent = _cd.isConnectingToInternet();
		if (_isInternetPresent) {
			get_video_list();
			if (retry.getVisibility()==View.VISIBLE)
				retry.setVisibility(View.GONE);
		} else {
			retry.setVisibility(View.VISIBLE);
		}
		retry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_cd = new ConnectionDetector(getApplicationContext());
				_isInternetPresent = _cd.isConnectingToInternet();
				if (_isInternetPresent) {
					get_video_list();
					retry.setVisibility(View.GONE);
				}
			}
		});

	}

	private  void get_video_list(){
		showpDialog();

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
				url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d(TAG, response.toString());
				try {
					// Parsing json object response
					// response will be a json object
					JSONArray videobysearch = response.getJSONArray("videobysearch");
					for (int i = 0; i < videobysearch.length(); i++) {

						JSONObject movieobj = (JSONObject) videobysearch.get(i);
						if (!is_repeat(movieobj.getString("title"))){
							Movie movie = new Movie();
							movie.setTitle(movieobj.getString("title"));
							movie.setFrame(movieobj.getString("frame"));
							movie.setTime(movieobj.getString("duration"));
							movie.setVisited(movieobj.getInt("visit_cnt"));
							movie.setThumbnailUrl(movieobj.getString("small_poster"));
							movieList.add(movie);
						}
						JSONObject page_ui = response.getJSONObject("ui");
						if(page_ui.getString("pagingForward").length()>0){
							pageList3.add(page_ui.getString("pagingForward"));
							Log.d("page",pageList3.get(page_num));
							Log.d("page",page_num+"");}}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),
							"Error: " + e.getMessage(),
							Toast.LENGTH_LONG).show();
					Log.d("er",e.getMessage());
				}
				adapter.setItems(movieList);
				adapter.notifyDataSetChanged();
				hidepDialog();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_SHORT).show();
				Log.d("er",error.getMessage());
				// hide the progress dialog
				hidepDialog();
			}
		});

		// Adding request to request queue
		com.shahruie.zanankoochak.app.AppController.getInstance().addToRequestQueue(jsonObjReq);
	}
	private boolean is_repeat(String title) {
		for (int i=0;i<movieList.size();i++){
			if(title.trim().equals(movieList.get(i).getTitle().trim()))
				return true;
		}
		return false;
	}

	private  void get_next_page(){
		showpDialog();

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
				pageList3.get(page_num), null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d(TAG, response.toString());
				try {
					// Parsing json object response
					// response will be a json object
					JSONArray videobysearch = response.getJSONArray("videobysearch");
					for (int i = 0; i < videobysearch.length(); i++) {

						JSONObject movieobj = (JSONObject) videobysearch.get(i);
						if (!is_repeat(movieobj.getString("title"))){
						Movie movie = new Movie();
						movie.setTitle(movieobj.getString("title"));
						movie.setFrame(movieobj.getString("frame"));
						movie.setTime(movieobj.getString("duration"));
						movie.setVisited(movieobj.getInt("visit_cnt"));
						movie.setThumbnailUrl(movieobj.getString("small_poster"));
						movieList.add(movie);
					}
					JSONObject page_ui = response.getJSONObject("ui");
					if(page_ui.getString("pagingForward").length()>0){
						page_num++;
						pageList3.add(page_ui.getString("pagingForward"));
						loading=true;
						Log.d("page",pageList3.get(page_num));
						Log.d("page",page_num+"");
					}}

				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),
							"Error: " + e.getMessage(),
							Toast.LENGTH_LONG).show();
					Log.d("er",e.getMessage());
				}
				adapter.setItems(movieList);
				adapter.notifyDataSetChanged();
				hidepDialog();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_SHORT).show();
				Log.d("er",error.getMessage());
				// hide the progress dialog
				hidepDialog();
			}
		});

		// Adding request to request queue
		com.shahruie.zanankoochak.app.AppController.getInstance().addToRequestQueue(jsonObjReq);
	}
	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

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
	public void onItemClick(View view, int position) {

		_cd = new ConnectionDetector(getApplicationContext());
		_isInternetPresent = _cd.isConnectingToInternet();
		if (_isInternetPresent) {
			video_id=position;
			if (full_screen.getVisibility()==View.GONE)
				full_screen.setVisibility(View.VISIBLE);
			if (small_web.getVisibility()==View.GONE)
				small_web.setVisibility(View.VISIBLE);
			API.play(small_web,movieList.get(position).getFrame());

		} else {
			Toast.makeText(MainActivity2.this, "اتصال اینترنت برقرار نمیباشد", Toast.LENGTH_LONG).show();
		}

	}
}
