package com.example.mymemes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;

import org.json.JSONException;

import java.lang.annotation.Target;

import javax.sql.DataSource;

public class MainActivity extends AppCompatActivity {
    String currentImageUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMeme();
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextMeme(view);
            }
        });
        Button share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMeme(view);
            }
        });

    }

    private void loadMeme() {
        ProgressBar progressBar = findViewById(R.id.progress);
        final ImageView image = findViewById(R.id.memeimage);
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://meme-api.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        currentImageUrl = response.getString("url");
                        Glide.with(this)
                                .load(currentImageUrl)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, com.bumptech.glide.request.target.Target<Drawable> target, @NonNull com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(image);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show()
        );

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    public void nextMeme(View view) {
        loadMeme();
    }

    public void shareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Check out this meme I got for you " + currentImageUrl);
        Intent chooser = Intent.createChooser(intent, "Share this on ...");
        startActivity(chooser);
    }
}