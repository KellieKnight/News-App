package keeknigh.homework4;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Read url from intent
        String myUrl = getIntent().getExtras().getString("url");

        webview = new WebView(this);
        webview.setWebViewClient(new WebViewClient());
        setContentView(webview);
        webview.loadUrl(myUrl);


    }

    public void onBackPressed(){

        //If user can go back in the browser, do so.
        //Otherwise, go back to main activity
        if(webview.canGoBack()){
            webview.goBack();
        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    //Couldn't get this part to work in time. :(
    /*private class MyWebViewClient extends WebViewClient {


        //Read url from intent
                String myUrl = getIntent().getExtras().getString("url");
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains(myUrl)) {
                webview.loadUrl(myUrl);
                return false;
            }
            // Otherwise, the link is not for a page on my site,
            // so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }


    }*/

}