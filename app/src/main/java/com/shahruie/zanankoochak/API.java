package com.shahruie.zanankoochak;


import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class API
{


    public static String getData(String Address)
    {
        URL url = null;
        try
        {
            url = new URL(Address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

            InputStream in = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                responseOutput.append(line);
            }
            br.close();
            return responseOutput.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return "";
    }

    public static void play(WebView webView, String frame)
    {
        String html = "<style>.h_iframe-aparat_embed_frame{position:relative;} .h_iframe-aparat_embed_frame .ratio {display:block;width:100%;height:auto;} .h_iframe-aparat_embed_frame iframe {position:absolute;top:0;left:0;width:100%; height:100%;}</style><div class=\"h_iframe-aparat_embed_frame\"> <span style=\"display: block;padding-top: 57%\"></span><iframe src=\"" +
                frame +
                "\" allowFullScreen=\"true\" webkitallowfullscreen=\"true\" mozallowfullscreen=\"true\" ></iframe></div>";
        //html = "https://www.aparat.com//video/video/embed/videohash/BKFf8/vt/frame";
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true); // This will allow access
        webView.setWebViewClient(new WebViewClient());

        webView.loadData(html, "text/html; charset=UTF-8", null);
    }
}
