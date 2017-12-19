package com.example.android.radioapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

public class PlayActivity extends AppCompatActivity {
    RadioItem radioStation;
    TextView title;
    NetworkImageView banner;
    Button browserOpen;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        imageLoader = ImageLoaderSingleton.getInstance(this).getImageLoader();

        title = (TextView) findViewById(R.id.tv_title);
        banner = (NetworkImageView) findViewById(R.id.niv_banner);
        browserOpen = (Button) findViewById(R.id.b_browseropen);

        browserOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWeb();
            }
        });

        Intent i = getIntent();
        radioStation = (RadioItem) i.getSerializableExtra("item");
        populateView();
    }

    private void populateView(){
        banner.setImageUrl(radioStation.getImageURL(),imageLoader);
        title.setText(radioStation.getTitle());
    }

    private void openWeb(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(radioStation.getUrl()));
        startActivity(i);
    }
}
