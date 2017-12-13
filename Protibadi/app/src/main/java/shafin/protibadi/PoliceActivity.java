package shafin.protibadi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PoliceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police);

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        String latitude = bundle.getString("chatname");
        String longitude = bundle.getString("id");

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("http://www.google.com/maps/search/police/@"+latitude+","+longitude+",16z");
    }
}
