package com.shahruie.zanankoochak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shahruie.zanankoochak.util.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    // json array response url
    private String urlJsonArry = "https://www.aparat.com/etc/api/videoBySearch/text/%DA%A9%D8%A7%D8%B1%D8%AA%D9%88%D9%86+%D8%B2%D9%86%D8%A7%D9%86+%DA%A9%D9%88%DA%86%DA%A9/perpage/10";

    private static String TAG = MainActivity.class.getSimpleName();
    private WebView webView;
    Boolean _isInternetPresent = false;
    ConnectionDetector _cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.web);
        webView.setBackgroundColor(Color.parseColor("#000000"));
        Intent intent = getIntent();
        String frame = intent.getStringExtra("frame");
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("لطفا منتظر بمانید...");
        pDialog.setCancelable(false);
        _cd = new ConnectionDetector(getApplicationContext());
        _isInternetPresent = _cd.isConnectingToInternet();
        if (_isInternetPresent) {
            API.play(webView,frame);
        } else {
            Toast.makeText(MainActivity.this, "اتصال اینترنت برقرار نمیباشد", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                           String jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("title");
                                String email = person.getString("duration");
                                String home = person.getString("frame");

                                jsonResponse += "title: " + name + "\n\n";
                                jsonResponse += "duration: " + email + "\n\n";
                                jsonResponse += "frame: " + home + "\n\n";

                            }

            //                txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.d("err",e.getMessage());
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("err",error.getMessage());
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void makeJsonObjectRequest() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonArry, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONArray videobysearch = response.getJSONArray("videobysearch");
                    String jsonResponse="";
                    for (int i = 0; i < videobysearch.length(); i++) {

                        JSONObject person = (JSONObject) videobysearch.get(i);

                        String name = person.getString("title");
                        String email = person.getString("duration");
                        String home = person.getString("frame");

                        jsonResponse += "title: " + name + "\n\n";
                        jsonResponse += "duration: " + email + "\n\n";
                        jsonResponse += "frame: " + home + "\n\n";

                    }
          //          txtResponse.setText(jsonResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.d("er",e.getMessage());
                }
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
    @Override
    public void onBackPressed() {
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
        finish();
        super.onBackPressed();
    }
}
